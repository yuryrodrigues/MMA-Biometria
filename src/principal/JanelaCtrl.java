package principal;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;

import com.neurotechnology.Nffv.Nffv;
import com.neurotechnology.Nffv.NffvStatus;
import com.neurotechnology.Nffv.NffvUser;

public class JanelaCtrl implements ActionListener {

	// tempo maximo de tentativa de leitura da digital
	static final int TIMEOUT = 10000;
	
	// JanelaGUI e o sdk para manipular o scanner
	private JanelaGUI janelaDono;
	private Nffv ffv;
	
	// lista de usuarios cadastrados
	DefaultListModel listaUsuarios;;
	
	// janelas de dialogo
	JDialog jDialogProgressoLeitura;
	
	public JanelaCtrl(JanelaGUI janela){
		// o endereco da JanelaGUI
		janelaDono = janela;
		
		// cria a lista que armazenara os usuarios cadastrados
		listaUsuarios = new DefaultListModel();		
		
		// busca o objeto da classe que manipular� o scanner e o DB
		ffv = ScannerNffv.getNffv();
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// se o usuario pediu para cadastrar um novo usu�rio
		if(arg0.getSource() == janelaDono.btnCadastrar){
			cadastrarUser();
		}		
		else if(arg0.getSource() == janelaDono.btnVerificar){
			verificarUser();
		}		
		else if(arg0.getSource() == janelaDono.btnRemover){
			removerUser();
		}		
		else if(arg0.getSource() == janelaDono.btnRemoverTodos){
			removerTodosUsuarios();
		}	
		
		// atualiza o box com os dados do usu�rio
		atualizaBoxUser();
	}
	
	// remove todos os usu�ros do DB
	private void removerTodosUsuarios(){
		// pergunta se deseja remover todo os usuarios
		Object[] options = { "SIM", "N�O" };	
		int desejaRemover = JOptionPane.showOptionDialog(janelaDono,
			    "Deseja remover todos os usu�rios cadastrados?",
			    "",
			    JOptionPane.YES_NO_OPTION,
			    JOptionPane.WARNING_MESSAGE,
			    null,
			    options,
			    options[1]);
		
		// caso n�o deseje, n�o faz nada
		if(desejaRemover == 1) return;
		
		// remove os usuario da lista do DB 
		ffv.clearUsers();
		listaUsuarios.removeAllElements();
		
		// atualiza a lista de usu�rios na janela
		atualizaListaUserJanela();
		
		// salva a nova lista de usu�rioss
		salvarUsuarios();
	}
	
