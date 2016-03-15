package Server;

import org.junit.Assert;
import org.junit.Test;

import Klient.ChatGUI;
import Message.Message;
import Message.Room;

public class SocketServerTest 
{

	ServerFrame ui = new ServerFrame();
		
	ChatGUI cui1 = new ChatGUI();

	ChatGUI cui2 = new ChatGUI();

	ChatGUI cui3 = new ChatGUI();
	

	
	Room room;

	@SuppressWarnings("unchecked")
	@Test
	public void test() 
	{
		ui.RetryStart(13000);
		cui1.TestConnect();
		cui2.TestConnect();
		cui3.TestConnect();
		
		sleep(50);
		Assert.assertEquals(cui1.client.Outgoing.toString(), ui.server.clients.get(0).Incoming.toString());
		Assert.assertEquals(ui.server.clients.get(0).Outgoing.toString(), cui1.client.Incoming.toString());// test po³¹czenia (connect)
		sleep(50);
		////////////////////////////////////////////////////////////////////////////////////////////////
		
		loginNotInDatabase();
		Assert.assertEquals("FALSE", ui.server.clients.get(0).Outgoing.content); // test loginu, którego nie ma w bazie
		
		////////////////////////////////////////////////////////////////////////////////////
		
		forbiddenLogin();
		Assert.assertEquals("FALSE", ui.server.clients.get(0).Outgoing.content); // test niedozwolonego loginu
		
		////////////////////////////////////////////////////////////////////////////////////

		validLogin(cui1, "Anurag");
		Assert.assertEquals("TRUE", cui1.client.Incoming.content); // akceptacja serwera, user jest w bazie
		sleep(50);
		Assert.assertEquals("newuser", cui1.client.Incoming.type);
		Assert.assertEquals("SERVER", cui1.client.Incoming.sender);
		Assert.assertEquals("Anurag", cui1.client.Incoming.recipient); // test poprawnego loginu
		Assert.assertEquals("Anurag", ui.server.clients.get(0).username); // klient [0] zosta³ zapisany jako Anurag
		
		
		///////////////////////////////////////////////////////////////////////////////////////////////////////
		
		sendMessageToAll(cui1, "Anurag");
		Assert.assertEquals("Wiadomosc testowa", cui1.client.Incoming.content);
		Assert.assertEquals("All", cui1.client.Incoming.recipient); // wysy³anie wiadomoœci do wszystkich

		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		cui2.client.send(new Message("login", "Anurag", "password", "SERVER"));
		sleep(50);
		Assert.assertEquals("FALSE", cui2.client.Incoming.content); // nie mo¿na zalogowaæ siê na istniej¹cy nick
		
		//////////////////////////////////////////////////////////////////////////////////////////////////////
		
		validLogin(cui2, "aaa");
		Assert.assertEquals("TRUE", cui2.client.Incoming.content); // logowanie drugiego u¿ytkownika
		
		//////////////////////////////////////////////////////////////////////////////////////////////////////
		
		validLogin(cui3, "bbb");
		Assert.assertEquals("TRUE", cui3.client.Incoming.content); // logowanie trzeciego u¿ytkownika
		
		///////////////////////////////////////////////////////////////////////////////////////////////////////
		
		cui1.client.send(new Message("message", "Anurag", "Wiadomosc testowa", "All"));
		sleep(50);
		Assert.assertEquals("Wiadomosc testowa", cui2.client.Incoming.content);
		Assert.assertEquals("Wiadomosc testowa", cui3.client.Incoming.content);// wiadomosc w pokoju glownym
		
		//////////////////////////////////////////////////////////////////////////////////////////////////////
		
		cui1.client.send(new Message("message", "Anurag", "Wiadomosc testowa", "aaa"));
		sleep(50);
		Assert.assertEquals("Wiadomosc testowa", cui2.client.Incoming.content);
		Assert.assertEquals("aaa", cui2.client.Incoming.recipient);// wiadomosc prywatna
		
		/////////////////////////////////////////////////////////////////////////////////////////////////////
		
		createRoom(cui1, "Anurag");
		Assert.assertEquals(true, ui.server.clients.get(0).hasRoom);
		Assert.assertEquals(1, ui.server.usersInRoom.size());
		Assert.assertEquals("[Room Anurag]", ui.server.usersInRoom.get(0).get(0));
		Assert.assertEquals("Anurag", ui.server.usersInRoom.get(0).get(1));// tworzenie prywatnego pokoju
		
		//////////////////////////////////////////////////////////////////////////////////////////////////////
		
		cui1.client.send(new Message("invite", "Anurag", "[Room Anurag]", "All", room));
		sleep(50);
		Assert.assertEquals("Inviting 'All' forbidden", cui1.client.Incoming.content); // dodawanie wszystkich do pokoju zabronione
		
		cui1.client.send(new Message("invite", "Anurag", "[Room Anurag]", "[Room Anurag]", room));
		sleep(50);
		Assert.assertEquals("You can't do that", cui1.client.Incoming.content);// dodawanie pokoju do pokoju zabronione
		
		////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		cui1.client.send(new Message("invite", "Anurag", "[Room Anurag]", "aaa", room));
		sleep(10);
		Assert.assertEquals("invite", cui2.client.Incoming.type);// uzytkownik aaa otrzymuje zaproszenie
		Assert.assertEquals("Anurag", cui2.client.Incoming.sender);// od Anurag
		Assert.assertEquals("[Room Anurag]", cui2.client.Incoming.content); // do pokoju [Room Anurag]
		
		cui2.client.send(new Message("invitation_res", "aaa", "aaa" + " didn't ", "Anurag", room));// aaa odmawia
		sleep(50);
		Assert.assertEquals("invitation_res", cui1.client.Incoming.type); // Anurag odbiera odpowiedz
		Assert.assertEquals(false, ui.server.usersInRoom.get(0).contains("aaa")); // aaa nie ma na liscie pokoju
		
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		cui1.client.send(new Message("invite", "Anurag", "[Room Anurag]", "aaa", room));
		sleep(50);
		Assert.assertEquals("invite", cui2.client.Incoming.type);
		Assert.assertEquals("[Room Anurag]", cui2.client.Incoming.content); // do pokoju [Room Anurag]
		
		cui2.client.send(new Message("invitation_res", "aaa", "aaa", "Anurag", room));// aaa akceptuje
		sleep(50);
		Assert.assertEquals(true, ui.server.usersInRoom.get(0).contains("aaa"));// aaa zostaje dodanu do pokoju Anurag
		cui2.model.addElement("[Room Anurag]");
		
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		cui1.client.send(new Message("message_room", "Anurag", "Wiadomosc do roomu", "[Room Anurag]", room));
		sleep(50);
		Assert.assertEquals("message_room", cui1.client.Incoming.type);
		Assert.assertEquals("Wiadomosc do roomu", cui1.client.Incoming.content);
		Assert.assertEquals("Anurag", cui1.client.Incoming.sender);
		Assert.assertEquals("[Room Anurag]", cui1.client.Incoming.recipient);
		
		cui2.client.send(new Message("message_room", "aaa", "Wiadomosc do roomu2", "[Room Anurag]", room));
		sleep(50);
		Assert.assertEquals("message_room", cui1.client.Incoming.type);
		Assert.assertEquals("Wiadomosc do roomu2", cui1.client.Incoming.content);
		Assert.assertEquals("aaa", cui1.client.Incoming.sender);
		Assert.assertEquals("[Room Anurag]", cui1.client.Incoming.recipient);
		
		Assert.assertEquals("message_room", cui2.client.Outgoing.type);
		Assert.assertEquals("Wiadomosc do roomu2", cui2.client.Outgoing.content);
		Assert.assertEquals("aaa", cui2.client.Outgoing.sender);
		Assert.assertEquals("[Room Anurag]", cui2.client.Outgoing.recipient);// wiadomosci do pokoju
		
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		cui2.client.send(new Message("leave_room", "aaa", "Anurag", "SERVER"));// aaa wychodzi z pokoju 
		sleep(50);
		Assert.assertEquals(false, ui.server.usersInRoom.get(0).contains("aaa"));// aaa zostaje usuniêty z listy pokoju
		
		cui1.client.send(new Message("message_room", "Anurag", "Wiadomosc do roomu3", "[Room Anurag]", room));
		sleep(50);
		boolean b = cui2.client.Incoming.type.equals("message_room");
		Assert.assertEquals(false, b);// aaa nie dosta³ wiadomoœci z pokoju
		Assert.assertEquals("Wiadomosc do roomu3", cui1.client.Incoming.content);// Anurag dosta³
		
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		inviteRoom(cui1, "Anurag", "aaa");
		inviteRoom(cui1, "Anurag", "bbb");
		acceptInvitation(cui2, "aaa", "Anurag");
		acceptInvitation(cui3, "bbb", "Anurag");
		
		Assert.assertEquals(4, ui.server.usersInRoom.get(0).size()); // u¿ytkownicy dodani do listy pokoju
		
		cui1.client.send(new Message("invite", "Anurag", "[Room Anurag]", "aaa", room)); // ponownie zaprasza aaa do pokoju
		sleep(50);
		
		Assert.assertEquals("You already invited him!", cui1.client.Incoming.content); // nie mo¿na zaprosiæ 2 raz
		Assert.assertEquals(false, ui.server.usersInRoom.get(0).size() == 5); // nie dodano u¿ytkownika 2 raz
		
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		deleteRoom(cui1, "Anurag");
		Assert.assertEquals(0, ui.server.usersInRoom.size()); // pokój nie istnieje
		
		///////////////////////////////////////////////////////////////////////////////////////
		
		createRoom(cui1, "Anurag");
		inviteRoom(cui1, "Anurag", "aaa");
		inviteRoom(cui1, "Anurag", "bbb");
		acceptInvitation(cui2, "aaa", "Anurag");
		acceptInvitation(cui3, "bbb", "Anurag");
		leaveRoom(cui2, "aaa", "Anurag");
		Assert.assertEquals(true, ui.server.usersInRoom.get(0).size() == 3);// usuniêto aaa z pokoju
		Assert.assertEquals(false, ui.server.clients.get(0).invited.contains("aaa"));// usuniêto z listy zapraszanych, wiêc mo¿na go dodaæ ponownie
		deleteRoom(cui1, "Anurag");
		
		////////////////////////////////////////////////////////////////////////////////////////////
		
		createRoom(cui1, "Anurag");
		inviteRoom(cui1, "Anurag", "aaa");
		inviteRoom(cui1, "Anurag", "bbb");
		acceptInvitation(cui2, "aaa", "Anurag");
		acceptInvitation(cui3, "bbb", "Anurag");
		
		createRoom(cui2, "aaa");
		inviteRoom(cui2, "aaa", "Anurag");
		acceptInvitation(cui1, "Anurag", "aaa");
		Assert.assertEquals(true, ui.server.usersInRoom.size() == 2); // utworzono 2 pokoje
		Assert.assertEquals(true, ui.server.usersInRoom.get(1).size() == 3); // Anurag jest w pokoju
		
		createRoom(cui3, "bbb");
		inviteRoom(cui3, "bbb", "Anurag");
		acceptInvitation(cui1, "Anurag", "bbb");
		Assert.assertEquals(true, ui.server.usersInRoom.size() == 3); // utworzono 3 pokoje
		Assert.assertEquals(true, ui.server.usersInRoom.get(2).size() == 3); // Anurag jest w pokoju
		
		closeWindow(cui1, "Anurag");
		Assert.assertEquals(true, ui.server.usersInRoom.size() == 2);
		Assert.assertEquals(false, ui.server.usersInRoom.get(0).contains("Anurag")); //
		Assert.assertEquals(false, ui.server.usersInRoom.get(1).contains("Anurag"));
		
		///////////////////////////////////////////////////////////////////////////////////////////
		
		inviteRoom(cui3, "bbb", "aaa");
		acceptInvitation(cui2, "aaa", "bbb");
		
		inviteRoom(cui2, "aaa", "bbb");
		acceptInvitation(cui3, "bbb", "aaa");
		
		leaveRoom(cui3, "bbb", "aaa");
		
		cui2.client.send(new Message("message_room", "aaa", "test", "[Room aaa]"));
		Assert.assertEquals("test", cui2.client.Outgoing.content);
		
		Assert.assertEquals(false, cui3.client.Incoming.content.equals("test"));
		
	}
	
	
	
