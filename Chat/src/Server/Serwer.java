package Server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

import Message.Message;
import Message.Room;

/**
 * Klasa w�tku klienta
 * @author Pawel
 *
 */
class ServerThread extends Thread 
{ 
	/**
	 * Referencja do obiektu serwera
	 */
    public Serwer server = null;
    
    /**
     * Socket
     */
    public Socket socket = null;
    
    /**
     * Indywidualny numer ka�dego klienta
     */
    public int ID = -1;
    
    /**
     * Login klienta
     */
    public String username = "";
    
    /**
     * Stream wej�ciowy
     */
    public ObjectInputStream streamIn  =  null;
    
    /**
     * Strema wyjsciowy
     */
    public ObjectOutputStream streamOut = null;
    
    /**
     * Okno serwera
     */
    public ServerFrame ui;
    
    /**
     * Zmienna przechowuj�ca true je�li klient stworzy� pok�j
     */
    public boolean hasRoom = false;
    
    /**
     * Lista os�b zaproszonych do pokoju przez u�ytkwnika
     */
    public ArrayList<String> invited = new ArrayList<String>();
    
    /**
     * Na potrzeby test�w. Przechowuje ostatni� wiadomo�� do klienta
     */
    public Message Outgoing;
    
    /**
     * Na potrzeby test�w. Przechowuje ostatni� wiadomo�� od klienta
     */
    public Message Incoming; 
    
    
    /**
     * Konstruktor
     * @param _server - referencja serwera
     * @param _socket - referencja socketu
     */
    public ServerThread(Serwer _server, Socket _socket)
    {  
    	super();
        server = _server;
        socket = _socket;
        ID     = socket.getPort();
        ui = _server.ui;
    }
    
    /**
     * Wysy�a wiadomo�c do klienta
     * @param msg - wiadomo��
     */
    public void send(Message msg)
    {
        try 
        {
            streamOut.writeObject(msg);
            Outgoing = msg;
            streamOut.flush();
        } 
        catch (IOException ex) 
        {
            System.out.println("Exception [SocketClient : send(...)]");
        }
    }
    
    /**
     * Zwraca ID klienta
     * @return ID klienta
     */
    public int getID()
    {  
	    return ID;
    }
   
    
    /**
     * Nasluchuje wiadomosci od klient�w
     */
	@SuppressWarnings("deprecation")
	public void run()
    {  
    	ui.jTextArea1.append("\nServer Thread " + ID + " running.");
        while (true)
        {  
    	    try
    	    {  
    	    	//odbiera wiadomosc od klienta
                Message msg = (Message) streamIn.readObject();
                Incoming = msg;
                //obsluguje wiadomosc
    	    	server.handle(ID, msg);
    	    	
            }
            catch(Exception ioe)
            {  
            	System.out.println(ID + " ERROR reading: " + ioe.getMessage());
                server.remove(ID);
                stop();
            }
        }
    }
    
	/**
	 * Otwiera sockety nas�uchuj�ce
	 * @throws IOException
	 */
    public void open() throws IOException 
    {  
        streamOut = new ObjectOutputStream(socket.getOutputStream());
        streamOut.flush();
        streamIn = new ObjectInputStream(socket.getInputStream());
    }
    
    /**
     * Zamyka sockety
     * @throws IOException
     */
    public void close() throws IOException 
    {  
    	if (socket != null)    socket.close();
        if (streamIn != null)  streamIn.close();
        if (streamOut != null) streamOut.close();
    }
}

/**
 * Klasa g��wna serwera
 * @author Pawel
 *
 */
public class Serwer implements Runnable 
{
    /**
     * Tablica klient�w
     */
    //public ServerThread clients[];
	public ArrayList<ServerThread> clients;
    
    /**
     * 
     */
    public ServerSocket server = null;
    
    /**
     * Referencja w�tku
     */
    public Thread       thread = null;
    
