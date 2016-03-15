package Files;

import Klient.ChatGUI;

import java.io.*;
import java.net.*;

/**
 * Klasa odpowiadaj¹ca za upload pliku
 * @author Pawel
 *
 */
public class Upload implements Runnable
{

	/**
	 * Adres
	 */
    public String addr;
    
    /**
     * Port
     */
    public int port;
    
    /**
     * Socket
     */
    public Socket socket;
    
    /**
     * Stream wejœæiowy
     */
    public FileInputStream In;
    
    /**
     * Stream wyjsciowy
     */
    public OutputStream Out;
    
    /**
     * Plik do przes³ania
     */
    public File file;
    
    /**
     * Okno klienta
     */
    public ChatGUI ui;
    
    /**
     * Konstruktor parametryzowany
     * @param addr - adres serwera
     * @param port - port serwera
     * @param filepath - œcie¿ka pliku wysy³anego
     * @param frame - okno klienta
     */
    public Upload(String addr, int port, File filepath, ChatGUI frame)
    {
        try 
        {
            file = filepath; 
            ui = frame;
            socket = new Socket(InetAddress.getByName(addr), port);
            Out = socket.getOutputStream();
            In = new FileInputStream(filepath);
        } 
        catch (Exception ex) 
        {
            System.out.println("Exception [Upload : Upload(...)]");
        }
    }
    
    /**
     * Czynnoœci wykonywane przez ka¿dy w¹tek
     */
    public void run() 
    {
        try 
        {       
            byte[] buffer = new byte[1024];
            int count;
            
            while((count = In.read(buffer)) >= 0)
            {
                Out.write(buffer, 0, count);
            }
            Out.flush();
            
            ui.jTextArea1.append("[Applcation > Me] : File upload complete\n");
            ui.jButton5.setEnabled(true); 
            ui.jButton6.setEnabled(true);
            ui.jTextField5.setVisible(true);
            
            if(In != null){ In.close(); }
            if(Out != null){ Out.close(); }
            if(socket != null){ socket.close(); }
        }
        catch (Exception ex) 
        {
            System.out.println("Exception [Upload : run()]");
            ex.printStackTrace();
        }
    }

}