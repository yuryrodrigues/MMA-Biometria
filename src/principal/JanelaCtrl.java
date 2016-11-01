package principal;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
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
	}
	
	// remove todos os usu�ros do DB
	private void removerTodosUsuarios(){
		// remove os usuario da lista do DB 
		ffv.clearUsers();
		listaUsuarios.removeAllElements();
		
		// atualiza a lista de usu�rios na janela
		janelaDono.listaUser.updateUI();
		
		// salva a nova lista de usu�rioss
		salvarUsuarios();
	}
	
	// remove usuario(s)
	private void removerUser(){
		// pega a lista de usu�ros selecionados
		Object [] usuariosSelecionados = janelaDono.listaUser.getSelectedValues();
		
		for (Object object : usuariosSelecionados) {
			// remove o usu�rio da lista do DB
			ffv.removeUserID(((Usuario)object).getID());
			listaUsuarios.removeElement((Usuario)object);
			
			// atualiza a lista de usu�rios na janela
			janelaDono.listaUser.updateUI();
			
			// salva a nova lista de usu�rioss
			salvarUsuarios();
		}
	}
	
	// verifica um usuario/checa sua digital
	private void verificarUser(){
		// pega o usuario selecionado para verificao
		Usuario usarioSelecionado = (Usuario)janelaDono.listaUser.getSelectedValue();
		
		// se n�o foi selecionado nenhum usu�rio
		if(usarioSelecionado == null){
			JOptionPane.showMessageDialog(janelaDono,"Selecione o usu�rio que deseja verificar","Erro",JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		// busca o usu�rio selecionado no banco de dado
		NffvUser usuarioDB = ffv.getUserByID(usarioSelecionado.getID());
		
		// escaneia a digital e verifica se ela � cmpat�vel com a salva no DB
		int compatibilidadeUsuario = ffv.verify(usuarioDB, TIMEOUT);
		
		// se conseguiu escanear a digital
		if (ffv.getEngineStatus() == NffvStatus.TemplateCreated){
			// se as digitais s�o compativeis
			if( compatibilidadeUsuario > 0){
				JOptionPane.showMessageDialog(janelaDono,usarioSelecionado.getNome() + " verificado. \n Compatibilidade da impress�o digital: " + compatibilidadeUsuario,"Verificado",JOptionPane.DEFAULT_OPTION);
			}
			else{ 
				JOptionPane.showMessageDialog(janelaDono,usarioSelecionado.getNome() + " n�o verificado.\nAs impress�es digitais n�o s�o compativeis.","Falha na verifica��o",JOptionPane.ERROR_MESSAGE);
			}
		}
		else{
			JOptionPane.showMessageDialog(janelaDono,"Falha na verifica��o - " + ffv.getEngineStatus(),"Falha",JOptionPane.ERROR_MESSAGE);
		}
	}
	
	// cadastra um usuario no banco de dados
	private void cadastrarUser(){
		// pega o nome do novo usu�rio
		String nomeUsuario = JOptionPane.showInputDialog(janelaDono,new JLabel("Enter name"),"Enter user name");
		if(nomeUsuario == null) return;
		
		try{
			// l� a digital
			NffvUser novoUsuario = ffv.enroll(TIMEOUT);
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
	}
	
	// salva a lista de usuarios atualizada no banco de dados
	public void salvarUsuarios(){
		// define o arquivo com o banco de dados das digitais
		File arquivoDB = new File(ScannerNffv.getBancoDeDados() + ".fdb");
		try{
			// abre o arquivo para leitura e escrita
			FileOutputStream arquivoOut = new FileOutputStream(arquivoDB);
			ObjectOutputStream arquivo = new ObjectOutputStream(arquivoOut);
			
			// salva a lista de usuarios atualizada no banco de dados
			for (int i = 0; i < listaUsuarios.getSize(); i++){
				arquivo.writeObject(listaUsuarios.get(i));
			}
			
			arquivo.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