    /**
     * Liczba pod��czonych klient�w
     */
    public int clientCount = 0; 
    		
    /**
     * Port serwera
     */
    public int port = 13000;
    
    /**
     * Referencja okna serwera
     */
    public ServerFrame ui;
    
    /**
     * Baza
     */
    public Database db;
    
    /**
     * Lista list, przechowuje pokoje i ich cz�onk�w
     */
    public ArrayList<ArrayList<String>> usersInRoom = new ArrayList<ArrayList<String>>();

    
    
    
    /**
     * Konstruktor klasy serwera
     * @param frame - okno serwera
     */
    public Serwer(ServerFrame frame)
    {
       
        clients = new ArrayList<ServerThread>();
        ui = frame;
        db = new Database(ui.filePath);
        
        try
        {  
        	server = new ServerSocket(port);
            port = server.getLocalPort();
            ui.jTextArea1.append("Server startet. IP : " + InetAddress.getLocalHost() + ", Port : " + server.getLocalPort());
            start(); 
        }
        catch(IOException ioe)
        {  
            ui.jTextArea1.append("Can not bind to port : " + port + "\nRetrying"); 
            ui.RetryStart(0);
        }
    }
    
    /**
     * KOnstruktor klasy serwera
     * @param frame - okno serwera
     * @param Port - port serwera
     */
    public Serwer(ServerFrame frame, int Port)
    {
       
        clients = new ArrayList<ServerThread>();
        ui = frame;
        port = Port;
        db = new Database(ui.filePath);
        
        try
        {  
        	server = new ServerSocket(port);
            port = server.getLocalPort();
            ui.jTextArea1.append("Server startet. IP : " + InetAddress.getLocalHost() + ", Port : " + server.getLocalPort());
            start(); 
        }
        catch(IOException ioe)
        {  
            ui.jTextArea1.append("\nCan not bind to port " + port + ": " + ioe.getMessage()); 
        }
    }
	
    /**
     * Akceptuje po��czenie klienta z serwerem
     */
    public void run()
    {  
    	while (thread != null)
    	{  
            try
            {  
            	ui.jTextArea1.append("\nWaiting for a client ..."); 
            	addThread(server.accept()); 
            }
            catch(Exception ioe)
            { 
                ui.jTextArea1.append("\nServer accept error: \n");
                ui.RetryStart(0);
            }
    	}
    }
	
    /**
     * Tworzy w�tek serwera
     */
    public void start()
    {  
    	if (thread == null)
    	{  
            thread = new Thread(this); 
            thread.start();
    	}
    }
    
    /**
     * Zatrzymuje w�tek serwera
     */
    @SuppressWarnings("deprecation")
    public void stop(){  
        if (thread != null){  
            thread.stop(); 
	    thread = null;
	}
    }
    
    /**
     * Szuka numer ID clienta
     * @param ID - szukane id
     * @return numer id klienta
     */
    private int findClient(int ID)
    {  
    	for (int i = 0; i < clientCount; i++)
    	{
        	if (clients.get(i).getID() == ID)
        	{
                    return i;
            }
    	}
    	return -1;
    }
	
