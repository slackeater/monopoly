package tests;

import static org.junit.Assert.assertTrue;

import java.awt.Color;

import org.junit.Before;
import org.junit.Test;
import common.GameServer;
import common.Player;




public class playerTests {

	@Before
	public void setup(){

	
	}
	
	@Test
	public void playerAttributesSaveCorrectly() {
		Color blue = Color.BLUE;
		Player p = new Player("Justin", blue, 5000, null);
		assertTrue(p.getName().equals("Justin"));
		assertTrue(p.getAccount() == 5000);
		
	}
	
}
