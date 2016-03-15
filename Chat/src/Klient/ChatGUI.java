package Klient;

import Message.Message;
import Message.Room;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

import javax.swing.*;
import javax.swing.text.DefaultCaret;

/**
 * Okno czatu klienta
 * @author Pawel
 *
 */
public class ChatGUI extends JFrame 
{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Referencja do klasy obs³uguj¹cej socket klienta
	 */
	public SocketClient client;
	
	/**
	 * Posrt serwera
	 */
    public int port;
    
    /**
     * Parametry po³¹czenia
     */
    public String serverAddr, username, password;
    
    /**
     * W¹tek klienta
     */
    public Thread clientThread;
    
    /**
     * Model listy u¿ytkowników
     */
    @SuppressWarnings("rawtypes")
	public DefaultListModel model;
    
    /**
     * Referencja file, która bêdzie przechowywaæ obiekt wysy³anego pliku
     */
    public File file;
    
    /**
     * 
     */
    //public String historyFile = "D:/History.xml";
    
    /**
     * Referencja do pokoju
     */
    public Room room;
    
    /**
     * Konstruktor
     */
    @SuppressWarnings("unchecked")
	public ChatGUI() 
    {
        initComponents();
        this.setTitle("Czat");
        model.addElement("All");
        jList1.setSelectedIndex(0);

        this.addWindowListener(new WindowListener() 
        {

            public void windowOpened(WindowEvent e) {}
            @SuppressWarnings("deprecation")
			public void windowClosing(WindowEvent e) 
            { 
            	try
            	{ 
            		client.send(new Message("delete_rooms", username, "[Room "+username+"]", "SERVER"));
            		client.send(new Message("message", username, ".bye", "SERVER"));
            		clientThread.stop();
            	}
            	catch(Exception ex){} 
            }
            public void windowClosed(WindowEvent e) {}
            public void windowIconified(WindowEvent e) {}
            public void windowDeiconified(WindowEvent e) {}
            public void windowActivated(WindowEvent e) {}
            public void windowDeactivated(WindowEvent e) {}
        });
        
    }
    

    /**
     * Inicjuje komponenty GUI
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })                        
    private void initComponents() 
    {

        jLabel1 = new JLabel();
        jTextField1 = new JTextField();
        jLabel2 = new JLabel();
        jTextField2 = new JTextField();
        jButton1 = new JButton();
        jTextField3 = new JTextField();
        jLabel3 = new JLabel();
        jLabel4 = new JLabel();
        jButton3 = new JButton();
        jPasswordField1 = new JPasswordField();
        jSeparator1 = new JSeparator();
        jScrollPane1 = new JScrollPane();
        jTextArea1 = new JTextArea();
        jScrollPane2 = new JScrollPane();
        jList1 = new JList();
        jLabel5 = new JLabel();
        jTextField4 = new JTextField();
        jTextField4.addKeyListener(new KeyAdapter()
        {
        	public void keyReleased(KeyEvent e)
        	{
        		jTextField4ActionReleased(e);
        	}
        });
        

        jButton2 = new JButton();
        jSeparator2 = new JSeparator();
        jTextField5 = new JTextField();
        jButton5 = new JButton();
        jButton6 = new JButton();
        jLabel6 = new JLabel();
        jScrollPane3 = new JScrollPane();
        jButton7 = new JButton();
        jButton8 = new JButton();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Host Address : ");

        jTextField1.setText("localhost");

        jLabel2.setText("Host Port : ");

        jTextField2.setText("13000");

        jButton1.setText("Connect");
        jButton1.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent evt) 
            {
                jButton1ActionPerformed(evt);
            }
        });

        jTextField3.setText("a");
        jTextField3.setEnabled(false);

        jLabel3.setText("Password :");

        jLabel4.setText("Username :");

        jButton3.setText("SignUp");
        jButton3.setEnabled(false);
        jButton3.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent evt) 
            {
                jButton3ActionPerformed(evt);
            }
        });

        jPasswordField1.setText("password");
        jPasswordField1.setEnabled(false);

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setFont(new Font("Arial", 0, 14)); 
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);
        DefaultCaret caret = (DefaultCaret)jTextArea1.getCaret(); //
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE); // scrollowanie okna

        jList1.setModel((model = new DefaultListModel()));
        jScrollPane2.setViewportView(jList1);

        jLabel5.setText("Message : ");

        jButton2.setText("Login");
        jButton2.setEnabled(false);
        jButton2.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent evt) 
            {
                jButton2ActionPerformed(evt);
            }
        });

        jButton5.setText("...");
        jButton5.setEnabled(false);
        jButton5.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent evt) 
            {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setText("Send");
        jButton6.setEnabled(false);
        jButton6.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent evt) 
            {
                jButton6ActionPerformed(evt);
            }
        });

        jLabel6.setText("File :");

        jButton7.setText("Invite");
        jButton7.setEnabled(false);
        jButton7.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent evt) 
            {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setText("Create room");
        jButton8.setEnabled(false);
        jButton8.addActionListener(new java.awt.event.ActionListener() 
        {
            public void actionPerformed(ActionEvent evt) 
            {
                jButton8ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator2)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField3)
                            .addComponent(jTextField1))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField2)
                            .addComponent(jPasswordField1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 378, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(47, 47, 47))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField4))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 436, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane3))
                            .addGroup(layout.createSequentialGroup()
                                //.addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 47, Short.MAX_VALUE)
                                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(48, 48, 48)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(26, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jButton3)
                    .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2))
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 264, Short.MAX_VALUE)
                    .addComponent(jScrollPane3))
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    //.addComponent(jButton4)
                    .addComponent(jLabel5)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                    .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton7))
                .addContainerGap())
        );

        pack();
    }                         

    /**
     * Akcja po nacisnieciu "Connect"
     * @param evt
     */
    private void jButton1ActionPerformed(ActionEvent evt) 
    {                                         
        serverAddr = jTextField1.getText(); 
        port = Integer.parseInt(jTextField2.getText());
        
        if(!serverAddr.isEmpty() && !jTextField2.getText().isEmpty())
        {
            try
            {
                client = new SocketClient(this);
                clientThread = new Thread(client);
                clientThread.start();
                client.send(new Message("test", "testUser", "testContent", "SERVER")); // wiadomoœæ testowa
            }
            catch(Exception ex)
            {
                jTextArea1.append("[Application > Me] : Server not found\n");
            }
        }
    }                                        

