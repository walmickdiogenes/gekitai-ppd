package executaveis;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import jogo.Mensagem;
import jogo.Jogadas;
import jogo.Tabuleiro;
import tela.JogoGekitai;

/**
 *
 * @author walmi
 */
public class Cliente extends JFrame implements KeyListener, ActionListener, MouseListener, WindowListener{
    
    private Socket socketCl;
    private OutputStream out;
    private Writer outWr; 
    private BufferedWriter bufWr;
    private JTextField txtNome;
    public JogoGekitai tabuleiro;
    private JTextField txtIP;
    private JTextField txtPorta;
    int flagJogadaEncerrada = 0;
    String jogadaIni = "";
    String jogadaFin = "";
    String iniUltimaJog = "";
    String finUltimaJog = "";
    Tabuleiro tab = new Tabuleiro();
    String corPecas = "";
    String corPecasOutro = "";
    Jogadas jogadas = new Jogadas();
    int pecasAdversario = 8;
    
    public Cliente() throws IOException{
        JLabel lblMessage = new JLabel("Digite seu nome");
        txtNome = new JTextField("User");
        JLabel lblHost = new JLabel("Digite o IP do servidor");
        txtIP = new JTextField("127.0.0.1");
        JLabel lblPorta = new JLabel("Digite a porta do servidor");
        txtPorta = new JTextField("12346");
        Object[] texts = {lblMessage, txtNome, lblHost, txtIP, lblPorta, txtPorta};
        JOptionPane.showMessageDialog(null, texts);
        
        this.tabuleiro = new JogoGekitai();
        this.tabuleiro.setVisible(true);

        iniciaListenerButtons();
        iniciaCasas();
        tab.iniciarTabuleiro(tabuleiro);
        tab.desabilitaBotoes(tabuleiro);
        }
    
