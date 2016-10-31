package principal;

import java.awt.EventQueue;
import com.neurotechnology.Nffv.Nffv;

public class Principal {
	// objeto que manipulará o scanner e o DB
	private Nffv ffv;
	
	public static void main(String[] args) {
		new Principal();
	}

	public Principal() {
		// objeto da classe que manipulará o scanner e o DB
		ffv = new ScannerNffv().carrega();
		
		// exibe a janela do gerenciador de usuários
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JanelaGUI window = new JanelaGUI();
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
