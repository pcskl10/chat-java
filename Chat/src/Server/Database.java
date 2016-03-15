package Server;

import java.io.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;

/**
 * Baza zawieraj¹ca dane o u¿ytkownikach
 * @author Pawel
 *
 */
public class Database 
{
    /**
     * Œcie¿ka do bazy
     */
    public String filePath;
    
    /**
     * Konstruktor
     * @param filePath - scie¿ka do bazy
     */
    public Database(String filePath)
    {
        this.filePath = filePath;
    }
    
    /**
     * Sprawdza czy u¿ytkownik istnieje w bazie
     * @param username - login u¿ytkonika
     * @return true, jeœli istnieje
     */
    public boolean userExists(String username)
    {
        try
        {
            File fXmlFile = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            
            NodeList nList = doc.getElementsByTagName("user");
            
            for (int temp = 0; temp < nList.getLength(); temp++) 
            {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) 
                {
                    Element eElement = (Element) nNode;
                    if(getTagValue("username", eElement).equals(username))
                    {
                        return true;
                    }
                }
            }
            return false;
        }
        catch(Exception ex)
        {
            System.out.println("Database exception : userExists()");
            return false;
        }
    }
    
    /** 
     * Sprawdza czy login istnieje w bazie i czy haslo siê zgadza
     */
    public boolean checkLogin(String username, String password)
    {
        
        if(!userExists(username))
        { 
        	return false; 
        }
        
        try
        {
            File fXmlFile = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            
            NodeList nList = doc.getElementsByTagName("user");
            
            for (int temp = 0; temp < nList.getLength(); temp++) 
            {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) 
                {
                    Element eElement = (Element) nNode;
                    if(getTagValue("username", eElement).equals(username) && getTagValue("password", eElement).equals(password))
                    {
                        return true;
                    }
                }
            }
            return false;
        }
        catch(Exception ex)
        {
            System.out.println("Database exception : userExists()");
            return false;
        }
    }
    
    /**
     * Dodaje nowego u¿ytkownika do bazy
     * @param username - login u¿ytkownika
     * @param password - has³o u¿ytkownika
     */
    public void addUser(String username, String password)
    {
        
        try 
        {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(filePath);//otwieranie dokumentu
 
            Node data = doc.getFirstChild();
            
            Element newuser = doc.createElement("user");
            
            Element newusername = doc.createElement("username"); 
            newusername.setTextContent(username);
            Element newpassword = doc.createElement("password"); 
            newpassword.setTextContent(password);
            
            newuser.appendChild(newusername); 
            newuser.appendChild(newpassword); 
            data.appendChild(newuser);
            
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filePath));
            transformer.transform(source, result);//dkonanie zmian w dokumencie
 
	   } 
       catch(Exception ex)
       {
    	   System.out.println("Exceptionmodify xml");
	   }
	}
    
    /**
     * Zwraca wartoœæ tagu
     * @param sTag - rodzaj tagu
     * @param eElement - szukany element
     * @return - wartoœæ tagu
     */
    public static String getTagValue(String sTag, Element eElement) 
    {
    	NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
        Node nValue = (Node) nlList.item(0);
        return nValue.getNodeValue();
  }
}
