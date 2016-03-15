package Server;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

public class DatabaseTest 
{

	@Test
	public void test() 
	{
		Database base = new Database("../Data.xml");
		
		base.addUser("test1", "pw1");
		Assert.assertEquals(true, base.userExists("test1"));
		Assert.assertEquals(true, base.checkLogin("test1", "pw1"));
		
		
		
		Database base1 = new Database("../Dataa.xml");
		Assert.assertEquals(false, base1.userExists("test1"));
		Assert.assertEquals(false, base1.checkLogin("test1", "pw1"));
		
		
	}

}
