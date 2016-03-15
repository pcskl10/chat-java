package Message;

import java.io.Serializable;

/**
 * Klasa przechowuj¹ca wiadomosc przesy³ana na drodze klient-serwer
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
     * Referencja do klasy pokój
     */
    public Room room;
    
    /**
     * Konstruktor parametryzowany 
     * @param type - typ wiadomoœci
     * @param sender - nadawca wiadomoœci
     * @param content - zawartoœæ wiadomoœæi
     * @param recipient - adresat wiadomoœci
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
     * @param type - typ wiadomoœci
     * @param sender - nadawca wiadomoœci
     * @param content - zawartoœæ wiadomoœæi
     * @param recipient - adresat wiadomoœci
     * @param r - pokój
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
     * Wypisywanie parametrów wiadomoœci jako String
     */
    public String toString()
    {
        return "{type='"+type+"', sender='"+sender+"', content='"+content+"', recipient='"+recipient+"', room='"+room+"'}";
    }
}
