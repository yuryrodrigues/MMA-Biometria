package janelaCadastroUsuarios;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.neurotechnology.Nffv.Nffv;
import com.neurotechnology.Nffv.NffvStatus;
import com.neurotechnology.Nffv.NffvUser;

import compartilhada.JDialogProgressoLeituraDigital;
import compartilhada.ScannerNffv;
import compartilhada.SobreGUI;
import compartilhada.TrataErrosExcecaoEscaner;
import compartilhada.Usuario;

public class JanelaCtrl implements ActionListener, ListSelectionListener {	
	// JanelaGUI e o sdk para manipular o scanner
	private JanelaGUI janelaDono;
	private Nffv ffv;
	
	// janela de progresso da leitura da digital
	private JDialogProgressoLeituraDigital jDialogProgressoLeituraDigital;
	// trata os erros e excecoes do escaner
	private TrataErrosExcecaoEscaner trataErrosExcecaoEscaner;
	
	// lista de usuarios cadastrados
	DefaultListModel listaUsuarios;;
	
	// lista com os nomes de usuarios dos usuarios
	ArrayList listaNomesUsuarios;
	
	// janelas de dialogo
	JDialog jDialogProgressoLeitura;
	
	// mensagens no box do usuario
	ExecutorService cachedPoolMensagens;
	Future<?> runFutureMensagem;
	
	public JanelaCtrl(JanelaGUI janela){
		// o endereco da JanelaGUI
		janelaDono = janela;
		
		// lê a lista de usuário cadastrados no banco de dados
		carregaListaUsuarios();
		
		// busca o objeto da classe que manipulará o scanner e o DB
		ffv = ScannerNffv.getNffv();
		
		// janela de progresso da leitura da digital
		jDialogProgressoLeituraDigital = new JDialogProgressoLeituraDigital(janelaDono);
		// trata os erros e excecoes do escaner
		trataErrosExcecaoEscaner = new TrataErrosExcecaoEscaner(janelaDono);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// se o usuario pediu para cadastrar um novo usuário
		if(arg0.getSource() == janelaDono.btnCadastrar){
			cadastrarUser();
		}		
		else if(arg0.getSource() == janelaDono.btnVerificar){
			verificarUser();
		}		
		else if(arg0.getSource() == janelaDono.btnRemover){
			removerUser();
		}		
		else if(arg0.getSource() == janelaDono.btnRemoverTodos){
			removerTodosUsuarios();
		}
		else if(arg0.getSource() == janelaDono.btnSobre){
			JPanel sobre = new SobreGUI();
	        
	        JOptionPane.showOptionDialog(janelaDono, 
				sobre, 
		        "Sobre", 
		        JOptionPane.NO_OPTION, 
		        JOptionPane.PLAIN_MESSAGE, 
		        null, 
		        new String[]{},
		        "default");
		}
		else if(arg0.getSource() == janelaDono.btnSalvarDadosUser){
			atualizaDadosUsuarioSelecionado();
		}
		else if(arg0.getSource() == janelaDono.btnSubstituirDigitalUser){
			// pega o usuario selecionado na lista
			Usuario usuarioSelecionado = (Usuario)janelaDono.listaUser.getSelectedValue();
			
			// se o usuario selecionado for o administrador
			if(((Usuario)usuarioSelecionado).getNome().equals(ScannerNffv.getAdminDB())){
				substituiDigitalAdmin();
			}
			else{
				substituirDigitalUsuario();
			}
		}
		
		// atualiza o box com os dados do usuário
		atualizaBoxUser();
	}
	
