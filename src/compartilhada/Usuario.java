package compartilhada;

import java.io.Serializable;

public class Usuario implements Serializable{

	private int id;
	private String nome;
	private String nomeUsuario;
	private int nivelAcesso;
	
	public Usuario(int id, String nome){
		this.id = id;
		this.nome = nome;
		this.nivelAcesso = 1;
		this.nomeUsuario = "";
	}
	
	public void setID(int iD) {
		this.id = iD;
	}
	public int getID() {
		return id;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getNome() {
		return nome;
	}
	
	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}
	public String getNomeUsuario() {
		return nomeUsuario;
	}
	
	public int getNivelAcesso() {
		return nivelAcesso;
	}
	public void setNivelAcesso(int nivelAcesso) {
		this.nivelAcesso = nivelAcesso;
	}
	
	public String toString(){
		return nome;
	}
}