	/**
	 * Zaproszenie do pokju
	 * @param c - GUI klienta
	 * @param sender - nadawca
	 * @param recipient - odbiorca
	 */
	public void inviteRoom(ChatGUI c, String sender, String recipient)
	{
		Room room = new Room("[Room " + sender + "]");
		c.client.send(new Message("invite", sender, "[Room " + sender + "]", recipient, room)); // zaprasza aaa do pokoju
		sleep(50);
	}
	
	/**
	 * Akceptuje zaproszenie do pokoju
	 * @param c - GUI klienta
	 * @param recipient - odbiorca
	 * @param sender - nadawca
	 */
	public void acceptInvitation(ChatGUI c, String recipient, String sender)
	{
		Room room = new Room("[Room " + sender + "]");
		c.client.send(new Message("invitation_res", recipient, recipient, sender, room));// aaa akceptuje
		sleep(50);
		
	}
	
	/**
	 * Tworzy pokoj
	 * @param c - GUI klienta
	 * @param username - nick klienta
	 */
	public void createRoom(ChatGUI c, String username)
	{
		Room room = new Room("[Room " + username + "]");
		c.client.send(new Message("register_room", username, "[Room " + username + "]", "Server", room));
		sleep(50);
	}
	
	/**
	 * Usuwa pokój wybranego u¿ytkownika
	 * @param c - GUI klienta
	 * @param username - nick klienta
	 */
	public void deleteRoom(ChatGUI c, String username)
	{
		c.client.send(new Message("delete_room", username, "[Room "+username+"]", "SERVER"));
		sleep(50);
	}
	
