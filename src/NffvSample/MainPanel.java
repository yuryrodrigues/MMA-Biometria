package NffvSample;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.neurotechnology.Nffv.Nffv;
import com.neurotechnology.Nffv.NffvStatus;
import com.neurotechnology.Nffv.NffvUser;
import com.neurotechnology.Nffv.ScannerModule;

public class MainPanel extends JPanel implements ActionListener, ListSelectionListener{
	
	static final int TIMEOUT = 10000;
	
	JLabel img;
	JList users;
	DefaultListModel usersmodel;
	JButton enroll;
	JButton verify;
	JButton remove;
	JButton clearusers;
	JButton parameters;
	
	Nffv engine;
	NffvUser curruser;
	
	String database;

	public MainPanel(ScannerModule[] scanners, String database, String password){
		System.out.println("Loading scanner modules");
		for (ScannerModule modules : scanners) {	
			System.out.println(modules.getName());
		}	
		
		this.database = database;
		
		
		engine = new Nffv(database, password, scanners);
		
		usersmodel = new DefaultListModel();
		loadIDNames();
		
		users = new JList(usersmodel);
		users.setMinimumSize(new Dimension(150,Integer.MAX_VALUE));
		users.setPreferredSize(new Dimension(150,Integer.MAX_VALUE));
		users.setMaximumSize(new Dimension(150,Integer.MAX_VALUE));
		users.setBorder(new TitledBorder("Users"));
		users.addListSelectionListener(this);
		img = new JLabel();
		
		enroll = new JButton("Enroll");
		verify = new JButton("Verify");
		remove = new JButton("Remove");
		clearusers = new JButton("Clear users");
		parameters = new JButton("Settings");
		
		enroll.addActionListener(this);
		verify.addActionListener(this);
		remove.addActionListener(this);
		clearusers.addActionListener(this);
		parameters.addActionListener(this);
		
		
		JPanel buttons = new JPanel();
		buttons.setLayout(new BoxLayout(buttons,BoxLayout.X_AXIS));
		buttons.add(enroll);
		buttons.add(verify);
		buttons.add(remove);
		buttons.add(clearusers);
		buttons.add(parameters);
		buttons.add(Box.createGlue());
		
		setLayout(new BorderLayout());
		add(img, BorderLayout.CENTER);
		add(users, BorderLayout.EAST);
		add(buttons, BorderLayout.NORTH);
		
		setPreferredSize(new Dimension(500,500));
	}
	
	public void actionPerformed(ActionEvent arg0) {
		
		if(arg0.getSource() == enroll){
			
			String name = JOptionPane.showInputDialog(this,new JLabel("Enter name"),"Enter user name");
			if(name == null) return;
			
			try{
				curruser = engine.enroll(TIMEOUT);
				System.out.println(engine.getEngineStatus());
			
				if(engine.getEngineStatus() != NffvStatus.TemplateCreated){
					JOptionPane.showMessageDialog(this, "Enroll failed - " + engine.getEngineStatus(), "Failed", JOptionPane.ERROR_MESSAGE);
					return;
				}
			
			
				img.setIcon(curruser.getNffvImage().getImageIcon());
				usersmodel.addElement(new IDName(curruser.getID(),name));
				saveIDNames();
			}catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(this, "Enroll failed - " + e.getMessage(), "Failed", JOptionPane.ERROR_MESSAGE);
			}
		}
		
		if(arg0.getSource() == verify){
			IDName selection = (IDName)users.getSelectedValue();
			if(selection == null){
				JOptionPane.showMessageDialog(this,"Please select user to verify with","Error",JOptionPane.ERROR_MESSAGE);
				return;
			}
			NffvUser user = engine.getUserByID(selection.getID());
			int score = engine.verify(user, TIMEOUT);
			if (engine.getEngineStatus() == NffvStatus.TemplateCreated){
				if( score > 0) 
					JOptionPane.showMessageDialog(this,selection.getName() + " verified. \n Fingerprint matching score: " + score,"Verification",JOptionPane.DEFAULT_OPTION);
				else 
					JOptionPane.showMessageDialog(this,selection.getName() + " not verified.\nFingerprints did not match.","Verification failed",JOptionPane.ERROR_MESSAGE);
			}
			else{
				JOptionPane.showMessageDialog(this,"Verification failed - " + engine.getEngineStatus(),"Failed",JOptionPane.ERROR_MESSAGE);
			}
		}
		
		if(arg0.getSource() == remove){
			Object [] selections = users.getSelectedValues();
			for (Object object : selections) {
				engine.removeUserID(((IDName)object).getID());
				usersmodel.removeElement((IDName)object);
				users.updateUI();
				saveIDNames();
			}
		}
		
		if(arg0.getSource() == clearusers){
			engine.clearUsers();
			usersmodel.removeAllElements();
			users.updateUI();
			saveIDNames();
		}
		
		if(arg0.getSource() == parameters) new Parameters(engine);
		
	}
	
	public void loadIDNames(){
		File dbfile = new File(database + ".fdb");
		if(dbfile.exists())
		try{
			FileInputStream fis = new FileInputStream(dbfile);
			ObjectInputStream in = new ObjectInputStream(fis);
			
			for (IDName idname = (IDName)in.readObject(); idname != null; idname = (IDName)in.readObject()){
				System.out.println(idname);
				usersmodel.addElement(idname);
			}
			
			in.close();
		}catch (EOFException eof){}
		catch (Exception e) {
			e.printStackTrace();
		}
			
	}
	
	public void saveIDNames(){
		File dbfile = new File(database + ".fdb");
		try{
			FileOutputStream fos = new FileOutputStream(dbfile);
			ObjectOutputStream out = new ObjectOutputStream(fos);
			
			for (int i = 0; i < usersmodel.getSize(); i++)
				out.writeObject(usersmodel.get(i));
			
			out.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void valueChanged(ListSelectionEvent e){
		IDName selection = (IDName)users.getSelectedValue();
		if (selection == null) {
			img.setIcon(null);
			return;
		}
		curruser = engine.getUserByID(selection.getID());
		try{
			img.setIcon(curruser.getNffvImage().getImageIcon());
		}catch (Exception ex) {
			ex.printStackTrace();
		}	
	}
	
}
