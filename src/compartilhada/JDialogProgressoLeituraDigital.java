package compartilhada;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.Window;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;

public class JDialogProgressoLeituraDigital {
	// janela de dialogo
	private JDialog jDialogProgressoLeitura;
	// janela pai do dialogo de progresso
	private Window janelaDono;
	
	public JDialogProgressoLeituraDigital(Window janelaDono){
		this.janelaDono = janelaDono;
	}
	
	// exibe uma janela de dialogo informando que esta escaneado a digital
	public void exibe(boolean op){		
		if(jDialogProgressoLeitura == null){
			/* cria a janela de dialogo informando que esta escaneando a digital */
	        jDialogProgressoLeitura = new JDialog(janelaDono, ModalityType.APPLICATION_MODAL);
	        jDialogProgressoLeitura.setIconImage(Toolkit.getDefaultToolkit().getImage(JDialogProgressoLeituraDigital.class.getResource("/img/icon-digital-verificada.png")));
	        jDialogProgressoLeitura.setResizable(false);
	        jDialogProgressoLeitura.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
	        jDialogProgressoLeitura.setBounds(100, 100, 255, 117);
	        jDialogProgressoLeitura.setPreferredSize(new Dimension(255, 117));
	        jDialogProgressoLeitura.getContentPane().setLayout(new BorderLayout());
	        JPanel contentPanel = new JPanel();
	        contentPanel.setBackground(Color.WHITE);
			contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
			jDialogProgressoLeitura.getContentPane().add(contentPanel, BorderLayout.CENTER);
			contentPanel.setLayout(new GridLayout(0, 1, 0, 0));
			
			JLabel lblNewLabel = new JLabel("Escaneando a digital...");
			//lblNewLabel.setBounds(25, 11, 199, 16);
			contentPanel.add(lblNewLabel);
			
			JProgressBar progressBar = new JProgressBar();
			progressBar.setString("0");
			progressBar.setForeground(SystemColor.textHighlight);
			progressBar.setIndeterminate(true);
			//progressBar.setBounds(25, 38, 199, 28);
			contentPanel.add(progressBar);			
			
			jDialogProgressoLeitura.setLocationRelativeTo(janelaDono);
			jDialogProgressoLeitura.pack();
		}
		
		// exibe ou fecha a janela de progresso
		if(op == true){
			jDialogProgressoLeitura.setVisible(true);
		}
		else{
			jDialogProgressoLeitura.dispose();
		}
	}
}
