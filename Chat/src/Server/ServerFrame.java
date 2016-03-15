package Server;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


/**
 * Klasa okna serwera
 * @author Pawel
 *
 */
public class ServerFrame extends JFrame 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Referencja do obiektu serwera
	 */
	public Serwer server;
    
    /**
     * Œcie¿ka databazy
     */
    public String filePath = "../Data.xml";
    
    /**
     * Mechanizm do wybierania pliku
     */
    public JFileChooser fileChooser;
    
    /**
     * Konstruktor
     */
    public ServerFrame() 
    {
        initComponents();     
        jTextField3.setEditable(true);
        jTextField3.setBackground(Color.WHITE);
        fileChooser = new JFileChooser();
        jTextArea1.setEditable(false);
        
    }
    
    /**
     * Sprawdza czy system to Windows
     * @return true - jeœli system to Windows
     */
    public boolean isWin32()
    {
        return System.getProperty("os.name").startsWith("Windows");
    }

    
    /**
     * Inicjacja komponentów
     */
    private void initComponents() 
    {

        jButton1 = new JButton();
        jScrollPane1 = new JScrollPane();
        jTextArea1 = new JTextArea();
        jLabel3 = new JLabel();
        jTextField3 = new JTextField();
        jButton2 = new JButton();
        
        String filePath = "../Data.xml";
        filePath = filePath.replace("\\", "/"); 
        jTextField3.setText(filePath);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Server");

        jButton1.setText("Start Server");
        jButton1.setEnabled(true);
        jButton1.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent evt) 
            {
                jButton1ActionPerformed(evt);
            }
        });

        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Arial", 0, 14)); 
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jLabel3.setText("Database File : ");

        jButton2.setText("Browse...");
        jButton2.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent evt) 
            {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField3, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jButton2)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }

    /**
     * Akcja po naciœniêciu "Start Server"
     * @param evt
     */
    private void jButton1ActionPerformed(ActionEvent evt) 
    {
        server = new Serwer(this);
        jButton1.setEnabled(false); 
        jButton2.setEnabled(false);
    }

    /**
     * Start serwera
     * @param port - port serwera
     */
    public void RetryStart(int port)
    {
        if(server != null){ server.stop(); }
        server = new Serwer(this, port);
    }
    
    /**
     * Akcja po naciœniêciu "Browse"
     * @param evt - naciœnieciê "Browse" button
     */
    private void jButton2ActionPerformed(ActionEvent evt) 
    {
        fileChooser.showDialog(this, "Select");
        File file = fileChooser.getSelectedFile();
        
        if(file != null)
        {
            filePath = "../Data.xml";
            if(this.isWin32())
            { 
            	filePath = filePath.replace("\\", "/"); 
            }
            jTextField3.setText(filePath);
            jButton1.setEnabled(true);
        }
    }

    /**
     * Main klasy
     * @param args
     */
    public static void main(String args[]) 
    {
        new ServerFrame().setVisible(true);
    	
    }

    /**
     * Button "Start server"
     */
    private JButton jButton1;
    
    /**
     * Button "Browse"
     */
    private JButton jButton2;
    
    /**
     * "Database File : "
     */
    private JLabel jLabel3;
    
    /**
     * Scroll okna serwera
     */
    private JScrollPane jScrollPane1;
    
    /**
     * G³ówne okno serwera
     */
    public JTextArea jTextArea1;
    
    /**
     * Pole na œcie¿kê database
     */
    private JTextField jTextField3;

}
