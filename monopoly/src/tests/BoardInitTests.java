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
	public void tilesSetWithLocData() {
		StateManager sm = new StateManager();
		
		//System.out.println(sm.tiles[1]);
		assertTrue(((Terrain)sm.tiles[1]).getName().equals("Mediterranean Avenue"));
		assertTrue(((Terrain)sm.tiles[1]).getPrice()==60);
		assertTrue(((Terrain)sm.tiles[1]).getHouseCost()==50);
		assertTrue(((Terrain)sm.tiles[1]).getHotelCost()==50);
		assertTrue(((Terrain)sm.tiles[1]).getMortgageValue()==30);
	
		assertTrue(((Terrain)sm.tiles[14]).getName().equals("Virginia Avenue"));
		assertTrue(((Terrain)sm.tiles[14]).getPrice()==160);
		assertTrue(((Terrain)sm.tiles[14]).getHouseCost()==100);
		assertTrue(((Terrain)sm.tiles[14]).getHotelCost()==100);
		assertTrue(((Terrain)sm.tiles[14]).getMortgageValue()==80);
		//System.out.println(sm.tiles[14]);
		
		assertTrue(((Terrain)sm.tiles[39]).getName().equals("Boardwalk"));
		assertTrue(((Terrain)sm.tiles[39]).getPrice()==400);
		assertTrue(((Terrain)sm.tiles[39]).getHouseCost()==200);
		assertTrue(((Terrain)sm.tiles[39]).getHotelCost()==200);
		assertTrue(((Terrain)sm.tiles[39]).getMortgageValue()==200);
		//System.out.println(sm.tiles[39]);
	}
	
	@Test
	public void playerOwnsProperty() {
		StateManager sm = new StateManager();
		Player p = new Player("Justin", 5000);
		Tile t = sm.tiles[1];
		p.addProperty(t);
		assertTrue(p.ownsProperty(t));
	}
	
	@Test
	public void propertyTransfer() {
		StateManager sm = new StateManager();
		Player j = new Player("Justin", 5000);
		Player g = new Player("Giuseppe", 5000);
		Tile t = sm.tiles[1];
		j.addProperty(t);
		assertTrue(((Property)t).getOwner().getName().equals("Justin"));
		//we could also just use the method ownsProperty, but that method probably serves no purpose for game play, just testing
		//assertTrue(p.ownsProperty(t));
		g.addProperty(t);
		assertTrue(((Property)t).getOwner().getName().equals("Giuseppe"));
	}
	
}
