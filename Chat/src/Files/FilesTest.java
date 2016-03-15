package Files;

import java.io.File;

import junit.framework.Assert;

import org.junit.Test;

import Klient.ChatGUI;
import Message.Message;
import Server.ServerFrame;

public class FilesTest 
{
	
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

	@SuppressWarnings("deprecation")
	@Test
	public void test() 
	{
		ServerFrame ui = new ServerFrame();
		ui.RetryStart(13000);
		
		ChatGUI cui1 = new ChatGUI();// Anurag
		cui1.TestConnect();
		ChatGUI cui2 = new ChatGUI();// aaa
		cui2.TestConnect();
		sleep(50);
		
		cui1.client.send(new Message("login", "Anurag", "password", "SERVER"));
		sleep(50);
		Assert.assertEquals("TRUE", cui1.client.Incoming.content);
		cui2.client.send(new Message("login", "aaa", "password", "SERVER"));
		sleep(50);
		Assert.assertEquals("TRUE", cui2.client.Incoming.content);
		
		cui1.file = new File("C:\\Users\\Pawel\\Desktop\\wahadlo.xls");
		cui1.client.send(new Message("upload_req", cui1.username, cui1.file.getName(), "All"));
		sleep(50);
		Assert.assertEquals("Uploading to 'All' forbidden", cui1.client.Incoming.content);// nie mo¿na wys³aæ do wszystkich
		
		
		cui1.client.send(new Message("upload_req", "Anurag", cui1.file.getName(), "aaa"));
		sleep(50);
		Assert.assertEquals(cui1.file.getName(), cui2.client.Incoming.content);
		cui2.client.send(new Message("upload_res", "aaa", "NO", "Anurag"));
		
		
		
		
		
	}

}
