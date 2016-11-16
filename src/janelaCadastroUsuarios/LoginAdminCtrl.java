package janelaCadastroUsuarios;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

public class LoginAdminCtrl implements ActionListener {
	// janela principal da aplicacao
	private Principal janelaPrincipal;
	// janela da GUI da aplicacao
	private JanelaGUI janelaGUI;
	// JanelaGUI e o sdk para manipular o scanner
	private LoginAdminGUI janelaDono;
	private Nffv ffv;
	// lista de usuarios
	private HashMap<String, Usuario> listaUsuarios;
	// janela de progresso da leitura da digital
	private JDialogProgressoLeituraDigital jDialogProgressoLeituraDigital;
	// trata os erros e excecoes do escaner
	private TrataErrosExcecaoEscaner trataErrosExcecaoEscaner;
	
	public LoginAdminCtrl(LoginAdminGUI janelaDono, Principal janelaPrincipal, JanelaGUI janelaGUI){
		this.janelaDono 		= janelaDono;
		this.ffv 				= ScannerNffv.getNffv();
		this.janelaPrincipal 	= janelaPrincipal;
		this.janelaGUI			= janelaGUI;
		
		// janela de progresso da leitura da digital
		jDialogProgressoLeituraDigital = new JDialogProgressoLeituraDigital(this.janelaDono.getJDialog());
		// trata os erros e excecoes do escaner
		trataErrosExcecaoEscaner = new TrataErrosExcecaoEscaner(this.janelaDono.getJDialog());
	}
	
	protected void atualizaDadosJanela(){
		// mostra o texto da caixa de login
		// verifica se o administrador ja esta cadastrado
		if(!ffv.getUsers().isEmpty()){			
			// caso ja esteja
			janelaDono.btnEscanearDigital.setText("escanear digital");
			janelaDono.lblMsgInfo.setText("É preciso verificar se você é o Administrador:");
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == janelaDono.btnEscanearDigital){	
			// verifica se o administrador ja esta cadastrado
			if(ffv.getUsers().isEmpty()){
				// cadastra a digital do administrador
				janelaGUI.janelaCtrl.criaUsuario("adminMMA");
				
				// indica que o login foi feito com sucesso
				adminLogado();
			}
			else{
				// verifica a digital do administrador
				verificaDigitalAdmin();
			}
		}
	}
	
	// caso o login ou cadastro do admin tenha sido feito
	private void adminLogado(){
		// salva os dados do usuario logado
		janelaPrincipal.setAdminLogado(true);
		
		// fecha a janela de dialogo
		janelaDono.getJDialog().dispose();
	}
	
	// verifica a digital
	private void verificaDigitalAdmin(){		
		// busca o usuário administrador do bd
		NffvUser usuarioDB = ffv.getUsers().get(0);
		
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
				// indica que o login foi feito com sucesso
				adminLogado();
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
}
