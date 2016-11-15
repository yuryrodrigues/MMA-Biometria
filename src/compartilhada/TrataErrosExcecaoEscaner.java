package compartilhada;

import java.awt.Window;

import javax.swing.JOptionPane;

import com.neurotechnology.Nffv.NffvStatus;

public class TrataErrosExcecaoEscaner {
	// janela pai do dialogo de progresso
	private Window janelaDono;
	
	public TrataErrosExcecaoEscaner(Window janelaDono){
		this.janelaDono = janelaDono;
	}
	
	// trata os erros do escaner
	public void erro(NffvStatus erro){
		String msgErro = null;
		
		if(erro == NffvStatus.NoScanner){
			msgErro = "Nenhum escaner detectado";
		}
		else if(erro == NffvStatus.ScannerTimeout){
			msgErro = "Tempo limite de leitura da digital esgotado";
		}
		else if(erro == NffvStatus.QualityCheckFailed){
			msgErro = "Falha na checagem da qualidade da digital";
		}
		else if(erro == NffvStatus.None){
			msgErro = "Erro desconhecido";
		}
		
		JOptionPane.showMessageDialog(janelaDono, 
				"Falha na leitura da digital: \n" + msgErro, 
				"Falhou", 
				JOptionPane.ERROR_MESSAGE);
	}
	
	// trata erros genericos
	public void excecao(Exception erro){
		// mostra no console
		erro.printStackTrace();
		
		// mostra para o usuario
		JOptionPane.showMessageDialog(janelaDono, 
				erro, 
				"Erro", 
				JOptionPane.ERROR_MESSAGE);
	}
}
