package principal;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class SobreGUI extends javax.swing.JPanel {

    public SobreGUI() {
        initComponents();
    }

    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        lblLogoChat = new javax.swing.JLabel();
        lblLogoChat.setBackground(Color.WHITE);

        setPreferredSize(new Dimension(223, 343));
        setLayout(null);

        jLabel2.setFont(new java.awt.Font("Dialog", 1, 13));
        jLabel2.setText("Versão:");
        add(jLabel2);
        jLabel2.setBounds(10, 152, 56, 17);

        jLabel3.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel3.setText("1.3");
        add(jLabel3);
        jLabel3.setBounds(70, 152, 20, 15);

        jLabel4.setFont(new java.awt.Font("Dialog", 1, 13));
        jLabel4.setText("Desenvolvedores:");
        add(jLabel4);
        jLabel4.setBounds(10, 182, 131, 17);

        jLabel6.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel6.setText("Diogo R. L. da Silveira - C173398");
        add(jLabel6);
        jLabel6.setBounds(10, 202, 213, 15);

        jLabel7.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel7.setText("Ricardo F. Souto - C13CH5");
        add(jLabel7);
        jLabel7.setBounds(10, 222, 213, 15);

        jLabel8.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel8.setText("Wanderson M. Oliveira - C202754");
        add(jLabel8);
        jLabel8.setBounds(10, 242, 213, 15);

        jLabel9.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel9.setText("Yury R. Anunciação - C203360");
        add(jLabel9);
        jLabel9.setBounds(10, 262, 213, 15);
        
        
        // redimensiona a logomarca
        ImageIcon imageIcon = new ImageIcon(SobreGUI.class.getResource("/img/logomarca-mma.png"));
        Image image = imageIcon.getImage();
        Image novaImg = image.getScaledInstance(151, 131,  java.awt.Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(novaImg);
        
        lblLogoChat.setIcon(imageIcon);
        lblLogoChat.setAlignmentX(0.5F);
        lblLogoChat.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        add(lblLogoChat);
        lblLogoChat.setBounds(35, 10, 155, 131);
        
        JLabel lblBibliotecaPor = new JLabel("SDK:");
        lblBibliotecaPor.setFont(new Font("Dialog", Font.BOLD, 13));
        lblBibliotecaPor.setBounds(10, 296, 56, 15);
        add(lblBibliotecaPor);
        
        JLabel lblHtmlcleaner = new JLabel("FFV SDK 1.0");
        lblHtmlcleaner.setFont(new Font("Dialog", Font.PLAIN, 12));
        lblHtmlcleaner.setBounds(52, 296, 161, 15);
        add(lblHtmlcleaner);
        
        lblneurotechnology = new JLabel("(Neurotechnology)");
        lblneurotechnology.setFont(new Font("Dialog", Font.PLAIN, 12));
        lblneurotechnology.setBounds(52, 313, 161, 15);
        add(lblneurotechnology);
    }

    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel lblLogoChat;
    private JLabel lblneurotechnology;
}
