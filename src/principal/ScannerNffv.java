package principal;

import com.neurotechnology.Nffv.Nffv;
import com.neurotechnology.Nffv.ScannerModule;

public abstract class ScannerNffv {
	static private String bancoDeDados	= "dbUsuarios";
	static private String senhaDB		= "ministerio_da_educacao";
	static private int tipoScanner 		= 25; //25=UareU
	// tempo maximo de tentativa de leitura da digital
	static public final int TIMEOUT 	= 10000;
	// objeto que manipulara o scanner e o DB
	static private Nffv nffv 			= null;
	
	static private void carrega(){
		// Define o modulo do scanner a ser carregado
		ScannerModule[] scanner = new ScannerModule[1];
		scanner[0] = Nffv.getAvailableScannerModules()[tipoScanner]; 
		
		// cria um objeto da classe que manipulara o scanner e o DB
		nffv = new Nffv(bancoDeDados, senhaDB, scanner);
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
}