    /**
     * Akcja po nacisnieciu "Login"
     * @param evt
     */
    @SuppressWarnings("deprecation")
	private void jButton2ActionPerformed(ActionEvent evt) 
    {                                         
        username = jTextField3.getText();
        password = jPasswordField1.getText();
        	
        if(!username.isEmpty() && !password.isEmpty())
        {
            client.send(new Message("login", username, password, "SERVER"));
            this.setTitle("Czat - " + username);
        }
    }                                        

    
    
    /**
     * Akcja po nacisnieciu klawisza Enter (wysy³anie wiadomoœci)
     * @param evt - naciœniêcie klawisza Enter
     */
    private void jTextField4ActionReleased(KeyEvent evt) 
    {                                         
    	if( evt.getKeyCode() == KeyEvent.VK_ENTER )
		{
			String msg = jTextField4.getText();
	        String target = jList1.getSelectedValue().toString();
	        
	       if(!commands(msg))
	       { 	
	    	   if(!msg.isEmpty() && !target.isEmpty())
	    	   {
	    		   jTextField4.setText("");
	    		   if(target.startsWith("[Room"))
	    			   client.send(new Message("message_room", username, msg, target));
	    		   else
	    			   client.send(new Message("message", username, msg, target));
	    	   }
	       }
	       else jTextField4.setText("");
		}
    }
    
    /**
     * Akcja po nacisnieciu "Signup"
     * @param evt
     */
    @SuppressWarnings("deprecation")
	private void jButton3ActionPerformed(ActionEvent evt) 
    {                                         
        username = jTextField3.getText();
        password = jPasswordField1.getText();
        
        if(!username.isEmpty() && !password.isEmpty())
        {
            client.send(new Message("signup", username, password, "SERVER"));
        }
    }                                        

    /**
     * Akcja po nacisnieciu "..."
     * @param evt
     */
    private void jButton5ActionPerformed(ActionEvent evt) 
    {                                         
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.showDialog(this, "Select File");
        file = fileChooser.getSelectedFile();
        
        if(file != null)
        {
            if(!file.getName().isEmpty())
            {
                jButton6.setEnabled(true); 
                String str;
                
                // Jesli sciezka ma wiecej niz 30 znakow dodaj "[...]" po 20 znakach i dodaj ostanie 20
                if(jTextField5.getText().length() > 30)
                {
                    String t = file.getPath();
                    str = t.substring(0, 20) + " [...] " + t.substring(t.length() - 20, t.length());
                }
                else
                {
                    str = file.getPath();
                }
                jTextField5.setText(str);
            }
        }
    }                                        

    /**
     * Akcja po nacisnieciu "Send"
     * @param evt
     */
    private void jButton6ActionPerformed(ActionEvent evt) 
    {                                         
            long size = file.length();
            if(size < 120 * 1024 * 1024)// 120 MB
            {
                client.send(new Message("upload_req", username, file.getName(), jList1.getSelectedValue().toString()));
            }
            else
            {
                jTextArea1.append("[Application > Me] : File is size too large\n");
            }
    }
    
