package Klient;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import Files.Download;
import Files.Upload;
import Message.Message;

/**
 * Klasa socketu klienta
 * @author Pawel
 *
 */
public class SocketClient implements Runnable
{
    /**
     * Port do którego bêdziemy siê ³¹czyæ
     */
    public int port;
    
    /**
     * Adres serwera do którego bêdziemy siê ³¹czyæ
     */
    public String serverAddr;
    
    /**
     * Socket klienta
     */
    public Socket socket;
    
    /**
     * Okno klienta
     */
    public ChatGUI ui;
    
    /**
     * Stream wejœæiowy
     */
    public ObjectInputStream In;
    
    /**
     * Stream wyjœciowy
     */
    public ObjectOutputStream Out;
    
    /**
     * String
     */
    public String roomName = "[Room ";
    
    /**
     * Na potrzeby testów. Przechowuje ostatni¹ wiadomoœæ ywcodz¹c¹ do serwera
     */
    public Message Outgoing;
    
    /**
     * Na potrzeby testów. Przechowuje ostatni¹ wiadomoœæ od serwera
     */
    public Message Incoming; 

    /**
     * Konstruktor tworz¹cy socket i otwieraj¹cy stream
     * @param frame
     * @throws IOException
     */
    public SocketClient(ChatGUI frame) throws IOException
    {
        ui = frame; 
        this.serverAddr = ui.serverAddr; 
        this.port = ui.port;
        socket = new Socket("localhost", port);
            
        Out = new ObjectOutputStream(socket.getOutputStream());
        Out.flush();
        In = new ObjectInputStream(socket.getInputStream());
        
    }

