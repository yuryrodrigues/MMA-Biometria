package principal;

import com.neurotechnology.Nffv.Nffv;
import com.neurotechnology.Nffv.ScannerModule;

public class ScannerNffv {
	private String bancoDeDados	= "dbUsuarios";
	private String senhaDB		= "ministerio_da_educacao";
	private int tipoScanner 	= 25; //25=UareU
	
	public Nffv carrega(){
		// Define o módulo do scanner a ser carregado
		ScannerModule[] scanner = new ScannerModule[1];
		scanner[0] = Nffv.getAvailableScannerModules()[tipoScanner]; 
		
		// cria um objeto da classe que manipulará o scanner e o DB
		return new Nffv(bancoDeDados, senhaDB, scanner);
	}
}
