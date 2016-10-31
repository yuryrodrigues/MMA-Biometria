package NffvSample;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.neurotechnology.Library.NetInstall;
import com.neurotechnology.Library.ScannerFiles;

public class LibInstallPanel extends JPanel implements ActionListener {
	
	JCheckBox [] scanners;
	PanelContainer owner;
	Vector<String> mainlibs;
	Vector<ScannerFiles> scannersf;
    NetInstall netinstall;
    JButton yes;
    JButton no;

	
	public LibInstallPanel(PanelContainer owner){
		this.owner = owner;
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JPanel toptextpan = new JPanel();
		toptextpan.setLayout(new GridLayout(4,1));
		
		toptextpan.add(new JLabel("No Nffv Libraries detected on your system."));
		toptextpan.add(new JLabel());
		
		JPanel buttons = new JPanel();
		buttons.setLayout(new FlowLayout());
		yes = new JButton("Yes");
		no = new JButton("No");
		yes.addActionListener(this);
		no.addActionListener(this);
		buttons.add(yes);
		buttons.add(no);
		
		System.out.println("Os name - " + System.getProperty("os.name"));
		System.out.println("Os arch - " + System.getProperty("os.arch"));
		
		try{
			if (System.getProperty("os.name").indexOf("Windows") != -1){
				
				toptextpan.add(new JLabel("Your operating system was identified as Windows."));
				toptextpan.add(new JLabel("If this is correct, please choose scanners which you are willing to use"));
				
				netinstall = new NetInstall();
				mainlibs = netinstall.getMainLibrariesWindows();
				scannersf = netinstall.getScannerLibrariesWindows();
				scanners = new JCheckBox[scannersf.size()];
				JPanel scannerpanel = new JPanel();
				scannerpanel.setLayout(new GridLayout(7,4));
				for (int i = 0; i < scannersf.size(); i++){
					scanners[i] = new JCheckBox(scannersf.get(i).getName());
					scannerpanel.add(scanners[i]);
				}
				
				this.add(toptextpan);
				this.add(scannerpanel);
			}	
			else {
				System.out.println("Sorry, your operating system is not supported");
				yes.setEnabled(false);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		JPanel bottompan = new JPanel();
		bottompan.setLayout(new GridLayout(2,1));
		bottompan.add(new JLabel("Do you want to install then now"));
		bottompan.add(buttons);
		this.add(bottompan);
		this.add(Box.createGlue());
	}

	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource() == yes){
			this.removeAll();
			this.add(new JLabel("Installing..."));
			this.updateUI();
			(new Thread(){
				public void run(){
					try{
						Vector<ScannerFiles> selectedscanners = new Vector<ScannerFiles>();
						if(scanners != null)
							for (int i = 0; i < scanners.length; i++)
								if (scanners[i].isSelected()) selectedscanners.add(scannersf.get(i));
						netinstall.installTemp(((JApplet)owner).getCodeBase().toString(),mainlibs,selectedscanners);
					}catch (Exception e) {
						e.printStackTrace();
					}
					
					if(!netinstall.checkLoadTemp()){
						System.out.println("Failed to install libraries");
						removeAll();
						add(new JLabel("Failed to install libraries"));
						updateUI();
					}
					else {
						removeAll();
						System.out.println("Libraries installed successfully. Loading application...");
						add(new JLabel("Libraries installed successfully. Loading application..."));
						updateUI();
						owner.setPanel(new ScannerModules(owner));
					}
					System.out.println("Instalation thread closed");
				}
			}).start();
		}
		if(arg0.getSource() == no){
			System.out.println("Instalation canceled");
			removeAll();
			add(new JLabel("No Nffv libraries installed"));
			updateUI();
		}
	}

}
