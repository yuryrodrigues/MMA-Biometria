package janelaAcessoDB;

import com.neurotechnology.Nffv.Nffv;

import compartilhada.ScannerNffv;
import compartilhada.Usuario;

public class Principal {
	// objeto que manipulara o scanner e o DB
	private Nffv ffv;
	// objeto com os dados do usuario logado
	private Usuario usuarioLogado;

	public static void main(String[] args) {
		new Principal();
	}

	public Principal() {
		// cria o objeto da classe que manipulara o scanner e o DB
		ffv = ScannerNffv.getNffv();
		
		// exibe a janela com o bando de dados do MMA
		JanelaGUI janelaDB = new JanelaGUI();
		janelaDB.setVisible(true);
		
		// abre a janela para o usuario fazer login
		Principal princ 		= this;
		LoginDBGUI janelaLogin 	= new LoginDBGUI(princ, janelaDB);
		janelaLogin.getJDialog().setVisible(true);
		
		// aguarda o o termino do login
		while(janelaLogin.getJDialog().isShowing()){}
		
		// se o usuario esta logado
		if(usuarioLogado != null){
			// atualiza os dados do usuario na janela do DB
			janelaDB.atualizaDadosUser(usuarioLogado);
			
			// exibe as funções da janela com o bando de dados do MMA
			janelaDB.exibe(true);
		}
		else{
			// fecha a janela do DB; finaliza o programa
			janelaDB.dispose();
		}
	}
	
	protected Usuario getUsuarioLogado() {
		return usuarioLogado;
	}

	protected void setUsuarioLogado(Usuario usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
	}
}