    /**
     * G³ówna metoda w¹tku, nas³uchuje wiadomoœci od serwera
     */
    @SuppressWarnings({ "unchecked", "deprecation" })
	public void run() 
    {
        boolean keepRunning = true;
        while(keepRunning)
        {
            try 
            {
                Message msg = (Message) In.readObject();
                System.out.println("Incoming : " + msg.toString());
                Incoming = msg;
            switch(msg.type)
            {
            	/**
            	 * Wyœwietla wiadomoœæ w oknie klienta
            	 */
            	case "message":
                    if(msg.recipient.equals(ui.username))
                    {
                        ui.jTextArea1.append("["+msg.sender +" > Me] : " + msg.content + "\n");
                    }
                    else
                    {
                        ui.jTextArea1.append("["+ msg.sender +" > "+ msg.recipient +"]: " + msg.content + "\n");
                    }
                    break;                        
                /**
                 * Wyœwietla wiadomoœæ do pokoju w oknie klienta
                 */
            	case "message_room":
            		ui.jTextArea1.append("["+msg.sender +" > " + msg.recipient + "] : " + msg.content + "\n");
            		break;
            	
            	/**
            	 * Wyœwietla rezultat próby loginu
            	 */
            	case "login":
                    if(msg.content.equals("TRUE"))
                    {
                        ui.jButton2.setEnabled(false); 
                        ui.jButton3.setEnabled(false);                        
                        ui.jButton5.setEnabled(true);
                        ui.jButton7.setEnabled(true);
                        ui.jButton8.setEnabled(true);
                        ui.jTextArea1.append("[SERVER > Me] : Login Successful\n");
                        ui.jTextField3.setEnabled(false); 
                        ui.jPasswordField1.setEnabled(false);
                    }
                    else
                    {
                        ui.jTextArea1.append("[SERVER > Me] : Login Failed\n");
                    }
                    break;
                    
                /**
                 * Testowe po³¹czenie
                 */
            	case "test":
                    ui.jButton1.setEnabled(false);
                    ui.jButton2.setEnabled(true); 
                    ui.jButton3.setEnabled(true);
                    ui.jTextField3.setEnabled(true); 
                    ui.jPasswordField1.setEnabled(true);
                    ui.jTextField1.setEditable(false); 
                    ui.jTextField2.setEditable(false);
                    break;
                    
                /**
                 * Wyœwietla infromacje o do³¹czniu nowego usera
                 */
            	case "newuser": // dodanie do listy uzytkowników
                    if(!msg.content.equals(ui.username))
                    {
                        boolean exists = false;
                        for(int i = 0; i < ui.model.getSize(); i++)
                        {
                            if(ui.model.getElementAt(i).equals(msg.content))
                            {
                                exists = true; 
                                break;
                            }
                        }
                        if(!exists){ ui.model.addElement(msg.content); }
                    }
                    break;
                    
                /**
                 * Rezultat rejestracji
                 */
            	case "signup":
                    if(msg.content.equals("TRUE"))
                    {
                        ui.jButton2.setEnabled(false); 
                        ui.jButton3.setEnabled(false); 
                        ui.jButton5.setEnabled(true);
                        ui.jTextArea1.append("[SERVER > Me] : Singup Successful\n");
                    }
                    else
                    {
                        ui.jTextArea1.append("[SERVER > Me] : Signup Failed\n");
                    }
                    break;
                    
                /**
                 * Wylogowanie
                 */
            	case "signout":
                    if(msg.content.equals(ui.username))
                    {
                        ui.jTextArea1.append("["+ msg.sender +" > Me] : Bye\n");
                        ui.jButton1.setEnabled(true); 
                        ui.jTextField1.setEditable(true); 
                        ui.jTextField2.setEditable(true);
                        
                        for(int i = 1; i < ui.model.size(); i++)
                        {
                            ui.model.removeElementAt(i);
                        }
                        
                        ui.clientThread.stop();
                    }
                    else
                    {
                        ui.model.removeElement(msg.content);
                        ui.jTextArea1.append("["+ msg.sender +" > All] : "+ msg.content +" has signed out\n");
                    }
                    break;
                    
                /**
                 * Propozycja przes³ania pliku od innego u¿ytkownika
                 */
            	case "upload_req":
                    if(JOptionPane.showConfirmDialog(ui, ("Accept '"+msg.content+"' from "+msg.sender+" ?")) == 0)
                    {
                        JFileChooser jf = new JFileChooser();
                        jf.setSelectedFile(new File(msg.content));
                        int returnVal = jf.showSaveDialog(ui);
                       
                        String saveTo = jf.getSelectedFile().getPath();
                        if(saveTo != null && returnVal == JFileChooser.APPROVE_OPTION)
                        {
                            Download dwn = new Download(saveTo, ui);
                            Thread t = new Thread(dwn);
                            t.start();
                            send(new Message("upload_res", ui.username, (""+dwn.port), msg.sender));
                        }
                        else
                        {
                            send(new Message("upload_res", ui.username, "NO", msg.sender));
                        }
                    }
                    else
                    {
                        send(new Message("upload_res", ui.username, "NO", msg.sender));
                    }
                    break;
                    
                /**
                 * 
                 */
            	case "upload_res":
                    if(!msg.content.equals("NO"))
                    {
                        int port  = Integer.parseInt(msg.content);
                        String addr = msg.sender;
                        
                        ui.jButton5.setEnabled(false); 
                        ui.jButton6.setEnabled(false);
                        Upload upl = new Upload(addr, port, ui.file, ui);
                        Thread t = new Thread(upl);
                        t.start();
                    }
                    else
                    {
                        ui.jTextArea1.append("[SERVER > Me] : "+msg.sender+" rejected file request\n");
                    }
                    break;
                    
                /**
                 * Wyœwietla propozycjê do³¹czenia do pokoju
                 */
            	case "invite":
                    if(JOptionPane.showConfirmDialog(ui, ("Accept invitation to "+ msg.room.roomName + " from " + msg.sender + "?")) == 0)
                    {
                        send(new Message("invitation_res", ui.username, msg.recipient, msg.sender, msg.room)); 
                        ui.model.addElement(msg.room.roomName);
                    }
                    else
                    {
                        send(new Message("invitation_res", ui.username, msg.recipient + " didn't ", msg.sender, msg.room));
                    }
                    break;
                    
                /**
                 * OdpowiedŸ na propozycjê
                 */
            	case "invitation_res":
                	// jesli zaakceptowal
                    if(!msg.content.equals(msg.sender))
                    {
                    	ui.jTextArea1.append("[SERVER > Me]: "+ msg.content +" accepted\n");
                        
                    }
                    else
                    {
                    	ui.jTextArea1.append("[SERVER > Me]: "+ msg.content + "accepted\n");
                    }
                    break;
                    
                /**
                 * Kasowanie pokoju z listy online
                 */
            	case "delete_room":
                    ui.model.removeElement("[Room " + msg.sender + "]");
                    if(msg.sender.equals(ui.username))
                    	ui.jButton8.setEnabled(true);
            		break;
            		
            	/**
            	 * Default
            	 */
                default:
                    ui.jTextArea1.append("[SERVER > Me] : Unknown message type\n");

            }
            }        
            catch(Exception ex) 
            {
                keepRunning = false;
                ui.jTextArea1.append("[Application > Me] : Connection Failure\n");
                ui.jButton1.setEnabled(true); 
                ui.jTextField1.setEditable(true); 
                ui.jTextField2.setEditable(true);
                ui.jButton5.setEnabled(false); 
                ui.jButton5.setEnabled(false);
                
                for(int i = 1; i < ui.model.size(); i++)
                {
                    ui.model.removeElementAt(i);
                }
                
                ui.clientThread.stop();
                
                System.out.println("Exception SocketClient run()");
                ex.printStackTrace();
            }
        }
    }
    
    /**
     * Wysy³a wiadomoœc do serwera
     * @param msg - wiadomosc
     */
    public void send(Message msg)
    {
        try 
        {
            Out.writeObject(msg);
            Out.flush();
            System.out.println("Outgoing : " + msg.toString());
            Outgoing = msg;
        } 
        catch (IOException ex) 
        {
            System.out.println("Exception SocketClient send()");
        }
    }
    
    
    /**
     * Zamyka w¹tek
     * @param t - referencja w¹tku
     */
    public void closeThread(Thread t)
    {
        t = null;
    }
}