    /**
     * Metoda obs�uguj�ca otrzymane wiadmo��i od klient�w
     * @param ID - ID klienta, kt�rego wiadomo�� jest obs�ugiwana
     * @param msg - wiadomo�� wys�ana przez klienta na serwer
     */
    public synchronized void handle(int ID, Message msg)
    {
    	/**
    	 * 
    	 */
    	if (msg.content.equals(".bye"))
    	{
            Announce("signout", "SERVER", msg.sender);
            AnnounceBye(msg.sender);
            remove(ID); 
    	}
    	else
    	{
    	  switch(msg.type)
    	  {
    	    /**
    	     * Zalogowanie u�ytkownika
    	     */
    	  	case "login":
            {
            	//jesli nie ma takiego uzytkownika
                if(findUserThread(msg.sender) == null)
                {
                	//jesli login istnieje w bazie
                    if(db.checkLogin(msg.sender, msg.content) && !msg.sender.startsWith("[Room "))
                    {
                        clients.get(findClient(ID)).username = msg.sender;
                        clients.get(findClient(ID)).send(new Message("login", "SERVER", "TRUE", msg.sender));
                        sleep(51);
                        Announce("newuser", "SERVER", msg.sender);
                        SendUserList(msg.sender);
                    }
                    else
                    {
                    	clients.get(findClient(ID)).send(new Message("login", "SERVER", "FALSE", msg.sender));
                    } 
                }
                else{
                	clients.get(findClient(ID)).send(new Message("login", "SERVER", "FALSE", msg.sender));
                }
                break;
            }
            /**
             * Wys�anie wiadomo�ci do wszystkich lub prywatna wiadomo��
             */
    	  	case "message":
            {
                if(msg.recipient.equals("All"))
                {
                    Announce("message", msg.sender, msg.content);
                }
                else
                {
                    findUserThread(msg.recipient).send(new Message(msg.type, msg.sender, msg.content, msg.recipient));
                    clients.get(findClient(ID)).send(new Message(msg.type, msg.sender, msg.content, msg.recipient));
                }
                break;
            }
            /**
             * Test po��czenia
             */
    	  	case "test":
            {
            	clients.get(findClient(ID)).send(new Message("test", "SERVER", "OK", msg.sender));
                break;
            }
            /**
             * Za�o�enie konta i zapisanie go w bazie
             */
    	  	case "signup":
            {
                if(findUserThread(msg.sender) == null)
                {
                    if(!db.userExists(msg.sender) && !msg.sender.startsWith("[") && !msg.sender.startsWith("[Room ") && !msg.sender.startsWith("[R")
                    		&& !msg.sender.startsWith("[r"))
                    {
                        db.addUser(msg.sender, msg.content);
                        clients.get(findClient(ID)).username = msg.sender;
                        clients.get(findClient(ID)).send(new Message("signup", "SERVER", "TRUE", msg.sender));
                        clients.get(findClient(ID)).send(new Message("login", "SERVER", "TRUE", msg.sender));
                        Announce("newuser", "SERVER", msg.sender);
                        SendUserList(msg.sender);
                    }
                    else
                    {
                    	clients.get(findClient(ID)).send(new Message("signup", "SERVER", "FALSE", msg.sender));
                    }
                }
                else
                {
                	clients.get(findClient(ID)).send(new Message("signup", "SERVER", "FALSE", msg.sender));
                }
                break;
            }
            /**
             * Propozycja wys�ania pliku do wybranego u�ytkownika
             */
    	  	case "upload_req":
            {
            	// Nie mo�na do wszystkich
                if(msg.recipient.equals("All"))
                {
                	clients.get(findClient(ID)).send(new Message("message", "SERVER", "Uploading to 'All' forbidden", msg.sender));
                }
                else if(msg.recipient.startsWith("[Room "))
                {
                	clients.get(findClient(ID)).send(new Message("message", "SERVER", "Uploading to Room forbidden", msg.sender));
                }
                else
                {
                    findUserThread(msg.recipient).send(new Message("upload_req", msg.sender, msg.content, msg.recipient));
                }
                break;
            }
            /**
             * Odpowied� na propozycj� wys�ania pliku
             */
    	  	case "upload_res":
            {
                if(!msg.content.equals("NO"))
                {

                	//Adres IP u�ytkownika
                    String IP = findUserThread(msg.sender).socket.getInetAddress().getHostAddress();
                    findUserThread(msg.recipient).send(new Message("upload_res", IP, msg.content, msg.recipient));
                }
                else
                {
                    findUserThread(msg.recipient).send(new Message("upload_res", msg.sender, msg.content, msg.recipient));
                }
                break;
            }
            /**
             * Zaproszenie do pokoju wybranego u�ytkownika
             */
    	  	case "invite":
            {
            	// jesli chce zaprosic "All"
            	if(msg.recipient.equals("All"))
            	{
            		clients.get(findClient(ID)).send(new Message("message", "SERVER", "Inviting 'All' forbidden", msg.sender));
            	}
            	else if(msg.recipient.startsWith("[Room "))
            	{
            		clients.get(findClient(ID)).send(new Message("message", "SERVER", "You can't do that", msg.sender));
            	}
            	else
            	{
            		if(!clients.get(findClient(ID)).invited.contains(msg.recipient)) // jesli nie byl zapraszany
            			findUserThread(msg.recipient).send(new Message("invite", msg.sender, msg.content, msg.recipient, msg.room)); // szuka usera kt�rego chce zaprosic
            		else
            			clients.get(findClient(ID)).send(new Message("message", "SERVER", "You already invited him!", msg.sender));
            	}
            	break;
            }
            /**
             * Odpowied� adresata na zaproszenie do pokoju
             */
    	  	case "invitation_res":
            {
            	//jesli sie zgodzil
                if(!msg.content.equals(msg.sender + " didn't "))
                {
                	int index = findRoom(msg.recipient, 1); // szuka kt�ry na liscie jest ten pokoj; 0 bo szukamy nazwy pokoju
                	usersInRoom.get(index).add(msg.sender); // dodaje do tego pokoju uzytkownika
                	findUserThread(msg.recipient).invited.add(msg.sender); // dodaje do listy zaproszonych
                	
                    findUserThread(msg.recipient).send(new Message("invitation_res", "Server", msg.content, msg.recipient, msg.room));// pokazywanie pokoju na liscie dla zaproszonego
                    clients.get(findClient(ID)).send(new Message("invitation_res", "Server", msg.content, msg.recipient, msg.room));// pokazywanie pokoju na liscie dla zaproszonego
                   
                    //SendUserList(msg.sender);
                }
                else
                {
                    findUserThread(msg.recipient).send(new Message("invitation_res", msg.sender, msg.content, msg.recipient, msg.room));
                    SendUserList(msg.sender);
                    
                }
                break;
            }
            /**
             * Zak�ada pok�j
             */
    	  	case "register_room":
            {
            	
            	ArrayList<String> roomUsers = new ArrayList<String>();
            	roomUsers.add(msg.content); // nazwa pokoju na indexie 0
            	roomUsers.add(msg.sender);
                usersInRoom.add(roomUsers);
                clients.get(findClient(ID)).hasRoom = true;
                break;
            }
            /**
             * Kasuje pok�j
             */
    	  	case "delete_room":
            {
            	if(clients.get(findClient(ID)).hasRoom)
            	{
            		for(int j = 0; j < usersInRoom.size(); ++j)
            		{
            			int x = findRoom(msg.sender, 1);
            			if(x != -1)
            			{
            				usersInRoom.remove(x);
            				clients.get(findClient(ID)).hasRoom = false;
            				AnnounceBye(msg.sender);
            				clients.get(findClient(ID)).invited.clear();
            			}
            		}
            	}
            	break;
            }
            /**
             * Po wy��czeniu przez klienta okna czatu, usuwa go ze wszystkich pokoi i kasuje jego pok�j
             */
    	  	case "delete_rooms":
            {
            	@SuppressWarnings("unused")
				boolean hadRoom = false; // mo�e niepotrzebne
            	if(clients.get(findClient(ID)).hasRoom)
            	{
            		for(int j = 0; j < usersInRoom.size(); ++j)
            		{
            			int x = findRoom(msg.sender, 1);
            			if(x != -1)
            			{
            				usersInRoom.remove(x);
            				clients.get(findClient(ID)).hasRoom = false;
            				hadRoom = true; ///
            			}
            			
            		}
            	}
            	if(!clients.get(findClient(ID)).hasRoom) // usuwanie z czyjego� pokoju
            	{
            		boolean flag;
            		for(int j = 0; j < usersInRoom.size(); ++j)
            		{
            			flag = false;
            			int i = usersInRoom.get(j).size();
            			for(int k = 0; k < i; k++ )
            			{
            				int x = findRoom(msg.sender, k);
            				if(x != -1)
            				{
            					usersInRoom.get(x).remove(msg.sender);
            					findUserThread(usersInRoom.get(x).get(1)).invited.remove(msg.sender); // kasuje z listy zapraszanych
            					usersInRoom.get(x).trimToSize(); // 
            					flag = true;
            				}
            				if(flag) break;
            			}
            		}
            	}
            	break;
            }
            /**
             * Opuszczenie pokoju
             */
    	  	case "leave_room":
            {
            	boolean flag;
        		for(int j = 0; j < usersInRoom.size(); ++j)
        		{
        			flag = false;
        			int i = usersInRoom.get(j).size();
        			for(int k = 0; k < i; k++ )
        			{
        				int x = findRoom(msg.sender, k);
        				if((x != -1) && (k != 1) && usersInRoom.get(x).get(k).equals(msg.sender))
        				{
        					usersInRoom.get(x).remove(msg.sender);
        					findUserThread(usersInRoom.get(x).get(1)).invited.remove(msg.sender); // kasuje z listy zapraszanych
        					usersInRoom.get(x).trimToSize(); // 
        					flag = true;
        					LeaveRoom(msg.content, findClient(ID));
        					
        				}
        				if(flag) break;
        			}
        		}
        		break;
            }
            /**
             * Wysy�a wiadomo�� do pokoju
             */
    	  	case "message_room":
            {
            	Room r = new Room(msg.recipient);
            	int x = findRoom(msg.recipient, 0); // na kt�rym pokoj jest indexie
            	for(int i = 1; i < usersInRoom.get(x).size(); ++i)
            	{
            		findUserThread(usersInRoom.get(x).get(i)).send(new Message("message_room", msg.sender, msg.content, msg.recipient, r));
            	}
            	break;
            }
            /**
             * Wysy�a list� u�ytkownik�w
             */
    	  	case "users_list":
            {
            	int roomNumber = findRoom("[Room " + msg.content + "]", 0);
            	if(roomNumber != -1)
            	{
            		if(usersInRoom.get(roomNumber).contains(msg.sender))
            		{
            			String list = "\n";
            			for(int i = 1; i < usersInRoom.get(roomNumber).size(); ++i)
            			{
            				list += i + ") " + usersInRoom.get(roomNumber).get(i) + "\n";
            			}
            			clients.get(findClient(ID)).send(new Message("message", "SERVER", list, msg.sender));
            		}
            		else
            			clients.get(findClient(ID)).send(new Message("message", "SERVER", "You're not in this room", msg.sender));
            	}
            	break;
            }
            default:
            {
            	clients.get(findClient(ID)).send(new Message("message", "SERVER", "...", msg.sender));
            }
    	  }
    	}
    }
    
