package janelaCadastroUsuarios;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class LoginAdminGUI {

	private JDialog jDialog;
	private LoginAdminCtrl loginAdminCtrl;
	protected JLabel lblMsgInfo;
	protected JButton btnEscanearDigital;

	/**
	 * Create the dialog.
	 */
	public LoginAdminGUI(Principal janelaPrincipal, JanelaGUI janelaGUI){		
		// inicializa o controlador da janela
		loginAdminCtrl = new LoginAdminCtrl(this, janelaPrincipal, janelaGUI);
		
		// cria a janela
		jDialog = new JDialog(janelaGUI, true);
		
		// cria os componentes
		initComponents();
		
		loginAdminCtrl.atualizaDadosJanela();
		
		// centraliza a janela
		jDialog.setLocationRelativeTo(janelaGUI);	
	}
	
	private void initComponents() {
		JPanel contentPanel = new JPanel();		
		
		jDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);		
		jDialog.setResizable(false);
		jDialog.setTitle("Banco de Dados - Ministério do Meio Ambiente");
		jDialog.setIconImage(Toolkit.getDefaultToolkit().getImage(LoginAdminGUI.class.getResource("/img/icon-digital-verificada.png")));
		jDialog.setBounds(100, 100, 286, 138);
		jDialog.getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		jDialog.getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		btnEscanearDigital = new JButton("cadastrar digital");
		btnEscanearDigital.setBounds(11, 51, 257, 33);
		btnEscanearDigital.addActionListener(loginAdminCtrl);
		contentPanel.add(btnEscanearDigital);
		
		lblMsgInfo = new JLabel("É preciso cadastrar a digital do Administrador:");
		lblMsgInfo.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblMsgInfo.setBounds(12, 15, 256, 16);
		contentPanel.add(lblMsgInfo);
	}
	
	protected JDialog getJDialog(){
		return jDialog;
	}
}
