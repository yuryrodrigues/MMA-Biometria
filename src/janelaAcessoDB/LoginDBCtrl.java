package janelaAcessoDB;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import com.neurotechnology.Nffv.Nffv;
import com.neurotechnology.Nffv.NffvStatus;
import com.neurotechnology.Nffv.NffvUser;

import compartilhada.JDialogProgressoLeituraDigital;
import compartilhada.ScannerNffv;
import compartilhada.SobreGUI;
import compartilhada.TrataErrosExcecaoEscaner;
import compartilhada.Usuario;

public class LoginDBCtrl implements ActionListener {
	// janela principal da aplicacao
	private Principal janelaPrincipal;
	// JanelaGUI e o sdk para manipular o scanner
	private LoginDBGUI janelaDono;
	private Nffv ffv;
	// lista de usuarios
	private HashMap<String, Usuario> listaUsuarios;
	// janela de progresso da leitura da digital
	private JDialogProgressoLeituraDigital jDialogProgressoLeituraDigital;
	// trata os erros e excecoes do escaner
	private TrataErrosExcecaoEscaner trataErrosExcecaoEscaner;
	
	public LoginDBCtrl(LoginDBGUI janelaDono, Principal janelaPrincipal){
		this.janelaDono 		= janelaDono;
		this.ffv 				= ScannerNffv.getNffv();
		this.janelaPrincipal 	= janelaPrincipal;
		
		// lê a lista de usuário cadastrados no banco de dados
		carregaListaUsuarios();
		
		// janela de progresso da leitura da digital
		jDialogProgressoLeituraDigital = new JDialogProgressoLeituraDigital(this.janelaDono.getJDialog());
		// trata os erros e excecoes do escaner
		trataErrosExcecaoEscaner = new TrataErrosExcecaoEscaner(this.janelaDono.getJDialog());
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == janelaDono.btnEscanearDigital){
			// caso nao tenha digitado o nome do usuario
			if(janelaDono.txtNomeUsuario.getText().length() == 0){
				JOptionPane.showMessageDialog(janelaDono.getJDialog(),
						"Digite seu nome de usuário!",
						"",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			else{
				// busca o usuario no DB
				Usuario usuario = listaUsuarios.get(janelaDono.txtNomeUsuario.getText());
				
				// se o usuario nao existir
				if(usuario == null){
					JOptionPane.showMessageDialog(janelaDono.getJDialog(),
							"O nome de usuário é inválido",
							"",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				else{
					// verifica a digital
					verificaDigital(usuario);
				}
			}
		}
	}
	
	// verifica a digital
	private void verificaDigital(Usuario usuario){
		// busca o usuário selecionado no banco de dado
		NffvUser usuarioDB = ffv.getUserByID(usuario.getID());
		
		// executa a leitura da digital no plano de fundo
        SwingWorker<Integer,Void> worker = new SwingWorker<Integer,Void>(){
            @Override
            protected Integer doInBackground(){
            	// le a digital do usuario
                return ffv.verify(usuarioDB, ScannerNffv.TIMEOUT);
            }
         
            @Override
            protected void done(){
            	// fecha a janela de progresso
            	jDialogProgressoLeituraDigital.exibe(false);
            }
        };
        worker.execute();
        
        // exibe a janela de progresso
        jDialogProgressoLeituraDigital.exibe(true);
        
        // retorna a digital lida
        int compatibilidadeUsuario = 0;
		try {
			compatibilidadeUsuario = worker.get();
		} catch (InterruptedException | ExecutionException e) {
			trataErrosExcecaoEscaner.excecao(e);
		}
		
		// se conseguiu escanear a digital
		if (ffv.getEngineStatus() == NffvStatus.TemplateCreated){
			// se as digitais são compativeis
			if( compatibilidadeUsuario > 0){
				// salva os dados do usuario logado
				janelaPrincipal.setUsuarioLogado(usuario);
				
				// fecha a janela de dialogo
				janelaDono.getJDialog().dispose();
			}
			else{
				// icone da janela
				ImageIcon imageIcon = new ImageIcon(SobreGUI.class.getResource("/img/icon-digital-nao-verificada.png"));
		        Image image 		= imageIcon.getImage();
		        Image novaImg 		= image.getScaledInstance(75, 63,  java.awt.Image.SCALE_SMOOTH);
		        imageIcon 			= new ImageIcon(novaImg);
		        
				JOptionPane.showMessageDialog(janelaDono.getJDialog(),
						"As impressões digitais não são compativeis.",
						"Falha na verificação",
						JOptionPane.ERROR_MESSAGE,
						imageIcon);
			}
		}
		else{
			trataErrosExcecaoEscaner.erro(ffv.getEngineStatus());
		}
	}
	
	// lê a lista de usuário cadastrados no banco de dados
	protected void carregaListaUsuarios(){
		// cria a lista de usuarios
		listaUsuarios = new HashMap<>();
		
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
					// adiciona o usuario a lista de usuarios
					listaUsuarios.put(usuario.getNomeUsuario(), usuario);
				}
				
				arquivo.close();
			}catch (EOFException eof){}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