    /**
     * Wysy�a wiadomo�c do wszytkich klient�w
     * @param type - typ wiadomosci
     * @param sender - nadawca
     * @param content - zawartosc wiadomosci
     */
    public void Announce(String type, String sender, String content)
    {
        Message msg = new Message(type, sender, content, "All");
        for(int i = 0; i < clientCount; i++)
        {
        	clients.get(i).send(msg);
        }
    }
    
    /**
     * Kasuje pok�j u�ytkownika z listy online
     * @param sender - u�ytkownik kt�rego pok�j jest kasowany
     */
    public void AnnounceBye(String sender)
    {
        for(int i = 0; i < clientCount; i++)
        {
            clients.get(i).send(new Message("delete_room", sender, "Delete Room", "All"));
        }
    }
    
    /**
     * Wyj�cie z pokoju
     * @param sender - login wychodz�cego
     * @param id - ID wychodz�cego
     */
    public void LeaveRoom(String sender, int id)
    {
    	clients.get(id).send(new Message("delete_room", sender, "Delete Room", "All"));
    }
    
    /**
     * Wywy�a do wszystkich klient�w wadomo�� o po��czeniu nowego klienta
     * @param toWhom - login odbiorcy
     */
    public void SendUserList(String toWhom)
    {
        for(int i = 0; i < clientCount; i++)
        {
            findUserThread(toWhom).send(new Message("newuser", "SERVER", clients.get(i).username, toWhom));
        }
    }
    
