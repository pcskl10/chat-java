package Files;

import Klient.ChatGUI;

import java.io.*;
import java.net.*;

/**
 * Klasa odpowiadaj�ca za download plkiu
 * @author Pawel
 *
 */
public class Download implements Runnable
{
    /**
     * Referencja socketa
     */
    public ServerSocket server;
    
    /**
     * Socket
     */
    public Socket socket;
    
    /**
     * Port na kt�rym nas�uchuje
     */
    public int port;
    
    /**
     * �cie�ka zapisu
     */
    public String saveTo = "";
    
    /**
     * Stream wej�ciowy
     */
    public InputStream In;
    
    /**
     * Stream wyj�ciowy
     */
    public FileOutputStream Out;
    
    /**
     * Okno klienta
     */
    public ChatGUI ui;
    
    /**
     * Konstruktor parametryzowany
     * @param saveTo - �cie�ka zapisu
     * @param ui - okno klienta
     */
    public Download(String saveTo, ChatGUI ui)
    {
        try 
        {
            server = new ServerSocket(0);
            port = server.getLocalPort();
            this.saveTo = saveTo;
            this.ui = ui;
        } 
        catch (IOException ex) 
        {
            System.out.println("Exception [Download : Download(...)]");
        }
    }

    /**
     * Czynno�ci wykonywane przez ka�dy w�tek
     */
    public void run() 
    {
        try 
        {
            socket = server.accept();
            System.out.println("Download : "+socket.getRemoteSocketAddress());
            
            In = socket.getInputStream();
            Out = new FileOutputStream(saveTo);
            
            byte[] buffer = new byte[1024];
            int count;
            
            while((count = In.read(buffer)) >= 0)
            {
                Out.write(buffer, 0, count);
            }
            
            Out.flush();
            
            ui.jTextArea1.append("[Application > Me] : Download complete\n");
            
            if(Out != null){ Out.close(); }
            if(In != null){ In.close(); }
            if(socket != null){ socket.close(); }
        } 
        catch (Exception ex) 
        {
            System.out.println("Exception [Download : run(...)]");
        }
    }
}