    void estabeleceConexao(){
        try {
            socketCl = new Socket(txtIP.getText(),Integer.parseInt(txtPorta.getText()));
            
            out = socketCl.getOutputStream();
            outWr = new OutputStreamWriter(out);
            bufWr = new BufferedWriter(outWr);
            bufWr.write(txtNome.getText() + "\r\n");
            bufWr.flush();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    void iniciaListenerButtons(){
        java.util.List<JButton> botoes = Arrays.asList(tabuleiro.getEnviarButton(), tabuleiro.getIniciarJogoButton(), tabuleiro.getDesistirJogoButton());
        
        for (JButton botao : botoes) {
            botao.addActionListener(this);
            botao.addKeyListener(this);
        }
  
        tabuleiro.getMensagemTextField().addKeyListener(this);
        tabuleiro.addWindowListener(this);
    }
    
    void iniciaCasas(){
        java.util.List<JLabel> casas = Arrays.asList(tabuleiro.getA1Label(), tabuleiro.getA2Label(), tabuleiro.getA3Label(), tabuleiro.getA4Label(), tabuleiro.getA5Label(), tabuleiro.getA6Label(),
                tabuleiro.getB1Label(), tabuleiro.getB2Label(), tabuleiro.getB3Label(), tabuleiro.getB4Label(), tabuleiro.getB5Label(), tabuleiro.getB6Label(),
                tabuleiro.getC1Label(), tabuleiro.getC2Label(), tabuleiro.getC3Label(), tabuleiro.getC4Label(), tabuleiro.getC5Label(), tabuleiro.getC6Label(),
                tabuleiro.getD1Label(), tabuleiro.getD2Label(), tabuleiro.getD3Label(), tabuleiro.getD4Label(), tabuleiro.getD5Label(), tabuleiro.getD6Label(),
                tabuleiro.getE1Label(), tabuleiro.getE2Label(), tabuleiro.getE3Label(), tabuleiro.getE4Label(), tabuleiro.getE5Label(), tabuleiro.getE6Label(),
                tabuleiro.getF1Label(), tabuleiro.getF2Label(), tabuleiro.getF3Label(), tabuleiro.getF4Label(), tabuleiro.getF5Label(), tabuleiro.getF6Label(),
                
                tabuleiro.getP1Label(), tabuleiro.getP2Label(), tabuleiro.getP3Label(),
                tabuleiro.getP4Label(), tabuleiro.getP5Label(), tabuleiro.getP6Label(),
                tabuleiro.getP7Label(), tabuleiro.getP8Label());
        
        java.util.List<String> nomeLabel =  Arrays.asList("a1Label", "a2Label", "a3Label", "a4Label", "a5Label", "a6Label", 
                "b1Label", "b2Label", "b3Label", "b4Label", "b5Label", "b6Label",
                "c1Label", "c2Label", "c3Label", "c4Label", "c5Label", "c6Label",
                "d1Label", "d2Label", "d3Label", "d4Label", "d5Label", "d6Label",
                "e1Label", "e2Label", "e3Label", "e4Label", "e5Label", "e6Label",
                "f1Label", "f2Label", "f3Label", "f4Label", "f5Label", "f6Label",
                
                "p1Label", "p2Label", "p3Label", "p4Label", "p5Label", "p6Label", "p7Label", "p8Label");
        
        for(int i = 0; i < casas.size(); i++){
            casas.get(i).addMouseListener(this);
            casas.get(i).setName(nomeLabel.get(i));
        }
    }

    void desabilitaListeners(){
        java.util.List<JLabel> pecas = Arrays.asList(tabuleiro.getA1Label(), tabuleiro.getA2Label(), tabuleiro.getA3Label(), tabuleiro.getA4Label(), tabuleiro.getA5Label(), tabuleiro.getA6Label(),
                tabuleiro.getB1Label(), tabuleiro.getB2Label(), tabuleiro.getB3Label(), tabuleiro.getB4Label(), tabuleiro.getB5Label(), tabuleiro.getB6Label(),
                tabuleiro.getC1Label(), tabuleiro.getC2Label(), tabuleiro.getC3Label(), tabuleiro.getC4Label(), tabuleiro.getC5Label(), tabuleiro.getC6Label(),
                tabuleiro.getD1Label(), tabuleiro.getD2Label(), tabuleiro.getD3Label(), tabuleiro.getD4Label(), tabuleiro.getD5Label(), tabuleiro.getD6Label(),
                tabuleiro.getE1Label(), tabuleiro.getE2Label(), tabuleiro.getE3Label(), tabuleiro.getE4Label(), tabuleiro.getE5Label(), tabuleiro.getE6Label(),
                tabuleiro.getF1Label(), tabuleiro.getF2Label(), tabuleiro.getF3Label(), tabuleiro.getF4Label(), tabuleiro.getF5Label(), tabuleiro.getF6Label(),
                
                tabuleiro.getP1Label(), tabuleiro.getP2Label(), tabuleiro.getP3Label(),
                tabuleiro.getP4Label(), tabuleiro.getP5Label(), tabuleiro.getP6Label(),
                tabuleiro.getP7Label(), tabuleiro.getP8Label());
         
         for(JLabel casa : pecas){
             casa.removeMouseListener(this);
         }
    }
    
    void habilitaListenersLabel(){
        java.util.List<JLabel> pecas = Arrays.asList(tabuleiro.getA1Label(), tabuleiro.getA2Label(), tabuleiro.getA3Label(), tabuleiro.getA4Label(), tabuleiro.getA5Label(), tabuleiro.getA6Label(),
                tabuleiro.getB1Label(), tabuleiro.getB2Label(), tabuleiro.getB3Label(), tabuleiro.getB4Label(), tabuleiro.getB5Label(), tabuleiro.getB6Label(),
                tabuleiro.getC1Label(), tabuleiro.getC2Label(), tabuleiro.getC3Label(), tabuleiro.getC4Label(), tabuleiro.getC5Label(), tabuleiro.getC6Label(),
                tabuleiro.getD1Label(), tabuleiro.getD2Label(), tabuleiro.getD3Label(), tabuleiro.getD4Label(), tabuleiro.getD5Label(), tabuleiro.getD6Label(),
                tabuleiro.getE1Label(), tabuleiro.getE2Label(), tabuleiro.getE3Label(), tabuleiro.getE4Label(), tabuleiro.getE5Label(), tabuleiro.getE6Label(),
                tabuleiro.getF1Label(), tabuleiro.getF2Label(), tabuleiro.getF3Label(), tabuleiro.getF4Label(), tabuleiro.getF5Label(), tabuleiro.getF6Label(),
                
                tabuleiro.getP1Label(), tabuleiro.getP2Label(), tabuleiro.getP3Label(),
                tabuleiro.getP4Label(), tabuleiro.getP5Label(), tabuleiro.getP6Label(),
                tabuleiro.getP7Label(), tabuleiro.getP8Label());
         
         for(JLabel casa : pecas){
             casa.addMouseListener(this);
         }
    }

    void definirCorInicial(String cor){
        String corSec =  Mensagem.DEFINE_COR_BRANCA;
        tab.iniciarPecas(tabuleiro, cor);
        try {
            enviarMensagem(corSec + "\r\n");
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    	
    public void enviarMensagem(String msg) throws IOException{
        
        if((msg.contains(Mensagem.JOGADA_INICIO)&& msg.contains(Mensagem.JOGADA_FINAL)) || 
                        msg.contains(Mensagem.DEFINE_COR_BRANCA) ||
                        msg.contains(Mensagem.DESISTE_JOGO) || msg.contains(Mensagem.DESCONECTA)){
            bufWr.write(msg + "\r\n");
        }
        else{
           bufWr.write(msg + "\r\n");
            tabuleiro.conversaTextArea.append(txtNome.getText() + ": " + tabuleiro.getMensagemTextField().getText() + "\r\n"); 
        }
        
        bufWr.flush(); 
        tabuleiro.getMensagemTextField().setText("");
    }

    void escutarPorta() throws IOException{
        InputStream in = socketCl.getInputStream();
        InputStreamReader inReader = new InputStreamReader(in);
        BufferedReader bufRead = new BufferedReader(inReader);
        String mensagem = "";
             
        while(!Mensagem.DESCONECTA.equalsIgnoreCase(mensagem)){
            if(bufRead.ready()){
                mensagem = bufRead.readLine();
                //O adversário fechou a janela.
                if(mensagem.contains(Mensagem.SAIR_JANELA)){
                    tabuleiro.getStatusJogoLabel().setText("Seu adversário saiu da sala.\r\n");
                    desabilitaListeners();
                    tab.desabilitaBotoes(tabuleiro);
                    tabuleiro.getIniciarJogoButton().setEnabled(false);
                }
                else if(bufRead.ready()){
                    mensagem = bufRead.readLine();
                    //Jogada realizada
                    if(mensagem.contains(Mensagem.JOGADA_INICIO) && mensagem.contains(Mensagem.JOGADA_FINAL)){
                        iniUltimaJog = mensagem.substring(Mensagem.JOGADA_INICIO.length(), Mensagem.JOGADA_INICIO.length()+7);
                        finUltimaJog = mensagem.substring(Mensagem.JOGADA_FINAL.length()+7, Mensagem.JOGADA_FINAL.length()+14);

                        tab.atualizarTabuleiro(tabuleiro, corPecasOutro, iniUltimaJog, finUltimaJog, 200);  

                        jogadas.retomarVez(tabuleiro);
                        habilitaListenersLabel();
                    
                }
                //Informando as cores das pedras
                else if(mensagem.contains(Mensagem.DEFINE_COR_BRANCA)){
                    corPecas = "branca";
                    corPecasOutro = "preta";
                    
                    tab.iniciarPecas(tabuleiro, corPecas);
                    tab.desabilitaBotoes(tabuleiro);
                    desabilitaListeners();
                    tabuleiro.getIniciarJogoButton().setEnabled(false);
                    tabuleiro.getStatusJogoLabel().setText("Seu adversário iniciará o jogo.");
                }
                //
                else if(mensagem.contains(Mensagem.EXCLUI_PECA_INICIO) && mensagem.contains(Mensagem.EXCLUI_PECA_FINAL)){
                    String casa = mensagem.substring(Mensagem.EXCLUI_PECA_INICIO.length(), Mensagem.EXCLUI_PECA_INICIO.length()+7);
                    
                    tab.retirarPeca(tabuleiro, casa, 200);
                    tabuleiro.getStatusJogoLabel().setText("Você perdeu uma peça!");
                    jogadas.retomarVez(tabuleiro);
                    tab.setQtdPecas(tab.getQtdPecas()-1);
                    habilitaListenersLabel();
                }
                //O adversário desistiu do jogo.
                else if(mensagem.contains(Mensagem.DESISTE_JOGO)){
                    tabuleiro.getStatusJogoLabel().setText("O seu adversário desistiu.");
                    desabilitaListeners();
                }
                //O adversário enviou uma mensagem no chat.
                else 
                    tabuleiro.conversaTextArea.append(mensagem + "\r\n");
                }
            }
        }
    }
        
    public void desconectarChat() throws IOException{
            enviarMensagem(Mensagem.SAIR_JANELA);
            bufWr.close();
            outWr.close();
            out.close();
            socketCl.close();
    }

    public static void main(String[] args) throws IOException {
            // TODO Auto-generated method stub
            Cliente app1 = new Cliente();
            
            app1.estabeleceConexao();
            app1.escutarPorta();
    }


    @Override
    public void keyPressed(KeyEvent arg0) {
        //Tecla enter: envia mensagem pelo chat.
        if(arg0.getKeyCode() == KeyEvent.VK_ENTER){
                try {
                    if(!tabuleiro.getMensagemTextField().getText().equals("")){
                        enviarMensagem(tabuleiro.getMensagemTextField().getText());
                    }  
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
        }
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        
        try {
            //Botão enviar: envia mensagem pelo chat.
            if(arg0.getActionCommand().equals(tabuleiro.getEnviarButton().getActionCommand())){
                if(!tabuleiro.getMensagemTextField().getText().equals("")){
                        enviarMensagem(tabuleiro.getMensagemTextField().getText());
                    }
            }
            //Botão iniciar: inicia o jogo no tabuleiro, define a cor das peças.
            else if(arg0.getActionCommand().equals(tabuleiro.getIniciarJogoButton().getActionCommand())){
                corPecas = "preta";
                corPecasOutro = "branca";
                
                definirCorInicial(corPecas);
                tabuleiro.getIniciarJogoButton().setEnabled(false);
                tab.habilitaBotoes(tabuleiro);
                tabuleiro.getStatusJogoLabel().setText("Você iniciará o jogo.");
                
            }
            //Botão desistir: desiste do jogo.
            else if(arg0.getActionCommand().equals(tabuleiro.getDesistirJogoButton().getActionCommand())){
                int resposta;
                String msgDesistirJogo = Mensagem.DESISTE_JOGO + "\r\n";
                resposta = JOptionPane.showConfirmDialog(null, "Tem certeza que quer desistir?");
                
                if(resposta == JOptionPane.OK_OPTION){
                    tabuleiro.getStatusJogoLabel().setText("Você desistiu.");
                    tab.desabilitaBotoes(tabuleiro);
                    desabilitaListeners();
                    enviarMensagem(msgDesistirJogo);
                }       
                else{
                    tabuleiro.getStatusJogoLabel().setText("Faça sua jogada.");
                }           
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //Seleção da peça a ser movida.
        if(flagJogadaEncerrada == 0){
            jogadaIni = jogadas.verificarValidadePeca(tabuleiro, (JLabel) e.getComponent(), corPecas, tab.getCasas(), 1);
            if(!jogadaIni.equals(""))
               flagJogadaEncerrada = 1; 
            }  
        //Seleção da casa para onde a peça será movida.
        else if (flagJogadaEncerrada == 1){
            jogadaFin = jogadas.verificarValidadePeca(tabuleiro, (JLabel) e.getComponent(), corPecas, tab.getCasas(), 2);
            if(!jogadaFin.equals("")){
               flagJogadaEncerrada = 0;
                             
               try {
                   String jogada = Mensagem.JOGADA_INICIO + jogadaIni + jogadaFin + Mensagem.JOGADA_FINAL + "\r\n";
                   tab.atualizarTabuleiro(tabuleiro, this.corPecas, jogadaIni, jogadaFin, 100);
                   tab.setQtdPecasJogadas(tab.getQtdPecasJogadas() + 1);
                   jogadas.passarVez(tabuleiro);
                   desabilitaListeners();
                   enviarMensagem(jogada);

                } catch (IOException ex) {
                    Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                    }  
                }
        }
    }
    
    @Override
    public void windowClosing(WindowEvent e) {
        try {
            desconectarChat();
            System.exit(0);
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void windowClosed(WindowEvent e) { }
    
    @Override
    public void keyReleased(KeyEvent arg0) { }

    @Override
    public void keyTyped(KeyEvent arg0) { }

    @Override
    public void mousePressed(MouseEvent e) { }
  
    @Override
    public void mouseReleased(MouseEvent e) { }
    
    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }

    @Override
    public void windowOpened(WindowEvent e) { }

    @Override
    public void windowIconified(WindowEvent e) {  }

    @Override
    public void windowDeiconified(WindowEvent e) { }

    @Override
    public void windowActivated(WindowEvent e) { }

    @Override
    public void windowDeactivated(WindowEvent e) { }
}