    /**
     * Wysy�a list� zalogowanych u�ytkownik�w do nowego u�ytkownika
     * @param toWhom - odbiorca listy (nowy u�ytkonik)
     */
    public void SendRoomList(String toWhom)
    {
        for(int i = 0; i < clientCount; i++)
        {
            findUserThread(toWhom).send(new Message("newuser", "SERVER", clients.get(i).username, toWhom));
        }
    }
    
    /**
     * Szuka w�tku klienta po loginie
     * @param usr - login klienta
     * @return ServerThread - w�tek klienta
     */
    public ServerThread findUserThread(String usr)
    {
        for(int i = 0; i < clientCount; i++)
        {
            if(clients.get(i).username.equals(usr))
            {
                return clients.get(i);
            }
        }
        return null;
    }
	
    
    /**
     * Zamyka w�tek klienta i usuwa go z listy klient�w
     * @param ID - id w�tku klienta
     */
    @SuppressWarnings("deprecation")
    public synchronized void remove(int ID)
    {  
    	int pos = findClient(ID);
    	
        if(pos >= 0)
        {  
            ServerThread toTerminate = clients.get(pos);
            clients.remove(pos);
            ui.jTextArea1.append("\nRemoving client thread " + ID + " at " + pos);
            if(pos < clientCount-1)
            {
                for (int i = pos+1; i < clientCount; i++)
                {
                    //clients.get(i-1) = clients.get(i);\\\\\\\\\\\\\
                }
            }
            clientCount--;
            try
            {  
            	toTerminate.close(); 
            }
            catch(IOException ioe)
            {  
            	ui.jTextArea1.append("\nError closing thread: " + ioe); 
            }
            toTerminate.stop(); 
        }
    }
    
