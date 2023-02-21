package executaveis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import jogo.Mensagem;

/**
 *
 * @author walmi
 */
public class Servidor extends Thread{
    private static ArrayList<BufferedWriter>clientes; 
    private static ServerSocket server; 
    private String nome; 
    private Socket con; 
    private InputStream in; 
    private InputStreamReader inr; 
    private BufferedReader bfr;

    public Servidor(Socket conexao){
        this.con = conexao;

        try {
            in = con.getInputStream();
            inr = new InputStreamReader(in);
            bfr = new BufferedReader(inr);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
   
    }

    @Override
    public void run(){
        try {
            String mensagem = null;
            OutputStream out = this.con.getOutputStream();
            Writer outWriter = new OutputStreamWriter(out);
            BufferedWriter bufWriter = new BufferedWriter(outWriter);
            clientes.add(bufWriter);
            nome = mensagem = bfr.readLine();

            while(!Mensagem.DESCONECTA.equalsIgnoreCase(mensagem) && mensagem!=null){
                mensagem = bfr.readLine();
                enviaTodos(bufWriter, mensagem);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //Distribuição de mensagens entre os clientes
    private void enviaTodos(BufferedWriter bufWrOut, String msg) {
        BufferedWriter bufferWrOut;
       
        for(BufferedWriter bufW : clientes){
            bufferWrOut = (BufferedWriter) bufW;
            if(!(bufWrOut == bufferWrOut)){
                if((msg.contains(Mensagem.JOGADA_INICIO)&& msg.contains(Mensagem.JOGADA_FINAL)) || 
                        msg.contains(Mensagem.DEFINE_COR_BRANCA) || 
                        (msg.contains(Mensagem.EXCLUI_PECA_INICIO)) && msg.contains(Mensagem.EXCLUI_PECA_FINAL) ||
                        msg.contains(Mensagem.DESISTE_JOGO) || msg.contains(Mensagem.DESCONECTA)){
                    try {
                        bufW.write(msg);
                        bufW.flush();
                        } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    try {
                        bufW.write(nome + ": " + msg + "\r\n"); 
                        bufW.flush();
                    } catch (IOException e) {
                    e.printStackTrace();
                    }
                }   
            }
        }
    }

    public static void main(String[] args) {
        int qtdClientes = 0;
        try {
            server = new ServerSocket(12346);
            clientes = new ArrayList<BufferedWriter>();
            System.out.println("Servidor ativo!");
            
            //aguarda por dois jogadores
            while(qtdClientes<2){
                System.out.println("Aguardando conexão...");
                Socket con = server.accept();
                qtdClientes += 1;
                System.out.println("Cliente conectado...");
                Thread t = new Servidor(con);
                t.start();
            }
        } catch (IOException e) {
                e.printStackTrace();
        }
    }
}
