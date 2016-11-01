package principal;

import java.io.Serializable;

public class Usuario implements Serializable{

	private int id;
	private String nome;
	
	public Usuario(int id, String nome){
		this.id = id;
		this.nome = nome;
	}
	
	public void setID(int iD) {
		id = iD;
	}
	public int getID() {
		return id;
	}
	public void setNome(String nome) {
		nome = nome;
	}
	public String getNome() {
		return nome;
	}
	
	public String toString(){
		return nome;
	}
}
