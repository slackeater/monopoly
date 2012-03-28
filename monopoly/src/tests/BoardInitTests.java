package tests;

import static org.junit.Assert.assertTrue;

import java.awt.Color;

import org.junit.Before;
import org.junit.Test;
import common.*;




public class BoardInitTests {

	@Before
	public void setup(){

	
	}
	
	@Test
	public void locDataIsSetInTile() {
		StateManager sm = new StateManager();
		sm.createTiles();
		assertTrue(((Terrain)sm.tiles[0]).getName().equals("Justin"));
		
	}
	
}
