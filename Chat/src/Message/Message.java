package Message;

import java.io.Serializable;

/**
 * Klasa przechowuj�ca wiadomosc przesy�ana na drodze klient-serwer
 * @author Pawel
 *
 */
public class Message implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * Zmienne klasy
     */
    public String type, sender, content, recipient;
    
    /**
     * Referencja do klasy pok�j
     */
    public Room room;
    
    /**
     * Konstruktor parametryzowany 
     * @param type - typ wiadomo�ci
     * @param sender - nadawca wiadomo�ci
     * @param content - zawarto�� wiadomo��i
     * @param recipient - adresat wiadomo�ci
     */
    public Message(String type, String sender, String content, String recipient)
    {
        this.type = type; 
        this.sender = sender; 
        this.content = content; 
        this.recipient = recipient;
    }
    
    /**
     * Konstruktor parametryzowany 
     * @param type - typ wiadomo�ci
     * @param sender - nadawca wiadomo�ci
     * @param content - zawarto�� wiadomo��i
     * @param recipient - adresat wiadomo�ci
     * @param r - pok�j
     */
    public Message(String type, String sender, String content, String recipient, Room r)
    {
        this.type = type; 
        this.sender = sender; 
        this.content = content; 
        this.recipient = recipient;
        this.room = r;
    }
    
    /**
     * Wypisywanie parametr�w wiadomo�ci jako String
     */
    public String toString()
    {
        return "{type='"+type+"', sender='"+sender+"', content='"+content+"', recipient='"+recipient+"', room='"+room+"'}";
    }
}
