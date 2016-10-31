package NffvSample;

import java.io.Serializable;

public class IDName implements Serializable{

	private int ID;
	private String Name;
	
	public IDName(int ID, String Name){
		this.ID = ID;
		this.Name = Name;
	}
	
	public void setID(int iD) {
		ID = iD;
	}
	public int getID() {
		return ID;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getName() {
		return Name;
	}
	
	public String toString(){
		return Name;
	}
}