	// exibe ou oculta mensagens no box com informações do usuário
	private void exibeMensagemBoxUser(){
		// cria o "conteiner" que contera a thread, caso não exista ainda
		if(cachedPoolMensagens == null){
			cachedPoolMensagens = Executors.newCachedThreadPool();
		}
		
		// derruba a contagem anterior, senao ira ocultar a mensagem antes da hora
		if(runFutureMensagem != null && !runFutureMensagem.isDone()){
			runFutureMensagem.cancel(true);
		}
				
		// exibe a mensagem
		janelaDono.lblMsgDadosSalvos.setVisible(true);
		
		// oculta a mensagem apos alguns segundos de exibicao
		Runnable runMensagem = new Runnable(){
            @Override
            public void run() {
            	try {
					TimeUnit.SECONDS.sleep(3);
					// oculta a mensagem
					janelaDono.lblMsgDadosSalvos.setVisible(false);
				} catch (InterruptedException e) {}				
            }
		};
		
		// cronometra e oculta a mensagem
		runFutureMensagem = cachedPoolMensagens.submit(runMensagem);
	}
	
	// atualiza os dados do usuario selecionado
	private void atualizaDadosUsuarioSelecionado(){
		// pega o usuario selecionado na lista
		Usuario usuarioSelecionado = (Usuario)janelaDono.listaUser.getSelectedValue();
		
		// se o nome do usuario ou nome de usuario estiver vazio
		if(janelaDono.txtNome.getText().trim().length() == 0 || janelaDono.txtNomeUsuario.getText().trim().length() == 0){
			JOptionPane.showMessageDialog(janelaDono,
					"Preencha os campos Nome e Usuário!",
					"",
					JOptionPane.WARNING_MESSAGE);
			return;			
		}
		
		// se o nome de usuario digitado for diferente do anterior e ele ja estiver em uso
		if(!usuarioSelecionado.getNomeUsuario().equals(janelaDono.txtNomeUsuario.getText()) && listaNomesUsuarios.contains(janelaDono.txtNomeUsuario.getText())){
			JOptionPane.showMessageDialog(janelaDono,
					"O nome de usuário digitado já estar sendo utilizado.",
					"",
					JOptionPane.WARNING_MESSAGE);
			return;	
		}
		
		// atualiza a lista com os nomes de usuarios dos usuarios
		listaNomesUsuarios.remove(usuarioSelecionado.getNomeUsuario());
		listaNomesUsuarios.add(janelaDono.txtNomeUsuario.getText());
		
		// altera os dados do usuário selecionado
		usuarioSelecionado.setNome(janelaDono.txtNome.getText());
		usuarioSelecionado.setNomeUsuario(janelaDono.txtNomeUsuario.getText());
		usuarioSelecionado.setNivelAcesso((int)janelaDono.spinnerNivelAcesso.getValue());
		
		// atualiza a lista de usuários na janela
		atualizaListaUserJanela();
		
		// salva a nova lista de usuários
		salvarUsuarios();
				
		// informa que os dados foram salvos com sucesso
		janelaDono.lblMsgDadosSalvos.setText("Dados salvos com sucesso!");		
		exibeMensagemBoxUser();
	}
	
	// tempo limite para aguarda a substituição da digital do administrador
	// utiliza o mesmo conteiner das thread das mensagens
	// nao tem problema, pois se uma mensagem sera exibida é porque a janela de 
	// substituicao de digital do admin esta fechada
	// entao nao tem problema mantar esta cronometragem :)
	private void defineTempoSubsDigitalAdmin(JDialog adminSubsDigital){
		// cria o "conteiner" que contera a thread, caso não exista ainda
		if(cachedPoolMensagens == null){
			cachedPoolMensagens = Executors.newCachedThreadPool();
		}
		
		// derruba a contagem anterior, senao ira ocultar a mensagem antes da hora
		if(runFutureMensagem != null && !runFutureMensagem.isDone()){
			runFutureMensagem.cancel(true);
		}
		
		// oculta a mensagem apos alguns segundos de exibicao
		Runnable runMensagem = new Runnable(){
            @Override
            public void run() {
            	try {
					TimeUnit.SECONDS.sleep(60);
					// fecha e abre novamente a janela de substituicao de digital
					// pedindo para verificar a identidade novamente
					adminSubsDigital.dispose();
					substituiDigitalAdmin();
				} catch (InterruptedException e) {}				
            }
		};
		
		// cronometra e redefine a janela de substituicao da digital do admin
		runFutureMensagem = cachedPoolMensagens.submit(runMensagem);
	}
	
