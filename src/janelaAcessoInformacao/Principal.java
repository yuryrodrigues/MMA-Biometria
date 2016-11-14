package janelaAcessoInformacao;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JToolBar;
import java.awt.BorderLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Insets;
import java.awt.Dimension;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JSeparator;
import java.awt.Color;
import javax.swing.JPanel;
import java.awt.Toolkit;

public class Principal {

	private JFrame frmBancoDeDados;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Principal window = new Principal();
					window.frmBancoDeDados.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Principal() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmBancoDeDados = new JFrame();
		frmBancoDeDados.setIconImage(Toolkit.getDefaultToolkit().getImage(Principal.class.getResource("/img/icon-digital-verificada.png")));
		frmBancoDeDados.setTitle("Banco de dados - Ministério da Educação");
		frmBancoDeDados.setBounds(100, 100, 450, 300);
		frmBancoDeDados.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmBancoDeDados.getContentPane().setLayout(null);
		
		JToolBar toolBar = new JToolBar();
		toolBar.setPreferredSize(new Dimension(18, 25));
		toolBar.setMinimumSize(new Dimension(18, 25));
		toolBar.setMaximumSize(new Dimension(18, 25));
		toolBar.setFloatable(false);
		toolBar.setAlignmentY(0.5f);
		toolBar.setBounds(0, 0, 434, 25);
		frmBancoDeDados.getContentPane().add(toolBar);
		
		JButton btnAquivo = new JButton("Aquivo");
		btnAquivo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnAquivo.setPreferredSize(new Dimension(60, 30));
		btnAquivo.setOpaque(false);
		btnAquivo.setMinimumSize(new Dimension(60, 30));
		btnAquivo.setMaximumSize(new Dimension(60, 30));
		btnAquivo.setMargin(new Insets(2, 10, 2, 10));
		btnAquivo.setBorder(null);
		toolBar.add(btnAquivo);
		
		JButton btnEditar = new JButton("Editar");
		btnEditar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnEditar.setSize(new Dimension(55, 30));
		btnEditar.setPreferredSize(new Dimension(55, 30));
		btnEditar.setOpaque(false);
		btnEditar.setMinimumSize(new Dimension(55, 30));
		btnEditar.setMaximumSize(new Dimension(55, 30));
		btnEditar.setMargin(new Insets(2, 10, 2, 10));
		btnEditar.setBorder(null);
		toolBar.add(btnEditar);
		
		JButton btnPesquisar = new JButton("Pesquisar");
		btnPesquisar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
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
		toolBar.add(btnSobre);
		
		JLabel lblNome = new JLabel("Nome:");
		lblNome.setBounds(10, 33, 49, 16);
		frmBancoDeDados.getContentPane().add(lblNome);
		
		JLabel lblNomeUsuario = new JLabel("Yury Rodrigues Anunciação");
		lblNomeUsuario.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblNomeUsuario.setBounds(60, 33, 362, 16);
		frmBancoDeDados.getContentPane().add(lblNomeUsuario);
		
		JLabel lblNivelAcesso = new JLabel("Nível de acesso:");
		lblNivelAcesso.setBounds(10, 54, 101, 16);
		frmBancoDeDados.getContentPane().add(lblNivelAcesso);
		
		JLabel lblNivelAcessoUsuario = new JLabel("2");
		lblNivelAcessoUsuario.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblNivelAcessoUsuario.setBounds(114, 54, 55, 16);
		frmBancoDeDados.getContentPane().add(lblNivelAcessoUsuario);
		
		JLabel lblUtimaAtualizacaoDB = new JLabel("Última atualização no banco de dados:");
		lblUtimaAtualizacaoDB.setBounds(10, 238, 221, 16);
		frmBancoDeDados.getContentPane().add(lblUtimaAtualizacaoDB);
		
		JLabel lblDataUltAtualizacaoDB = new JLabel("10/11/2016");
		lblDataUltAtualizacaoDB.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblDataUltAtualizacaoDB.setBounds(232, 238, 84, 16);
		frmBancoDeDados.getContentPane().add(lblDataUltAtualizacaoDB);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(0, 77, 434, 2);
		frmBancoDeDados.getContentPane().add(separator);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(0, 230, 434, 2);
		frmBancoDeDados.getContentPane().add(separator_1);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(255, 255, 255));
		panel.setBounds(0, 78, 434, 152);
		frmBancoDeDados.getContentPane().add(panel);
		panel.setLayout(null);
		
		JButton btnProprietarios = new JButton("proprietários");
		btnProprietarios.setBounds(11, 70, 130, 38);
		panel.add(btnProprietarios);
		
		JButton btnAgrotoxicos = new JButton("agrotóxicos");
		btnAgrotoxicos.setBounds(152, 70, 130, 38);
		panel.add(btnAgrotoxicos);
		
		JButton btnPropriedades = new JButton("propriedades");
		btnPropriedades.setBounds(293, 70, 130, 38);
		panel.add(btnPropriedades);
		
		JLabel lblMsgNivelAcesso = new JLabel("Você tem permissão para acessar arquivos de nível");
		lblMsgNivelAcesso.setBounds(11, 5, 300, 16);
		panel.add(lblMsgNivelAcesso);
		lblMsgNivelAcesso.setForeground(new Color(0, 128, 0));
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setBounds(0, 27, 434, 2);
		panel.add(separator_2);
		
		JLabel label = new JLabel("2");
		label.setForeground(new Color(0, 128, 0));
		label.setBounds(310, 5, 55, 16);
		panel.add(label);
		btnAgrotoxicos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
	}
}
