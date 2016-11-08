package principal;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

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
	
	public JanelaCtrl(JanelaGUI janela){
		// o endereco da JanelaGUI
		janelaDono = janela;
		
		// cria a lista que armazenara os usuarios cadastrados
		listaUsuarios = new DefaultListModel();		
		
		// busca o objeto da classe que manipulará o scanner e o DB
		ffv = ScannerNffv.getNffv();
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// se o usuario pediu para cadastrar um novo usuário
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
		
		// atualiza o box com os dados do usuário
		atualizaBoxUser();
	}
	
	// remove todos os usuáros do DB
	private void removerTodosUsuarios(){
		// remove os usuario da lista do DB 
		ffv.clearUsers();
		listaUsuarios.removeAllElements();
		
		// atualiza a lista de usuários na janela
		atualizaListaUserJanela();
		
		// salva a nova lista de usuárioss
		salvarUsuarios();
	}
	
	// remove usuario(s)
	private void removerUser(){
		// pega a lista de usuáros selecionados
		Object [] usuariosSelecionados = janelaDono.listaUser.getSelectedValues();
		
		// se não foi selecionado nenhum usuário
		if(usuariosSelecionados.length == 0){
			JOptionPane.showMessageDialog(janelaDono,"Selecione o usuário que deseja remover","",JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		for (Object object : usuariosSelecionados) {
			// remove o usuário da lista do DB
			ffv.removeUserID(((Usuario)object).getID());
			listaUsuarios.removeElement((Usuario)object);
			
			// atualiza a lista de usuários na janela
			atualizaListaUserJanela();
			
			// salva a nova lista de usuárioss
			salvarUsuarios();
		}
	}
	
	// verifica um usuario/checa sua digital
	private void verificarUser(){
		// pega o usuario selecionado para verificao
		Usuario usuarioSelecionado = (Usuario)janelaDono.listaUser.getSelectedValue();
		
		// se não foi selecionado nenhum usuário
		if(usuarioSelecionado == null){
			JOptionPane.showMessageDialog(janelaDono,"Selecione o usuário que deseja verificar","",JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		// busca o usuário selecionado no banco de dado
		NffvUser usuarioDB = ffv.getUserByID(usuarioSelecionado.getID());
		
		// escaneia a digital e verifica se ela é cmpatível com a salva no DB
		int compatibilidadeUsuario = ffv.verify(usuarioDB, TIMEOUT);
		
		// se conseguiu escanear a digital
		if (ffv.getEngineStatus() == NffvStatus.TemplateCreated){
			// se as digitais são compativeis
			if( compatibilidadeUsuario > 0){
				JOptionPane.showMessageDialog(janelaDono,usuarioSelecionado.getNome() + " verificado. \n Compatibilidade da impressão digital: " + compatibilidadeUsuario,"Verificado",JOptionPane.DEFAULT_OPTION);
			}
			else{ 
				JOptionPane.showMessageDialog(janelaDono,usuarioSelecionado.getNome() + " não verificado.\nAs impressões digitais não são compativeis.","Falha na verificação",JOptionPane.ERROR_MESSAGE);
			}
		}
		else{
			JOptionPane.showMessageDialog(janelaDono,"Falha na verificação - " + ffv.getEngineStatus(),"Falha",JOptionPane.ERROR_MESSAGE);
		}
	}
	
	// cadastra um usuario no banco de dados
	private void cadastrarUser(){
		// pega o nome do novo usuário
		String nomeUsuario = JOptionPane.showInputDialog(janelaDono, new JLabel("Digite o nome do usuário"), "Cadastrar usuário", JOptionPane.QUESTION_MESSAGE);
		if(nomeUsuario == null) return;
		
		try{
			// lê a digital
			NffvUser novoUsuario = ffv.enroll(TIMEOUT);
			System.out.println(ffv.getEngineStatus());
		
			// se não conseguiu ler a digital
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
		
		// atualiza o box com as informações do usuario
		atualizaBoxUser();
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
	
	// salva a lista de usuarios atualizada no banco de dados
	protected void carregaListaUsuarios(){
		// define o arquivo com o banco de dados das digitais
		File arquivoDB = new File(ScannerNffv.getBancoDeDados() + ".fdb");
		System.out.println("qt lista use:"+listaUsuarios.size());
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
		
		// adiciona o modelo com a lista de usuários na janela
		janelaDono.listaUser.setModel(listaUsuarios);
	}
		
	// atualiza a lista de usuários na janela
	private void atualizaListaUserJanela(){
		// atualiza a lista de usuários na janela
		janelaDono.listaUser.updateUI();
	}
	
	// atualiza o box com os dados do usuário
	private void atualizaBoxUser(){
		// pega o usuario selecionado na lista
		Usuario usuarioSelecionado = (Usuario)janelaDono.listaUser.getSelectedValue();
		
		// caso não tenha nenhum usuário selecionado, limpa o box de informações do usuário
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
		
		// atualiza o box com as informações do usuário
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
}