	// substitui a digital do admin
	private void substituiDigitalAdmin(){
		/* informa que precisa confirmar a identidade do admin */		
		// cria a janela de dialogo
		JDialog jDialog = new JDialog(janelaDono, true);		
		jDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		jDialog.setResizable(false);
		jDialog.setTitle("Banco de Dados - Ministério do Meio Ambiente");
		jDialog.setIconImage(Toolkit.getDefaultToolkit().getImage(LoginAdminGUI.class.getResource("/img/icon-digital-verificada.png")));
		jDialog.setBounds(100, 100, 286, 138);	
		jDialog.setLocationRelativeTo(janelaDono);
		
		jDialog.getContentPane().setLayout(new BorderLayout());			
		JPanel contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		jDialog.getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblMsgInfo = new JLabel("É preciso verificar se você é o Administrador:");
		lblMsgInfo.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblMsgInfo.setBounds(12, 15, 256, 16);
		contentPanel.add(lblMsgInfo);
		
		JButton btnEscanearDigital = new JButton("escanear digital");
		btnEscanearDigital.setActionCommand("verificar");
		btnEscanearDigital.setBounds(11, 51, 257, 33);
		btnEscanearDigital.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getActionCommand().equals("verificar")){
					/* confirma se a identidade do admin */
					// pega o o admin selecionado na lista
					Usuario adminSelecionado = (Usuario)listaUsuarios.getElementAt(0);
					
					// busca o usuário administrador do bd					
					NffvUser adminUsuario = ffv.getUserByID(((Usuario)adminSelecionado).getID());
					
					// verifica se é o verdadeiro administrador
					int usuarioValidado = confirmaUsuario(adminUsuario);
					if(usuarioValidado == 1){
						// modifica o texto da janela
						lblMsgInfo.setText("Identidade verificada. Escanei a nova digital:");
						lblMsgInfo.setForeground(new Color(34, 139, 34));
						
						// indica que a proximo clique é para cadastrar a digital
						btnEscanearDigital.setActionCommand("cadastrar");
						
						// fecha a janela apos 1minuto de inatividade
						defineTempoSubsDigitalAdmin(jDialog);
					}
					else if(usuarioValidado == 0){
						// icone da janela
						ImageIcon imageIcon = new ImageIcon(SobreGUI.class.getResource("/img/icon-digital-nao-verificada.png"));
				        Image image 		= imageIcon.getImage();
				        Image novaImg 		= image.getScaledInstance(75, 63,  java.awt.Image.SCALE_SMOOTH);
				        imageIcon 			= new ImageIcon(novaImg);
				        
						JOptionPane.showMessageDialog(janelaDono,
								"As impressões digitais não são compativeis.",
								"Falha na verificação",
								JOptionPane.ERROR_MESSAGE,
								imageIcon);
					}
				}
				else if(e.getActionCommand().equals("cadastrar")){
					// substitue a digital do admin
					substituirDigitalUsuario();
					
					// fecha a janela
					jDialog.dispose();
				}
			}
		});
		contentPanel.add(btnEscanearDigital);
		
		jDialog.setVisible(true);
		/***********************************************/
	}
	
	// substitui a digital do usuario
	private void substituirDigitalUsuario(){		
		try{
			// executa a leitura da digital no plano de fundo
	        SwingWorker<NffvUser,Void> worker = new SwingWorker<NffvUser,Void>(){
	            @Override
	            protected NffvUser doInBackground(){
	            	// le a digital do usuario
	                return ffv.enroll(ScannerNffv.TIMEOUT);
	            }
	         
	            @Override
	            protected void done(){
	            	// fecha a janela de progresso
	            	jDialogProgressoLeituraDigital.exibe(false);
	            }
	        };
	        worker.execute();
	        
	        // exibe a janela de progresso
	        jDialogProgressoLeituraDigital.exibe(true);
	        
	        // retorna a digital lida
	        NffvUser novoUsuario = worker.get();
			
			// se não conseguiu ler a digital
			if(ffv.getEngineStatus() != NffvStatus.TemplateCreated){
				trataErrosExcecaoEscaner.erro(ffv.getEngineStatus());
				return;
			}
			
			// pega o usuario selecionado na lista
			Usuario usuarioSelecionado = (Usuario)janelaDono.listaUser.getSelectedValue();
			
			// remove o usuario do banco de digitais
			ffv.removeUserID(usuarioSelecionado.getID());
		
			// link o usuario do DB com o novo usuario do banco de digitais
			usuarioSelecionado.setID(novoUsuario.getID());			
			
			// salva a alteração de ID do usuario do DB
			salvarUsuarios();
			
			// informa que os dados foram salvos com sucesso
			janelaDono.lblMsgDadosSalvos.setText("Digital substituida!");
			exibeMensagemBoxUser();			
		}catch (Exception e) {
			trataErrosExcecaoEscaner.excecao(e);
		}
		
		// seleciona o usuario substituido na lista de usuarios
		janelaDono.listaUser.setSelectedIndex(janelaDono.listaUser.getSelectedIndex());
	}
	
	// remove todos os usuáros do DB
	private void removerTodosUsuarios(){
		// pergunta se deseja remover todo os usuarios
		Object[] options = { "SIM", "NÃO" };	
		int desejaRemover = JOptionPane.showOptionDialog(janelaDono,
			    "Deseja remover todos os usuários cadastrados?",
			    "",
			    JOptionPane.YES_NO_OPTION,
			    JOptionPane.WARNING_MESSAGE,
			    null,
			    options,
			    options[1]);
		
		// caso não deseje, não faz nada
		if(desejaRemover == 1) return;
		
		// remove os usuarios da lista do DB 
		/*ffv.clearUsers();
		listaUsuarios.removeAllElements();*/
		for(int i=(listaUsuarios.size()-1); i>=0; i--) {
			// se o usuario selecionado for o administrador
			if(((Usuario)listaUsuarios.get(i)).getNome().equals(ScannerNffv.getAdminDB())){
				// pula o usuario admin
				continue;
			}
			
			// remove o usuário do banco de usuarios e do banco de digitais
			ffv.removeUserID(((Usuario)listaUsuarios.get(i)).getID());
			listaUsuarios.removeElement((Usuario)listaUsuarios.get(i));
		}
		
		// atualiza a lista de usuários na janela
		atualizaListaUserJanela();
		
		// salva a nova lista de usuárioss
		salvarUsuarios();
	}
	
	// remove usuario(s)
	private void removerUser(){
		// pega a lista de usuáros selecionados
		Object[] usuariosSelecionados = janelaDono.listaUser.getSelectedValues();
		
		// se não foi selecionado nenhum usuário
		if(usuariosSelecionados.length == 0){
			JOptionPane.showMessageDialog(janelaDono,
					"Selecione o usuário que deseja remover",
					"",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		// pergunta se deseja remover o usuario		
		Object[] options = { "SIM", "NÃO" };	
		int desejaRemover = JOptionPane.showOptionDialog(janelaDono,
			    "Deseja remover este(s) usuário(s)?",
			    "",
			    JOptionPane.YES_NO_OPTION,
			    JOptionPane.WARNING_MESSAGE,
			    null,
			    options,
			    options[1]);
		
		// caso não deseje, não faz nada
		if(desejaRemover == 1) return;
		
		for (Object usuario : usuariosSelecionados) {
			// se o usuario selecionado for o administrador
			if(((Usuario)usuario).getNome().equals(ScannerNffv.getAdminDB())){
				// pula o usuario admin
				continue;
			}
			
			// remove o usuário do banco de usuarios e do banco de digitais
			ffv.removeUserID(((Usuario)usuario).getID());
			listaUsuarios.removeElement((Usuario)usuario);
			
			// atualiza a lista de usuários na janela
			atualizaListaUserJanela();
			
			// salva a nova lista de usuárioss
			salvarUsuarios();
		}
		
		// seleciona o primeiro usuário na lista de usuarios
		janelaDono.listaUser.setSelectedIndex(janelaDono.listaUser.getFirstVisibleIndex());
	}
	
	// verifica um usuario/checa sua digital
	private void verificarUser(){
		// pega o usuario selecionado para verificao
		Usuario usuarioSelecionado = (Usuario)janelaDono.listaUser.getSelectedValue();
		
		// se não foi selecionado nenhum usuário
		if(usuarioSelecionado == null){
			JOptionPane.showMessageDialog(janelaDono,
					"Selecione o usuário que deseja verificar",
					"",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		// busca o usuário selecionado no banco de dado
		NffvUser usuarioDB = ffv.getUserByID(usuarioSelecionado.getID());
		
		// se as digitais são compativeis
		int usuarioValidado = confirmaUsuario(usuarioDB);
		if(usuarioValidado == 1){
			// icone da janela
			ImageIcon imageIcon = new ImageIcon(SobreGUI.class.getResource("/img/icon-digital-verificada.png"));
	        Image image = imageIcon.getImage();
	        Image novaImg = image.getScaledInstance(85, 74,  java.awt.Image.SCALE_SMOOTH);
	        imageIcon = new ImageIcon(novaImg);
			
			JOptionPane.showMessageDialog(janelaDono,
					usuarioSelecionado.getNome() + " foi verificado(a). \nAs impressões digitais são compativeis.",
					"Verificado",
					JOptionPane.DEFAULT_OPTION,
					imageIcon);
		}
		else if(usuarioValidado == 0){
			// icone da janela
			ImageIcon imageIcon = new ImageIcon(SobreGUI.class.getResource("/img/icon-digital-nao-verificada.png"));
	        Image image = imageIcon.getImage();
	        Image novaImg = image.getScaledInstance(85, 74,  java.awt.Image.SCALE_SMOOTH);
	        imageIcon = new ImageIcon(novaImg);
	        
			JOptionPane.showMessageDialog(janelaDono,
					usuarioSelecionado.getNome() + " não foi verificado(a).\nAs impressões digitais não são compativeis.",
					"Falha na verificação",
					JOptionPane.ERROR_MESSAGE,
					imageIcon);
		}		
	}
	
	// verifica um usuario/checa sua digital
	/**
	 *@return 0 = nao verificado ; 1 = verificado ; -1 = erro na verificao
	 */
	protected int confirmaUsuario(NffvUser usuario){
		// executa a leitura da digital no plano de fundo
        SwingWorker<Integer,Void> worker = new SwingWorker<Integer,Void>(){
            @Override
            protected Integer doInBackground(){
            	// le a digital do usuario
                return ffv.verify(usuario, ScannerNffv.TIMEOUT);
            }
         
            @Override
            protected void done(){
            	// fecha a janela de progresso
            	jDialogProgressoLeituraDigital.exibe(false);
            }
        };
        worker.execute();
        
        // exibe a janela de progresso
        jDialogProgressoLeituraDigital.exibe(true);
        
        // retorna a digital lida
        int compatibilidadeUsuario = 0;
		try {
			compatibilidadeUsuario = worker.get();
		} catch (InterruptedException | ExecutionException e) {
			trataErrosExcecaoEscaner.excecao(e);
		}
		
		// se conseguiu escanear a digital
		if (ffv.getEngineStatus() == NffvStatus.TemplateCreated){
			// se as digitais são compativeis
			if(compatibilidadeUsuario > 0){
				return 1;
			}
			else{
				return 0;
			}
		}
		else{
			trataErrosExcecaoEscaner.erro(ffv.getEngineStatus());
			return -1;
		}
	}
	
	// cadastra um usuario no banco de dados
	private void cadastrarUser(){
		// verifica se já atingiu o limite de usuários permitidos
		// nao pode usar o contagem de ffv; pois aparece mais usuarios do que realmente existem
		if(listaUsuarios.size() >= ScannerNffv.QT_MAX_USER){
			// informa que já atingiu o limite
			JOptionPane.showMessageDialog(janelaDono,
					"Só é permitido o cadastro de 9 usuários :(",
					"",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		// pega o nome do novo usuário
		String nomeUsuario = JOptionPane.showInputDialog(janelaDono, 
				new JLabel("Digite o nome do usuário"), 
				"Cadastrar usuário", 
				JOptionPane.QUESTION_MESSAGE);
		if(nomeUsuario == null) return;
		
		// se o nome do usuario estiver vazio
		if(nomeUsuario.trim().length()==0){
			// abre uma nova janela de cadastro
			cadastrarUser();
			return;
		}
		
		// se o nome de usuario digitado for igual ao do administrador		
		if(nomeUsuario.equals(ScannerNffv.getAdminDB())){
			// informa que já atingiu o limite
			JOptionPane.showMessageDialog(janelaDono,
					"O nome do usuário é inválido!",
					"",
					JOptionPane.ERROR_MESSAGE);
			cadastrarUser();
			return;
		}
		
		// cria e salva o novo usuario
		criaUsuario(nomeUsuario);
		
		// seleciona o usuario cadastrado na lista de usuarios
		janelaDono.listaUser.setSelectedIndex(janelaDono.listaUser.getLastVisibleIndex());
	}
	
	// cria e salva um novo usuario
	protected void criaUsuario(String nomeUsuario){
		try{
			// executa a leitura da digital no plano de fundo
	        SwingWorker<NffvUser,Void> worker = new SwingWorker<NffvUser,Void>(){
	            @Override
	            protected NffvUser doInBackground(){
	            	// le a digital do usuario
	                return ffv.enroll(ScannerNffv.TIMEOUT);
	            }
	         
	            @Override
	            protected void done(){
	            	// fecha a janela de progresso
	            	jDialogProgressoLeituraDigital.exibe(false);
	            }
	        };
	        worker.execute();
	        
	        // exibe a janela de progresso
	        jDialogProgressoLeituraDigital.exibe(true);
	        
	        // retorna a digital lida
	        NffvUser novoUsuario = worker.get();
			
			// se não conseguiu ler a digital
			if(ffv.getEngineStatus() != NffvStatus.TemplateCreated){
				trataErrosExcecaoEscaner.erro(ffv.getEngineStatus());
				return;
			}
			
			// adiciona o usuario na lista de usuarios cadastrados
			listaUsuarios.addElement(new Usuario(novoUsuario.getID(),nomeUsuario));
			
			// salva o usuario no DB
			salvarUsuarios();
		}catch (Exception e) {			
			trataErrosExcecaoEscaner.excecao(e);
		}
	}
	
	// salva a lista de usuarios atualizada no banco de dados
	private void salvarUsuarios(){
		// define o arquivo com o banco de dados das digitais
		File arquivoDB = new File(ScannerNffv.getBancoDeDados() + ".fdb");
		try{
			// abre o arquivo para leitura e escrita
			FileOutputStream arquivoOut = new FileOutputStream(arquivoDB);
			ObjectOutputStream arquivo 	= new ObjectOutputStream(arquivoOut);
			
			// salva a lista de usuarios atualizada no banco de dados
			for (int i = 0; i < listaUsuarios.getSize(); i++){
				arquivo.writeObject(listaUsuarios.get(i));
			}
			
			arquivo.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// lê a lista de usuário cadastrados no banco de dados
	protected void carregaListaUsuarios(){
		// cria a lista que armazenara os usuarios cadastrados
		listaUsuarios = new DefaultListModel();	
		// cria a lista que aramzenara os nomes de usuarios dos usuarios
		listaNomesUsuarios = new ArrayList();
		
		// define o arquivo com o banco de dados das digitais
		File arquivoDB = new File(ScannerNffv.getBancoDeDados() + ".fdb");
		
		// verifica se o arquivo existe
		if(arquivoDB.exists()){
			try{
				// abre o arquivo para leitura e escrita
				FileInputStream arquivoIn 	= new FileInputStream(arquivoDB);
				ObjectInputStream arquivo 	= new ObjectInputStream(arquivoIn);
				
				// le os usuarios do arquivo de banco de dados
				for (Usuario usuario = (Usuario)arquivo.readObject(); usuario != null; usuario = (Usuario)arquivo.readObject()){
					// adiciona o usuario a lista de usuarios
					listaUsuarios.addElement(usuario);
					// cria uma lista com os nomes de usuarios
					listaNomesUsuarios.add(usuario.getNomeUsuario());
				}
				
				arquivo.close();
			}catch (EOFException eof){}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	// adiciona a lista de usuarios cadastrados na janela
	protected void mostraListaUsuarios(){	
		// adiciona o modelo com a lista de usuários na janela
		janelaDono.listaUser.setModel(listaUsuarios);
		
		// seleciona o primeiro usuário na lista de usuarios
		janelaDono.listaUser.setSelectedIndex(0);
		
		// atualiza o box com os dados do usuário
		atualizaBoxUser();
	}
		
	// atualiza a lista de usuários na janela
	private void atualizaListaUserJanela(){
		// atualiza a lista de usuários na janela
		janelaDono.listaUser.updateUI();
	}
	
	// atualiza o box com os dados do usuário
	private void atualizaBoxUser(){
		// pega o usuario selecionado na lista
		Usuario usuarioSelecionado = (Usuario)janelaDono.listaUser.getSelectedValue();
		
		// caso não tenha nenhum usuário selecionado, limpa o box de informações do usuário
		if(usuarioSelecionado == null){
			janelaDono.txtNome.setText("");
			janelaDono.txtNomeUsuario.setText("");
			janelaDono.spinnerNivelAcesso.setValue(1);
			janelaDono.lblImgDigital.setIcon(null);
			janelaDono.txtNome.setEnabled(false);
			janelaDono.txtNomeUsuario.setEnabled(false);
			janelaDono.spinnerNivelAcesso.setEnabled(false);
			janelaDono.btnSalvarDadosUser.setEnabled(false);
			janelaDono.btnSubstituirDigitalUser.setEnabled(false);
			janelaDono.btnRemover.setEnabled(false);
			janelaDono.btnVerificar.setEnabled(false);
			return;
		}
		
		// busca os dados do usuario selecionado no DB
		NffvUser usuario = ffv.getUserByID(usuarioSelecionado.getID());
		
		// atualiza o box com as informações do usuário
		janelaDono.txtNome.setText(usuarioSelecionado.getNome());		
		try {
			janelaDono.lblImgDigital.setIcon(usuario.getNffvImage().getImageIcon());
		} catch (Exception e) {e.printStackTrace();}		
		janelaDono.btnVerificar.setEnabled(true);
		janelaDono.txtNome.setEnabled(true);
		janelaDono.btnSubstituirDigitalUser.setEnabled(true);
		
		// se o usuario selecionado for o administrador
		if(usuarioSelecionado.getNome().equals(ScannerNffv.getAdminDB())){
			janelaDono.txtNomeUsuario.setText("");
			janelaDono.spinnerNivelAcesso.setValue(1);
			janelaDono.txtNomeUsuario.setEnabled(false);
			janelaDono.spinnerNivelAcesso.setEnabled(false);
			janelaDono.btnSalvarDadosUser.setEnabled(false);
			janelaDono.btnRemover.setEnabled(false);			
		}
		else{
			janelaDono.txtNomeUsuario.setText(usuarioSelecionado.getNomeUsuario());
			janelaDono.spinnerNivelAcesso.setValue(usuarioSelecionado.getNivelAcesso());			
			janelaDono.txtNomeUsuario.setEnabled(true);
			janelaDono.spinnerNivelAcesso.setEnabled(true);
			janelaDono.btnSalvarDadosUser.setEnabled(true);	
			janelaDono.btnRemover.setEnabled(true);
		}
		
		// se existir algum usuario na lista(desconsiderando o admin)
		if((listaUsuarios.size()-1) > 0){
			janelaDono.btnRemoverTodos.setEnabled(true);
		}
		else{
			janelaDono.btnRemoverTodos.setEnabled(false);
		}
	}

	// exibe as informações do usuário selecionado
	@Override
	public void valueChanged(ListSelectionEvent e) {
		atualizaBoxUser();
	}
}
