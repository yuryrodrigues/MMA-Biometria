package NffvSample;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class NameBinding {
	
	private HashMap idnames;
	private File dbfile;
	
	public NameBinding(String dbname)throws Exception{
		FileInputStream fis = null;
		ObjectInputStream in = null;
		
		dbfile = new File(dbname);
		if(dbfile.exists()){
			fis = new FileInputStream(dbfile);
			in = new ObjectInputStream(fis);
			idnames = (HashMap)in.readObject();
			in.close();
		}
		else idnames = new HashMap();
	}
	
	public String getIDName(String id){
		return (String)idnames.get(id);
	}
	
	public void add(String id, String idname){
		try{
			idnames.put(id, idname);
			dbfile.delete();
			FileOutputStream fos = new FileOutputStream(dbfile);
			ObjectOutputStream out = new ObjectOutputStream(fos);
			out.writeObject(idnames);
			out.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void remove(String id){
		try{
			idnames.remove(id);
			dbfile.delete();
			FileOutputStream fos = new FileOutputStream(dbfile);
			ObjectOutputStream out = new ObjectOutputStream(fos);
			out.writeObject(idnames);
			out.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
