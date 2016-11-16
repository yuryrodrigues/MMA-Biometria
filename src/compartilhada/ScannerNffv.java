package compartilhada;

import javax.swing.JOptionPane;

import com.neurotechnology.Nffv.Nffv;
import com.neurotechnology.Nffv.ScannerModule;

public abstract class ScannerNffv {
	static private String bancoDeDados	= "dbUsuarios";
	static private String senhaDB		= "ministerio_da_educacao";
	static private String adminDB 		= "adminMMA";
	static private int tipoScanner 		= 25; //25=UareU
	// tempo maximo de tentativa de leitura da digital
	static public final int TIMEOUT 	= 10000;
	// numero maximo de usuarios permitidos
	// 2 usuarios sao reservados: 1 para substituicao de digital
	// e 1 para administrar o banco de dados
	static public final int QT_MAX_USER = 8;
	// objeto que manipulara o scanner e o DB
	static private Nffv nffv 			= null;
	
	static private void carrega(){
		// Define o modulo do scanner a ser carregado
		ScannerModule[] scanner = new ScannerModule[1];
		scanner[0] = Nffv.getAvailableScannerModules()[tipoScanner]; 
		
		// cria um objeto da classe que manipulara o scanner e o DB
		try{
			nffv = new Nffv(bancoDeDados, senhaDB, scanner);
		}
		catch(Exception e){
			// caso ja exista uma janela do DB em execuação
			if(e.getMessage().equals("Win32 error has occured")){
				System.out.println(e.getMessage());
				
				// informa que já existe uma janela do DB em execuação
				JOptionPane.showMessageDialog(null,
						"Já existe uma janela do banco de dados em execução!",
						"",
						JOptionPane.ERROR_MESSAGE);
				
				// finaliza o programa
				System.exit(0);
			}			
		}
	}

	// getters e setters
	static public Nffv getNffv(){
		// se o sdk ainda nao foi carregado
		if(nffv == null){
			carrega();
		}
		return nffv;
	}
	
	static public String getBancoDeDados() {
		return bancoDeDados;
	}

	static public String getSenhaDB() {
		return senhaDB;
	}
	
	static public String getAdminDB() {
		return adminDB;
	}
}
