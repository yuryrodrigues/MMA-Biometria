package janelaCadastroUsuarios;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.Toolkit;

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
	protected JanelaCtrl janelaCtrl;
	// botoes da toolbar
	protected JButton btnCadastrar;
	protected JButton btnVerificar;
	protected JButton btnRemover;
	protected JButton btnRemoverTodos;
	protected JButton btnSobre;
	// box com os dados do usuario cadastrado
	protected JPanel jpanelBoxInfoUser;
	protected JTextField txtNome;
	protected JTextField txtNomeUsuario;
	protected JSpinner spinnerNivelAcesso;
	protected JLabel lblImgDigital;
	protected JButton btnSalvarDadosUser;
	protected JButton btnSubstituirDigitalUser;
	protected JLabel lblMsgDadosSalvos;
	// lista de usuarios cadastrados
	protected JList listaUser;	

	/**
	 * Create the frame.
	 */
	protected JanelaGUI() {
		// cria o objeto controlador da janela
		janelaCtrl = new JanelaCtrl(this);
		
		// carrega a janela
		setIconImage(Toolkit.getDefaultToolkit().getImage(JanelaGUI.class.getResource("/img/icon-digital-verificada.png")));
		setVisible(true);
		setResizable(false);
		setTitle("Gerenciar usu\u00E1rios");
		setBounds(100, 100, 569, 446);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		setLocationRelativeTo(null);
		
		setPanel();
		
		// oculta os componentes da janela
		exibe(false);
		
		// exibe a lista de usuarios
		janelaCtrl.mostraListaUsuarios();
	}
	
	// oculta os componentes da janela
	protected void exibe(boolean op){
		getContentPane().setVisible(op);
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
		lblListaUser.setBounds(411, 0, 152, 29);
		panel.add(lblListaUser);
		
		listaUser = new JList();
		listaUser.setFont(new Font("Dialog", Font.PLAIN, 12));
		listaUser.setBorder(new CompoundBorder(new LineBorder(new Color(70, 130, 180)), new EmptyBorder(2, 6, 0, 6)));
		listaUser.setBounds(411, 28, 152, 364);		
		listaUser.addListSelectionListener(janelaCtrl);
		
		panel.add(listaUser);
		
		jpanelBoxInfoUser = new JPanel();
		jpanelBoxInfoUser.setBackground(SystemColor.window);
		jpanelBoxInfoUser.setBounds(0, 0, 411, 392);
		panel.add(jpanelBoxInfoUser);
		
		JLabel lblNome = new JLabel("Nome:");
		lblNome.setBounds(37, 35, 43, 14);
		lblNome.setFont(new Font("Arial", Font.BOLD, 12));
		
		txtNome = new JTextField();
		txtNome.setEnabled(false);
		txtNome.setBounds(90, 28, 284, 26);
		txtNome.setColumns(10);
		
		JLabel lblUsuario = new JLabel("Usuário:");
		lblUsuario.setFont(new Font("Arial", Font.BOLD, 12));
		lblUsuario.setBounds(37, 68, 52, 14);
		jpanelBoxInfoUser.add(lblUsuario);
		
		txtNomeUsuario = new JTextField();
		txtNomeUsuario.setEnabled(false);
		txtNomeUsuario.setColumns(10);
		txtNomeUsuario.setBounds(90, 61, 284, 26);
		jpanelBoxInfoUser.add(txtNomeUsuario);
		
		JLabel lblNivelDeAcesso = new JLabel("N\u00EDvel de acesso:");
		lblNivelDeAcesso.setBounds(37, 107, 98, 14);
		lblNivelDeAcesso.setFont(new Font("Arial", Font.BOLD, 12));
		
		spinnerNivelAcesso = new JSpinner();
		spinnerNivelAcesso.setEnabled(false);
		spinnerNivelAcesso.setBounds(139, 101, 44, 26);
		spinnerNivelAcesso.setModel(new SpinnerNumberModel(1, 1, 3, 1));
		
		btnSalvarDadosUser = new JButton("Salvar");
		btnSalvarDadosUser.setEnabled(false);
		btnSalvarDadosUser.setBounds(298, 99, 76, 28);
		btnSalvarDadosUser.setMaximumSize(new Dimension(63, 20));
		btnSalvarDadosUser.setMinimumSize(new Dimension(63, 20));
		btnSalvarDadosUser.setPreferredSize(new Dimension(62, 20));
		btnSalvarDadosUser.addActionListener(janelaCtrl);
		
		lblImgDigital = new JLabel("");
		lblImgDigital.setOpaque(true);
		lblImgDigital.setBounds(37, 141, 337, 182);
		lblImgDigital.setHorizontalAlignment(SwingConstants.CENTER);
		lblImgDigital.setBackground(new Color(211, 211, 211));
		jpanelBoxInfoUser.setLayout(null);
		
		btnSubstituirDigitalUser = new JButton("Substituir digital");
		btnSubstituirDigitalUser.setEnabled(false);
		btnSubstituirDigitalUser.setBounds(37, 328, 337, 28);
		btnSubstituirDigitalUser.addActionListener(janelaCtrl);
		
		lblMsgDadosSalvos = new JLabel("Dados salvos com sucesso!");
		lblMsgDadosSalvos.setHorizontalAlignment(SwingConstants.CENTER);
		lblMsgDadosSalvos.setVisible(false);
		lblMsgDadosSalvos.setForeground(new Color(34, 139, 34));
		lblMsgDadosSalvos.setBounds(12, 5, 387, 16);
		jpanelBoxInfoUser.add(lblMsgDadosSalvos);
		
		jpanelBoxInfoUser.add(btnSubstituirDigitalUser);
		jpanelBoxInfoUser.add(lblImgDigital);
		jpanelBoxInfoUser.add(lblNivelDeAcesso);
		jpanelBoxInfoUser.add(spinnerNivelAcesso);
		jpanelBoxInfoUser.add(btnSalvarDadosUser);
		jpanelBoxInfoUser.add(lblNome);
		jpanelBoxInfoUser.add(txtNome);
	}
}
