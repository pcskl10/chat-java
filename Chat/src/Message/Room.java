package Message;

import java.io.Serializable;


/**
 * Klasa przechowuj¹ca nazwê pokoju
 * @author Pawel
 *
 */
public class Room implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Nazwa pokoju
	 */
	public String roomName = "";
	
	/**
	 * Konstruktor
	 * @param name - nazwa pokoju
	 */
	public Room(String name)
	{
		roomName = name;
	}
	
	/**
	 * @return nazwa pokoju
	 */
	String getName()
	{
		return this.roomName;
	}
}
