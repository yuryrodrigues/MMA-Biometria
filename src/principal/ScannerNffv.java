package principal;

import com.neurotechnology.Nffv.Nffv;
import com.neurotechnology.Nffv.ScannerModule;

abstract class ScannerNffv {
	static private String bancoDeDados	= "dbUsuarios";
	static private String senhaDB		= "ministerio_da_educacao";
	static private int tipoScanner 		= 25; //25=UareU
	static private Nffv nffv = null;
	
	static private void carrega(){
		// Define o módulo do scanner a ser carregado
		ScannerModule[] scanner = new ScannerModule[1];
		scanner[0] = Nffv.getAvailableScannerModules()[tipoScanner]; 
		
		// cria um objeto da classe que manipulará o scanner e o DB
		System.out.println(bancoDeDados+"-"+senhaDB+"-"+scanner.toString());
		nffv = new Nffv(bancoDeDados, senhaDB, scanner);
	}

	// getters e setters
	static protected Nffv getNffv(){
		// se o sdk ainda não foi carregado
		if(nffv == null){
			carrega();
		}
		return nffv;
	}
	
	static protected String getBancoDeDados() {
		return bancoDeDados;
	}

	static protected String getSenhaDB() {
		return senhaDB;
	}
}
