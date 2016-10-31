package NffvSample;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class NffvApplication extends JFrame implements PanelContainer{

	public NffvApplication(){
		this.setTitle("Neurotechnology - Nffv Sample");
		this.setIconImage(new ImageIcon(getClass().getResource("/img/logo.png")).getImage());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setPanel(new ScannerModules(this));
	}
	
	public void setPanel(JPanel panel){
		setContentPane(panel);
		this.setSize(panel.getPreferredSize());
		setVisible(true);
	}
	
	public static void main(String[] args) {
		new NffvApplication();
	}

}
