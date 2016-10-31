package NffvSample;

import javax.swing.JApplet;
import javax.swing.JPanel;

import com.neurotechnology.Library.NetInstall;

public class NffvApplet extends JApplet implements PanelContainer{
	
	public void init(){
		if (!NetInstall.checkLoadDefault()){
			NetInstall netinstall = null;
			try{
				netinstall = new NetInstall();
			}catch (Exception e) {
				e.printStackTrace();
			}
			if(!netinstall.checkLoadTemp()){
				this.setContentPane(new LibInstallPanel(this));
			}
			else setPanel(new ScannerModules(this));
		}
		else setPanel(new ScannerModules(this));
	}
	
	public void setPanel(JPanel panel){
		setContentPane(panel);
		this.setSize(panel.getPreferredSize());
		setVisible(true);
		System.out.println(panel.getClass() + " loaded");
		this.update(getGraphics());
	}
}
