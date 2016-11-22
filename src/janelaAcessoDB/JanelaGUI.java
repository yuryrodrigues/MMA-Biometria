package janelaAcessoDB;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JToolBar;

import compartilhada.SobreGUI;
import compartilhada.Usuario;

public class JanelaGUI extends JFrame {
	private JLabel lblNomeUsuario;
	private JLabel lblNivelAcessoUsuario;
	private JLabel lblMsgNivelAcessoUser;
	
	/**
	 * Create the application.
	 */
	protected JanelaGUI() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(JanelaGUI.class.getResource("/img/icon-digital-verificada.png")));
		setTitle("Banco de dados - Ministério da Educação");
		setBounds(100, 100, 450, 328);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		setLocationRelativeTo(null);	
		setResizable(false);
		
		// cria os componentes
		setPanel();
		
		// oculta os componentes da janela
		exibe(false);
	}
	
	// oculta os componentes da janela
	protected void exibe(boolean op){
		getContentPane().setVisible(op);
	}
	
	// atualiza a janela com os dados do usuario
	protected void atualizaDadosUser(Usuario usuario){
		lblNomeUsuario.setText(usuario.getNome());
		lblNivelAcessoUsuario.setText(Integer.toString(usuario.getNivelAcesso()));
		lblMsgNivelAcessoUser.setText(Integer.toString(usuario.getNivelAcesso()));
	}
	
	// panel
	protected void setPanel() {
		JToolBar toolBar = new JToolBar();
		toolBar.setPreferredSize(new Dimension(18, 25));
		toolBar.setMinimumSize(new Dimension(18, 25));
		toolBar.setMaximumSize(new Dimension(18, 25));
		toolBar.setFloatable(false);
		toolBar.setAlignmentY(0.5f);
		toolBar.setBounds(0, 0, 444, 25);
		getContentPane().add(toolBar);
		
		JButton btnAquivo = new JButton("Aquivo");
		btnAquivo.setPreferredSize(new Dimension(60, 30));
		btnAquivo.setOpaque(false);
		btnAquivo.setMinimumSize(new Dimension(60, 30));
		btnAquivo.setMaximumSize(new Dimension(60, 30));
		btnAquivo.setMargin(new Insets(2, 10, 2, 10));
		btnAquivo.setBorder(null);
		toolBar.add(btnAquivo);
		
		JButton btnEditar = new JButton("Editar");
		btnEditar.setSize(new Dimension(55, 30));
		btnEditar.setPreferredSize(new Dimension(55, 30));
		btnEditar.setOpaque(false);
		btnEditar.setMinimumSize(new Dimension(55, 30));
		btnEditar.setMaximumSize(new Dimension(55, 30));
		btnEditar.setMargin(new Insets(2, 10, 2, 10));
		btnEditar.setBorder(null);
		toolBar.add(btnEditar);
		
		JButton btnPesquisar = new JButton("Pesquisar");
		btnPesquisar.setPreferredSize(new Dimension(70, 30));
		btnPesquisar.setOpaque(false);
		btnPesquisar.setMinimumSize(new Dimension(70, 30));
		btnPesquisar.setMaximumSize(new Dimension(70, 30));
		btnPesquisar.setMargin(new Insets(2, 10, 2, 10));
		btnPesquisar.setBorder(null);
		toolBar.add(btnPesquisar);
		
		JButton btnExportar = new JButton("Exportar");
		btnExportar.setPreferredSize(new Dimension(70, 30));
		btnExportar.setOpaque(false);
		btnExportar.setMinimumSize(new Dimension(70, 30));
		btnExportar.setMaximumSize(new Dimension(70, 30));
		btnExportar.setMargin(new Insets(2, 10, 2, 10));
		btnExportar.setBorder(null);
		toolBar.add(btnExportar);
		
		JButton btnSobre = new JButton(" Sobre");
		btnSobre.setPreferredSize(new Dimension(50, 30));
		btnSobre.setOpaque(false);
		btnSobre.setMaximumSize(new Dimension(50, 30));
		btnSobre.setMargin(new Insets(2, 10, 2, 10));
		btnSobre.setBorder(null);
		// exibe a janela de Sobre
		JanelaGUI princ = this;
		btnSobre.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JPanel sobre = new SobreGUI();
		        
		        JOptionPane.showOptionDialog(princ, 
					sobre, 
			        "Sobre", 
			        JOptionPane.NO_OPTION, 
			        JOptionPane.PLAIN_MESSAGE, 
			        null, 
			        new String[]{},
			        "default");				
			}
		});
		toolBar.add(btnSobre);
		
		JLabel lblNome = new JLabel("Nome:");
		lblNome.setBounds(10, 35, 49, 16);
		getContentPane().add(lblNome);
		
		lblNomeUsuario = new JLabel("");
		lblNomeUsuario.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblNomeUsuario.setBounds(56, 35, 376, 16);
		getContentPane().add(lblNomeUsuario);
		
		JLabel lblNivelAcesso = new JLabel("Nível de acesso:");
		lblNivelAcesso.setBounds(10, 55, 101, 16);
		getContentPane().add(lblNivelAcesso);
		
		lblNivelAcessoUsuario = new JLabel("");
		lblNivelAcessoUsuario.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblNivelAcessoUsuario.setBounds(110, 55, 55, 16);
		getContentPane().add(lblNivelAcessoUsuario);
		
		JLabel lblUtimaAtualizacaoDB = new JLabel("Última atualização no banco de dados:");
		lblUtimaAtualizacaoDB.setBounds(10, 274, 221, 16);
		getContentPane().add(lblUtimaAtualizacaoDB);
		
		JLabel lblDataUltAtualizacaoDB = new JLabel(new SimpleDateFormat("dd/MM/yy").format(new Date()));
		lblDataUltAtualizacaoDB.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblDataUltAtualizacaoDB.setBounds(232, 274, 84, 16);
		getContentPane().add(lblDataUltAtualizacaoDB);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(0, 81, 444, 2);
		getContentPane().add(separator);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(255, 255, 255));
		panel.setBounds(0, 81, 444, 184);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		JButton btnProprietarios = new JButton("proprietários");
		btnProprietarios.setBounds(13, 85, 130, 38);
		panel.add(btnProprietarios);
		
		JButton btnAgrotoxicos = new JButton("agrotóxicos");
		btnAgrotoxicos.setBounds(156, 85, 130, 38);
		panel.add(btnAgrotoxicos);
		
		JButton btnPropriedades = new JButton("propriedades");
		btnPropriedades.setBounds(299, 85, 130, 38);
		panel.add(btnPropriedades);
		
		JLabel lblMsgNivelAcesso = new JLabel("Você tem permissão para acessar arquivos de nível");
		lblMsgNivelAcesso.setBounds(11, 7, 300, 16);
		panel.add(lblMsgNivelAcesso);
		lblMsgNivelAcesso.setForeground(new Color(0, 128, 0));
		
		lblMsgNivelAcessoUser = new JLabel("");
		lblMsgNivelAcessoUser.setForeground(new Color(0, 128, 0));
		lblMsgNivelAcessoUser.setBounds(310, 7, 55, 16);
		panel.add(lblMsgNivelAcessoUser);
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setBounds(0, 29, 444, 2);
		panel.add(separator_2);	
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(0, 265, 444, 2);
		getContentPane().add(separator_1);
	}
}
