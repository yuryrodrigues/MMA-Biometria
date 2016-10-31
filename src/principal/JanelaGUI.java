package principal;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.AbstractListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class JanelaGUI extends JFrame {
	
	// controlador dos eventos da janela
	JanelaCtrl janelaCtrl = new JanelaCtrl();
	// botoes da toolbar
	JButton btnCadastrar;
	JButton btnVerificar;
	JButton btnRemover;
	JButton btnRemoverTodos;
	JButton btnSobre;
	// box com os dados do usuário cadastrado
	JPanel jpanelBoxInfoUser;
	// lista de usuários cadastrados
	JList listaUser;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JanelaGUI frame = new JanelaGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public JanelaGUI() {
		setVisible(true);
		setResizable(false);
		setTitle("Gerenciar usu\u00E1rios");
		setBounds(100, 100, 545, 410);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		
		setPanel();
	}
	
	// panel
	private void setPanel(){
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		toolBar.setAlignmentY(Component.CENTER_ALIGNMENT);
		toolBar.setMaximumSize(new Dimension(18, 25));
		toolBar.setMinimumSize(new Dimension(18, 25));
		toolBar.setPreferredSize(new Dimension(18, 25));
		getContentPane().add(toolBar, BorderLayout.NORTH);
		
		btnCadastrar = new JButton(" Cadastrar ");
		btnCadastrar.setMaximumSize(new Dimension(70, 30));
		btnCadastrar.setMargin(new Insets(2, 10, 2, 10));
		btnCadastrar.setPreferredSize(new Dimension(75, 30));
		btnCadastrar.setOpaque(false);
		btnCadastrar.setBorder(null);
		btnCadastrar.addActionListener(janelaCtrl);
		toolBar.add(btnCadastrar);
		
		btnVerificar = new JButton(" Verificar ");
		btnVerificar.setMargin(new Insets(2, 10, 2, 10));
		btnVerificar.setPreferredSize(new Dimension(75, 30));
		btnVerificar.setMinimumSize(new Dimension(75, 30));
		btnVerificar.setMaximumSize(new Dimension(70, 30));
		btnVerificar.setOpaque(false);
		btnVerificar.setBorder(null);
		btnVerificar.addActionListener(janelaCtrl);
		toolBar.add(btnVerificar);
		
		btnRemover = new JButton(" Remover ");
		btnRemover.setMargin(new Insets(2, 10, 2, 10));
		btnRemover.setMaximumSize(new Dimension(70, 30));
		btnRemover.setMinimumSize(new Dimension(75, 30));
		btnRemover.setPreferredSize(new Dimension(75, 30));
		btnRemover.setOpaque(false);
		btnRemover.setBorder(null);
		btnRemover.addActionListener(janelaCtrl);
		toolBar.add(btnRemover);
		
		btnRemoverTodos = new JButton(" Remover todos ");
		btnRemoverTodos.setMargin(new Insets(2, 10, 2, 10));
		btnRemoverTodos.setPreferredSize(new Dimension(110, 30));
		btnRemoverTodos.setMinimumSize(new Dimension(110, 30));
		btnRemoverTodos.setMaximumSize(new Dimension(105, 30));
		btnRemoverTodos.setSize(new Dimension(126, 30));
		btnRemoverTodos.setOpaque(false);
		btnRemoverTodos.setBorder(null);
		btnRemoverTodos.addActionListener(janelaCtrl);
		toolBar.add(btnRemoverTodos);
		
		btnSobre = new JButton(" Sobre ");
		btnSobre.setMargin(new Insets(2, 10, 2, 10));
		btnSobre.setMinimumSize(new Dimension(55, 30));
		btnSobre.setMaximumSize(new Dimension(45, 30));
		toolBar.add(btnSobre);
		btnSobre.setPreferredSize(new Dimension(55, 30));
		btnSobre.setOpaque(false);
		btnSobre.addActionListener(janelaCtrl);
		btnSobre.setBorder(null);
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JLabel lblListaUser = new JLabel("Usu\u00E1rios");
		lblListaUser.setHorizontalTextPosition(SwingConstants.LEADING);
		lblListaUser.setFont(new Font("Arial", Font.BOLD, 13));
		lblListaUser.setForeground(new Color(70, 130, 180));
		lblListaUser.setHorizontalAlignment(SwingConstants.CENTER);
		lblListaUser.setBorder(new LineBorder(new Color(70, 130, 180)));
		lblListaUser.setBounds(411, 0, 130, 29);
		panel.add(lblListaUser);
		
		listaUser = new JList();
		listaUser.setFont(new Font("Dialog", Font.PLAIN, 12));
		listaUser.setBorder(new CompoundBorder(new LineBorder(new Color(70, 130, 180)), new EmptyBorder(2, 6, 0, 6)));
		listaUser.setBounds(411, 28, 130, 331);
		listaUser.setModel(new AbstractListModel() {
			String[] values = new String[] {"user 1", "user 2", "user 3", "user 4", "user 5", "user 6", "user 7", "user 8", "user 9", "user 10"};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		panel.add(listaUser);
		
		jpanelBoxInfoUser = new JPanel();
		jpanelBoxInfoUser.setBackground(new Color(255, 250, 250));
		jpanelBoxInfoUser.setBounds(0, 0, 411, 357);
		panel.add(jpanelBoxInfoUser);
		
		JLabel lblNome = new JLabel("Nome:");
		lblNome.setBounds(37, 32, 43, 14);
		lblNome.setFont(new Font("Arial", Font.BOLD, 12));
		
		JTextField txtNome = new JTextField();
		txtNome.setBounds(90, 25, 284, 26);
		txtNome.setColumns(10);
		
		JLabel lblNivelDeAcesso = new JLabel("N\u00EDvel de acesso:");
		lblNivelDeAcesso.setBounds(37, 72, 98, 14);
		lblNivelDeAcesso.setFont(new Font("Arial", Font.BOLD, 12));
		
		JSpinner spinnerNivelAcesso = new JSpinner();
		spinnerNivelAcesso.setBounds(139, 66, 44, 26);
		spinnerNivelAcesso.setModel(new SpinnerNumberModel(1, 1, 3, 1));
		
		JButton btnSalvar = new JButton("Salvar");
		btnSalvar.setBounds(298, 64, 76, 28);
		btnSalvar.setMaximumSize(new Dimension(63, 20));
		btnSalvar.setMinimumSize(new Dimension(63, 20));
		btnSalvar.setPreferredSize(new Dimension(62, 20));
		
		JLabel lblImgDigital = new JLabel("");
		ImageIcon img_icone = new ImageIcon(getClass().getResource("/img/logo.png"));
		lblImgDigital.setIcon(img_icone);
		lblImgDigital.setOpaque(true);
		lblImgDigital.setBounds(37, 106, 337, 182);
		lblImgDigital.setHorizontalAlignment(SwingConstants.CENTER);
		lblImgDigital.setBackground(new Color(211, 211, 211));
		jpanelBoxInfoUser.setLayout(null);
		
		JButton btnSubstituirDigital = new JButton("Substituir digital");
		btnSubstituirDigital.setBounds(37, 293, 337, 28);
		jpanelBoxInfoUser.add(btnSubstituirDigital);
		jpanelBoxInfoUser.add(lblImgDigital);
		jpanelBoxInfoUser.add(lblNivelDeAcesso);
		jpanelBoxInfoUser.add(spinnerNivelAcesso);
		jpanelBoxInfoUser.add(btnSalvar);
		jpanelBoxInfoUser.add(lblNome);
		jpanelBoxInfoUser.add(txtNome);
	}
}