	/**
	 * Login nieznajduj¹cy siê bazie
	 */
	public void loginNotInDatabase()
	{
		cui1.client.send(new Message("login", "niemawbazie", "password", "SERVER"));
		sleep(50);
	}
	
	/**
	 * Niedozwolony login
	 */
	public void forbiddenLogin()
	{
		cui1.client.send(new Message("login", "[Room ", "password", "SERVER"));
		sleep(50);
	}
	
	/**
	 * Prawid³owe dane do loginu
	 * @param c - GUI klienta
	 * @param username - nick klienta
	 */
	public void validLogin(ChatGUI c, String username)
	{
		c.client.send(new Message("login", username, "password", "SERVER"));
		sleep(50);
	}
	
	/**
	 * Wysylanie do wszystkich
	 * @param c - GUI klienta
	 * @param username - nick klienta
	 */
	public void sendMessageToAll(ChatGUI c, String username)
	{
		c.client.send(new Message("message", username, "Wiadomosc testowa", "All"));
		sleep(50);
	}
	
	/**
	 * Opuszczenie pokoju
	 * @param c - GUI klienta
	 * @param username - nick klienta
	 * @param room
	 */
	public void leaveRoom(ChatGUI c, String username, String room)
	{
		c.client.send(new Message("leave_room", username, room, "SERVER"));
		sleep(50);
	}
	
	/**
	 * Zamkniêcie okna
	 * @param c - GUI klienta
	 * @param username - nick klienta
	 */
	public void closeWindow(ChatGUI c, String username)
	{
		c.client.send(new Message("delete_rooms", username, "[Room "+username+"]", "SERVER"));
		c.client.send(new Message("message", username, ".bye", "SERVER"));
		c.clientThread.stop();
	}
	
	public void privateMessage(ChatGUI c, String username, String content, String recipient)
	{
		
		c.client.send(new Message("message", username, content, recipient));
		sleep(50);
	}
	
	
	/**
	 * Usypia w¹tek
	 * @param time - czas na jaki usypia
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
