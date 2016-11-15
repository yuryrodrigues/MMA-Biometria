package janelaCadastroUsuarios;

import java.awt.EventQueue;
import com.neurotechnology.Nffv.Nffv;

import compartilhada.ScannerNffv;

public class Principal {
	// objeto que manipulara o scanner e o DB
	private Nffv ffv;
	
	public static void main(String[] args) {
		new Principal();
	}

	public Principal() {
		// cria o objeto da classe que manipulara o scanner e o DB
		ffv = ScannerNffv.getNffv();
		
		// exibe a janela do gerenciador de usuarios
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
