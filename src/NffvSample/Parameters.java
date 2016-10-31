package NffvSample;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.neurotechnology.Nffv.Nffv;

public class Parameters extends JFrame implements ActionListener {

	JButton ok;
	JButton cancel;
	
	JTextField matcingthreshold;
	JTextField qualitythreshold;
	
	Nffv nffv;
	
	Parameters(Nffv nffv){
		
		this.nffv = nffv;
		this.setTitle("Settings");
		this.setIconImage(new ImageIcon(getClass().getResource("/img/logo.png")).getImage());
		this.setSize(new Dimension(370,120));
		
		JPanel quality = new JPanel();
		JPanel threshold = new JPanel();
		JPanel lab = new JPanel();
		JPanel buttons = new JPanel();
		JPanel mainpan = new JPanel();
		
		mainpan.setLayout(new BoxLayout(mainpan, BoxLayout.Y_AXIS));
		
		quality.setLayout(new GridLayout(1,2));
		threshold.setLayout(new GridLayout(1,2));
		lab.setLayout(new GridLayout(1,1));
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
		
		ok = new JButton("Ok");
		cancel = new JButton("Cancel");
		
		ok.addActionListener(this);
		cancel.addActionListener(this);
		
		buttons.add(Box.createGlue());
		buttons.add(ok);
		buttons.add(cancel);
		
		matcingthreshold = new JTextField(new Integer(nffv.getMatchingThreshold()).toString());
		qualitythreshold = new JTextField(new Integer(nffv.getQualityThreshold()).toString());
		
		quality.add(new JLabel("Quality threshold"));
		quality.add(qualitythreshold);
		threshold.add(new JLabel("Matching threshold"));
		threshold.add(matcingthreshold);
		lab.add(new JLabel("Quality threshold: 0 - 99: low, 100-199: medium, 200 - 255 high."));
		
		mainpan.add(lab);
		mainpan.add(quality);
		mainpan.add(threshold);
		mainpan.add(buttons);
		
		this.setContentPane(mainpan);
		
		this.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == cancel) this.dispose();
		if(e.getSource() == ok){
			try{
				int mt = new Integer(matcingthreshold.getText());
				int qt = new Integer(qualitythreshold.getText());
				
				if(qt < 0 || qt > 255) throw new Exception("Quality threshold range 0 - 255");
				if(mt < 0) throw new Exception("Matching threshold must be greater then 0");
				
				nffv.setMatchingThreshold(mt);
				nffv.setQualityThreshold(qt);
				
				this.dispose();
			}catch (Exception ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(this, "Operation failed - " + ex.getMessage(), "Failed", JOptionPane.ERROR_MESSAGE);
			}
		}

	}

}
