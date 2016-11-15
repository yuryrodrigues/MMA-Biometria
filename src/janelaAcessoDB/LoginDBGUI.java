package janelaAcessoDB;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Toolkit;

public class LoginDBGUI {

	private JDialog jDialog;
	private LoginDBCtrl loginDBCtrl;
	protected JTextField txtNomeUsuario;
	protected JButton btnEscanearDigital;

	/**
	 * Create the dialog.
	 */
	public LoginDBGUI(Principal janelaPrincipal, JanelaGUI janelaDB){
		// inicializa o controlador da janela
		loginDBCtrl = new LoginDBCtrl(this, janelaPrincipal);
		
		// cria a janela
		jDialog = new JDialog(janelaDB, true);
		// cria os componentes
		initComponents();
		
		// centraliza a janela
		jDialog.setLocationRelativeTo(janelaDB);
	}
	
	private void initComponents() {
		JPanel contentPanel = new JPanel();		
		
		jDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);		
		jDialog.setResizable(false);
		jDialog.setTitle("Banco de Dados - Ministério do Meio Ambiente");
		jDialog.setIconImage(Toolkit.getDefaultToolkit().getImage(LoginDBGUI.class.getResource("/img/icon-digital-verificada.png")));
		jDialog.setBounds(100, 100, 286, 152);
		jDialog.getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		jDialog.getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblNomeUsuario = new JLabel("Digite o seu nome de usuário:");
			lblNomeUsuario.setBounds(11, 12, 181, 16);
			contentPanel.add(lblNomeUsuario);
		}
		
		txtNomeUsuario = new JTextField();
		txtNomeUsuario.setBounds(11, 34, 257, 28);
		contentPanel.add(txtNomeUsuario);
		txtNomeUsuario.setColumns(10);
		
		btnEscanearDigital = new JButton("escanear digital");
		btnEscanearDigital.setBounds(11, 75, 257, 33);
		btnEscanearDigital.addActionListener(loginDBCtrl);
		contentPanel.add(btnEscanearDigital);	
	}
	
	protected JDialog getJDialog(){
		return jDialog;
	}
}