    /**
     * Dodaje w�tek klienta
     * @param socket - socket klienta
     */
    private void addThread(Socket socket)
    {  
    	if (clientCount <= clients.size())
    	{  
            ui.jTextArea1.append("\nClient accepted: " + socket);
            //clients.get(clientCount) = new ServerThread(this, socket);
            clients.add(new ServerThread(this, socket));
            try
            {  
            	clients.get(clientCount).open(); 
            	clients.get(clientCount).start(); // start w�tku
            	clientCount++; // dodanie klienta
            }
            catch(IOException ioe)
            {  
            	ui.jTextArea1.append("\nError opening thread: " + ioe); 
            } 
    	}
    	else
    	{
            ui.jTextArea1.append("\nClient refused: maximum " + clients.size() + " reached.");
    	}
    }
    
    /**
     * Funkcja wyszukuj�ca index listy pokoju
     * @param name - szukana nazwa
     * @param option - kt�rego indexu masz szuka� (0 - nazwa pokoju, 1 - za�o�yciel, 2-... - user)
     * @return Zwraca index listy pokoju, w kt�rym pojawi�a si� szukana nazwa, na wybranym miejscu
     */
    private int findRoom(String name, int option)
    {
    	for(int i = 0; i < usersInRoom.size(); i++)
        {
    		if(option < usersInRoom.get(i).size())
    			if(name.equals(usersInRoom.get(i).get(option)))
    			{
    				return i;
    			}
        }
        return -1;
    }
    
    /**
     * Usypianie w�tku
     * @param time - okres czasu na jaki ma zatrzyma�
     */
    public void sleep(int time)
	{
		try 
		{
			Thread.sleep(time);
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
	}
}
