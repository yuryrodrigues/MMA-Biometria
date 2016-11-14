package janelaAcessoInformacao;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Toolkit;

public class loginDB extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtNomeUsuario;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			loginDB dialog = new loginDB();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public loginDB() {
		setResizable(false);
		setTitle("Banco de Dados - Ministério do Meio Ambiente");
		setIconImage(Toolkit.getDefaultToolkit().getImage(loginDB.class.getResource("/img/icon-digital-verificada.png")));
		setBounds(100, 100, 286, 152);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
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
		
		JButton btnEscanearDigital = new JButton("escanear digital");
		btnEscanearDigital.setBounds(11, 75, 257, 33);
		contentPanel.add(btnEscanearDigital);
	}
}
