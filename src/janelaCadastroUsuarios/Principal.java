package janelaCadastroUsuarios;

import com.neurotechnology.Nffv.Nffv;

import compartilhada.ScannerNffv;

public class Principal {
	// objeto que manipulara o scanner e o DB
	private Nffv ffv;	
	// objeto com os dados do usuario logado
	private boolean adminLogado;
	
	public static void main(String[] args) {
		new Principal();
	}

	public Principal() {
		// cria o objeto da classe que manipulara o scanner e o DB
		ffv = ScannerNffv.getNffv();
		
		// exibe a janela de cadastro de usuarios do bando de dados do MMA
		JanelaGUI janelaCadastro = new JanelaGUI();
		janelaCadastro.setVisible(true);
		
		// abre a janela para o administrador fazer login
		Principal princ 			= this;
		LoginAdminGUI janelaLogin 	= new LoginAdminGUI(princ, janelaCadastro);
		janelaLogin.getJDialog().setVisible(true);
		
		// aguarda o o termino do login
		while(janelaLogin.getJDialog().isShowing()){}
		
		// se o adminstrador esta logado
		if(adminLogado == true){			
			// exibe as funções da janela de cadastro de usuarios
			janelaCadastro.exibe(true);
		}
		else{
			// fecha a janela de cadastro de usuarios; finaliza o programa
			janelaCadastro.dispose();
		}
	}
	
	protected boolean getAdminLogado() {
		return adminLogado;
	}

	protected void setAdminLogado(boolean op) {
		this.adminLogado = op;
	}
}