    /**
     * Akcja po nacisnieciu "Invite"
     * @param evt
     */
    private void jButton7ActionPerformed(ActionEvent evt) 
    {
    	String target = jList1.getSelectedValue().toString();
    	
    	if(!target.isEmpty())
    	{
    		if(room.roomName != "")
    		{
    			client.send(new Message("invite", username, room.roomName, target, this.room));
    		}
    		else
    			client.send(new Message("message", "SERVER", "Room doesn't exist", username));
    	}
    	
    }
    
    /**
     * Akcja po nacisnieciu "Create room"
     * @param evt
     */
    @SuppressWarnings("unchecked")
	private void jButton8ActionPerformed(ActionEvent evt) 
    {                                        
    	this.room = new Room(client.roomName + username + "]");
    	//hasRoom = true;
    	model.addElement(this.room.roomName);
    	System.out.println("Created " + this.room.roomName);
    	jButton8.setEnabled(false);
    	client.send(new Message("register_room", username, this.room.roomName, "Server", this.room));
    }
    
    /**
     * Obs³uguje komendy
     * @param command - komoenda
     * @return true - jeœli taka komenda istnieje
     */
    private boolean commands(String command)
    {
    	boolean flaga = false;
    	if(command.startsWith("/com_userslist "))
    	{
    		String[] tab = command.split(" ");
    		String room = tab[1];
    		client.send(new Message("users_list", username, room, "SERVER"));
    		flaga = true;
    	}
    	else if(command.startsWith("/com_leave "))
    	{
    		String[] tab = command.split(" ");
    		String room = tab[1];
    		if(room.equals(username))
    			client.send(new Message("delete_room", username, "[Room "+username+"]", "SERVER"));
    		else
    			client.send(new Message("leave_room", username, room, "SERVER"));
    		flaga = true;
    	}
    	return flaga;
    }
    
    /**
     * Metoda potrzebna do testów. Tworzy w¹tek socketa klienta
     */
    public void TestConnect()
    {
    	serverAddr = jTextField1.getText(); 
        port = Integer.parseInt(jTextField2.getText());
        
        if(!serverAddr.isEmpty() && !jTextField2.getText().isEmpty())
        {
            try
            {
                client = new SocketClient(this);
                clientThread = new Thread(client);
                clientThread.start();
                client.send(new Message("test", "testUser", "testContent", "SERVER")); // wiadomoœæ testowa
                
            }
            catch(Exception ex)
            {
                jTextArea1.append("[Application > Me] : Server not found\n");
            }
        }
    }
    

    public static void main(String args[]) 
    {
    	new ChatGUI().setVisible(true);
    }

    
    /**
     * "Connect"
     */
    public JButton jButton1;
    
    /**
     * "Login"
     */
    public JButton jButton2;
    
    /**
     * "Signup"
     */
    public JButton jButton3;
    
    /**
     * "..."
     */
    public JButton jButton5;
    
    /**
     * "Send"
     */
    public JButton jButton6;
    
    /**
     * "Invite"
     */
    public JButton jButton7;
    
    /**
     * "Create room"
     */
    public JButton jButton8;
    
    /**
     * "Host Address : "
     */
    private JLabel jLabel1;
    
    /**
     * "Host Port : "
     */
    private JLabel jLabel2;
    
    /**
     * "Password :"
     */
    private JLabel jLabel3;
    
    /**
     * "Username :"
     */
    private JLabel jLabel4;
    
    /**
     * "Message : "
     */
    private JLabel jLabel5;
    
    /**
     * "File :"
     */
    private JLabel jLabel6;
    
    /**
     * Lista zalogowanych u¿ytkowników i pokoi
     */
    @SuppressWarnings("rawtypes")
	public JList jList1;
    
    /**
     * "Password"
     */
    public javax.swing.JPasswordField jPasswordField1;
    
    /**
     * Scroll g³ównego okna
     */
    private JScrollPane jScrollPane1;
    
    /**
     * Scroll listy u¿ytkowników
     */
    private JScrollPane jScrollPane2;
    
    /**
     * 
     */
    private JScrollPane jScrollPane3;
    
    /**
     * 
     */
    private JSeparator jSeparator1;
    
    /**
     * 
     */
    private JSeparator jSeparator2;
    
    /**
     * "G³ówne okno chatu"
     */
    public JTextArea jTextArea1;
    
    
    /**
     * "Server address"
     */
    public JTextField jTextField1;
    
    /**
     * "Port"
     */
    public JTextField jTextField2;
    
    /**
     * "Username"
     */
    public JTextField jTextField3;
    
    /**
     * "Message"
     */
    public JTextField jTextField4;
    
    /**
     * "File path"
     */
    public JTextField jTextField5;                 
}
