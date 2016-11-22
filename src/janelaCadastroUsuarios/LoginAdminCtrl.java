package janelaCadastroUsuarios;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import com.neurotechnology.Nffv.Nffv;
import com.neurotechnology.Nffv.NffvUser;

import compartilhada.ScannerNffv;
import compartilhada.SobreGUI;
import compartilhada.Usuario;

public class LoginAdminCtrl implements ActionListener {
	// janela principal da aplicacao
	private Principal janelaPrincipal;
	// janela da GUI da aplicacao
	private JanelaGUI janelaGUI;
	// JanelaGUI e o sdk para manipular o scanner
	private LoginAdminGUI janelaDono;
	private Nffv ffv;
	
	public LoginAdminCtrl(LoginAdminGUI janelaDono, Principal janelaPrincipal, JanelaGUI janelaGUI){
		this.janelaDono 		= janelaDono;		
		this.janelaPrincipal 	= janelaPrincipal;
		this.janelaGUI			= janelaGUI;
		this.ffv 				= ScannerNffv.getNffv();
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
				janelaGUI.janelaCtrl.criaUsuario(ScannerNffv.getAdminDB());
				
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
		// pega o o admin selecionado na lista
		Usuario adminSelecionado = (Usuario)janelaGUI.janelaCtrl.listaUsuarios.getElementAt(0);
		
		// busca o usuário administrador do bd					
		NffvUser adminUsuario = ffv.getUserByID(((Usuario)adminSelecionado).getID());
		
		// verifica se é o verdadeiro administrador
		int usuarioValidado = janelaGUI.janelaCtrl.confirmaUsuario(adminUsuario);
		if(usuarioValidado == 1){
			// indica que o login foi feito com sucesso
			adminLogado();
		}
		else if(usuarioValidado == 0){
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
}