	// remove usuario(s)
	private void removerUser(){
		// pega a lista de usu�ros selecionados
		Object [] usuariosSelecionados = janelaDono.listaUser.getSelectedValues();
		
		// se n�o foi selecionado nenhum usu�rio
		if(usuariosSelecionados.length == 0){
			JOptionPane.showMessageDialog(janelaDono,"Selecione o usu�rio que deseja remover","",JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		// pergunta se deseja remover o usuario		
		Object[] options = { "SIM", "N�O" };	
		int desejaRemover = JOptionPane.showOptionDialog(janelaDono,
			    "Deseja remover este(s) usu�rio(s)?",
			    "",
			    JOptionPane.YES_NO_OPTION,
			    JOptionPane.WARNING_MESSAGE,
			    null,
			    options,
			    options[1]);
		
		// caso n�o deseje, n�o faz nada
		if(desejaRemover == 1) return;
		
		for (Object object : usuariosSelecionados) {
			// remove o usu�rio da lista do DB
			ffv.removeUserID(((Usuario)object).getID());
			listaUsuarios.removeElement((Usuario)object);
			
			// atualiza a lista de usu�rios na janela
			atualizaListaUserJanela();
			
			// salva a nova lista de usu�rioss
			salvarUsuarios();
		}
		
		// seleciona o primeiro usu�rio na lista de usuarios
		janelaDono.listaUser.setSelectedIndex(janelaDono.listaUser.getFirstVisibleIndex());
	}
	
	// verifica um usuario/checa sua digital
	private void verificarUser(){
		// pega o usuario selecionado para verificao
		Usuario usuarioSelecionado = (Usuario)janelaDono.listaUser.getSelectedValue();
		
		// se n�o foi selecionado nenhum usu�rio
		if(usuarioSelecionado == null){
			JOptionPane.showMessageDialog(janelaDono,"Selecione o usu�rio que deseja verificar","",JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		// busca o usu�rio selecionado no banco de dado
		NffvUser usuarioDB = ffv.getUserByID(usuarioSelecionado.getID());
		
		// executa a leitura da digital no plano de fundo
        SwingWorker<Integer,Void> worker = new SwingWorker<Integer,Void>(){
            @Override
            protected Integer doInBackground(){
            	// le a digital do usuario
                return ffv.verify(usuarioDB, TIMEOUT);
            }
         
            @Override
            protected void done(){
            	// fecha a janela de progresso
            	jDialogEscaneandoDigital(false);
            }
        };
        worker.execute();
        
        // exibe a janela de progresso
        jDialogEscaneandoDigital(true);
        
        // retorna a digital lida
        int compatibilidadeUsuario = 0;
		try {
			compatibilidadeUsuario = worker.get();
		} catch (InterruptedException | ExecutionException e) {}
		
		// se conseguiu escanear a digital
		if (ffv.getEngineStatus() == NffvStatus.TemplateCreated){
			// se as digitais s�o compativeis
			if( compatibilidadeUsuario > 0){
				JOptionPane.showMessageDialog(janelaDono,usuarioSelecionado.getNome() + " verificado. \n Compatibilidade da impress�o digital: " + compatibilidadeUsuario,"Verificado",JOptionPane.DEFAULT_OPTION);
			}
			else{ 
				JOptionPane.showMessageDialog(janelaDono,usuarioSelecionado.getNome() + " n�o verificado.\nAs impress�es digitais n�o s�o compativeis.","Falha na verifica��o",JOptionPane.ERROR_MESSAGE);
			}
		}
		else{
			JOptionPane.showMessageDialog(janelaDono,"Falha na verifica��o - " + ffv.getEngineStatus(),"Falha",JOptionPane.ERROR_MESSAGE);
		}
	}
	
	// cadastra um usuario no banco de dados
	private void cadastrarUser(){
		// pega o nome do novo usu�rio
		String nomeUsuario = JOptionPane.showInputDialog(janelaDono, new JLabel("Digite o nome do usu�rio"), "Cadastrar usu�rio", JOptionPane.QUESTION_MESSAGE);
		if(nomeUsuario == null) return;
		
		try{
			// executa a leitura da digital no plano de fundo
	        SwingWorker<NffvUser,Void> worker = new SwingWorker<NffvUser,Void>(){
	            @Override
	            protected NffvUser doInBackground(){
	            	// le a digital do usuario
	                return ffv.enroll(TIMEOUT);
	            }
	         
	            @Override
	            protected void done(){
	            	// fecha a janela de progresso
	            	jDialogEscaneandoDigital(false);
	            }
	        };
	        worker.execute();
	        
	        // exibe a janela de progresso
	        jDialogEscaneandoDigital(true);
	        
	        // retorna a digital lida
	        NffvUser novoUsuario = worker.get();
	     
			System.out.println(ffv.getEngineStatus());
			
			// se n�o conseguiu ler a digital
			if(ffv.getEngineStatus() != NffvStatus.TemplateCreated){
				JOptionPane.showMessageDialog(janelaDono, "Falha no cadastro da digital - " + ffv.getEngineStatus(), "Falhou", JOptionPane.ERROR_MESSAGE);
				return;
			}
		
			// exibe a digital na tela de cadastro
			janelaDono.lblImgDigital.setIcon(novoUsuario.getNffvImage().getImageIcon());
			
			// adiciona o usuario na lista de usuarios cadastrados
			listaUsuarios.addElement(new Usuario(novoUsuario.getID(),nomeUsuario));
			
			// salva o usuario no DB
			salvarUsuarios();
		}catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(janelaDono, "Falha no cadastro da digital - " + e.getMessage(), "Falhou", JOptionPane.ERROR_MESSAGE);
		}
		
		// seleciona o usuario cadastrado na lista de usuarios
		janelaDono.listaUser.setSelectedIndex(janelaDono.listaUser.getLastVisibleIndex());
	}
	
	// salva a lista de usuarios atualizada no banco de dados
	private void salvarUsuarios(){
		// define o arquivo com o banco de dados das digitais
		File arquivoDB = new File(ScannerNffv.getBancoDeDados() + ".fdb");
		try{
			// abre o arquivo para leitura e escrita
			FileOutputStream arquivoOut = new FileOutputStream(arquivoDB);
			ObjectOutputStream arquivo 	= new ObjectOutputStream(arquivoOut);
			
			// salva a lista de usuarios atualizada no banco de dados
			for (int i = 0; i < listaUsuarios.getSize(); i++){
				arquivo.writeObject(listaUsuarios.get(i));
			}
			
			arquivo.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// l� a lista de usu�rio cadastrados no banco de dados
	protected void carregaListaUsuarios(){
		// define o arquivo com o banco de dados das digitais
		File arquivoDB = new File(ScannerNffv.getBancoDeDados() + ".fdb");
		
		// verifica se o arquivo existe
		if(arquivoDB.exists()){
			try{
				// abre o arquivo para leitura e escrita
				FileInputStream arquivoIn 	= new FileInputStream(arquivoDB);
				ObjectInputStream arquivo 	= new ObjectInputStream(arquivoIn);
				
				// le os usuarios do arquivo de banco de dados
				for (Usuario usuario = (Usuario)arquivo.readObject(); usuario != null; usuario = (Usuario)arquivo.readObject()){
					System.out.println(usuario);
					// adiciona o usuario a lista de usuarios
					listaUsuarios.addElement(usuario);
				}
				
				arquivo.close();
			}catch (EOFException eof){}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		// adiciona o modelo com a lista de usu�rios na janela
		janelaDono.listaUser.setModel(listaUsuarios);
		
		// seleciona o primeiro usu�rio na lista de usuarios
		janelaDono.listaUser.setSelectedIndex(0);
		
		// atualiza o box com os dados do usu�rio
		atualizaBoxUser();
	}
		
	// atualiza a lista de usu�rios na janela
	private void atualizaListaUserJanela(){
		// atualiza a lista de usu�rios na janela
		janelaDono.listaUser.updateUI();
	}
	
	// atualiza o box com os dados do usu�rio
	private void atualizaBoxUser(){
		// pega o usuario selecionado na lista
		Usuario usuarioSelecionado = (Usuario)janelaDono.listaUser.getSelectedValue();
		
		// caso n�o tenha nenhum usu�rio selecionado, limpa o box de informa��es do usu�rio
		if(usuarioSelecionado == null){
			janelaDono.txtNome.setText("");
			janelaDono.spinnerNivelAcesso.setValue(1);
			janelaDono.lblImgDigital.setIcon(null);
			janelaDono.txtNome.setEnabled(false);
			janelaDono.spinnerNivelAcesso.setEnabled(false);
			janelaDono.btnSalvarDadosUser.setEnabled(false);
			janelaDono.btnSubstituirDigitalUser.setEnabled(false);
			return;
		}
		
		// busca os dados do usuario selecionado no DB
		NffvUser usuario = ffv.getUserByID(usuarioSelecionado.getID());
		
		// atualiza o box com as informa��es do usu�rio
		janelaDono.txtNome.setText(usuarioSelecionado.getNome());
		janelaDono.spinnerNivelAcesso.setValue(usuarioSelecionado.getNivelAcesso());
		try {
			janelaDono.lblImgDigital.setIcon(usuario.getNffvImage().getImageIcon());
		} catch (Exception e) {e.printStackTrace();}
		
		janelaDono.txtNome.setEnabled(true);
		janelaDono.spinnerNivelAcesso.setEnabled(true);
		janelaDono.btnSalvarDadosUser.setEnabled(true);
		janelaDono.btnSubstituirDigitalUser.setEnabled(true);
	}

	// exibe uma janela de dialogo informando que esta escaneado a digital
	private void jDialogEscaneandoDigital(boolean op){		
		if(jDialogProgressoLeitura == null){
			/* cria a janela de dialogo informando que esta escaneando a digital */
	        jDialogProgressoLeitura = new JDialog(janelaDono, true);	        
	        jDialogProgressoLeitura.setResizable(false);
	        jDialogProgressoLeitura.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
	        jDialogProgressoLeitura.setBounds(100, 100, 255, 117);
	        jDialogProgressoLeitura.setPreferredSize(new Dimension(255, 117));
	        jDialogProgressoLeitura.getContentPane().setLayout(new BorderLayout());
	        JPanel contentPanel = new JPanel();
	        contentPanel.setBackground(Color.WHITE);
			contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
			jDialogProgressoLeitura.getContentPane().add(contentPanel, BorderLayout.CENTER);
			contentPanel.setLayout(new GridLayout(0, 1, 0, 0));
			
			JLabel lblNewLabel = new JLabel("Escaneando a digital...");
			//lblNewLabel.setBounds(25, 11, 199, 16);
			contentPanel.add(lblNewLabel);
			
			JProgressBar progressBar = new JProgressBar();
			progressBar.setString("0");
			progressBar.setForeground(SystemColor.textHighlight);
			progressBar.setIndeterminate(true);
			//progressBar.setBounds(25, 38, 199, 28);
			contentPanel.add(progressBar);			
			
			jDialogProgressoLeitura.setLocationRelativeTo(janelaDono);
			jDialogProgressoLeitura.pack();
		}
		
		// exibe ou fecha a janela de progresso
		if(op == true){
			jDialogProgressoLeitura.setVisible(true);
		}
		else{
			jDialogProgressoLeitura.dispose();
		}
	}
}
