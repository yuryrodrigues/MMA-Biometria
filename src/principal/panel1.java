package principal;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Window.Type;
import javax.swing.JProgressBar;
import javax.swing.JLabel;
import java.awt.GridLayout;
import java.awt.SystemColor;
import java.awt.Color;

public class panel1 extends JDialog {

	private final JPanel contentPanel = new JPanel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			panel1 dialog = new panel1();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public panel1() {
		setResizable(false);
		setModal(true);
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 255, 110);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(Color.WHITE);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new GridLayout(0, 1, 0, 0));
		
		JLabel lblNewLabel = new JLabel("Escaneando a digital...");
		contentPanel.add(lblNewLabel);
		
		JProgressBar progressBar = new JProgressBar();
		progressBar.setForeground(SystemColor.textHighlight);
		progressBar.setString("0");
		progressBar.setToolTipText("");
		progressBar.setIndeterminate(true);
		contentPanel.add(progressBar);
	}
}
