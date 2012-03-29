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
	public void tilesCreatedWithCorrectInfo() {
		Board sm = new Board();
		int tileNumber;
		//Tests some Terrains
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
		
		////////////////////
		//Tests some Railroads
		tileNumber  = 5;
		assertTrue(((Railroad)sm.tiles[tileNumber]).getName().equals("Reading Railroad"));
		assertTrue(((Railroad)sm.tiles[tileNumber]).getPrice()==200);
		assertTrue(((Railroad)sm.tiles[tileNumber]).getRent()==25);
		assertTrue(((Railroad)sm.tiles[tileNumber]).getMortgageValue()==100);
		//System.out.println(sm.tiles[tileNumber]);
		
		tileNumber  = 25;
		assertTrue(((Railroad)sm.tiles[tileNumber]).getName().equals("B&O Railroad"));
		assertTrue(((Railroad)sm.tiles[tileNumber]).getPrice()==200);
		assertTrue(((Railroad)sm.tiles[tileNumber]).getRent()==25);
		assertTrue(((Railroad)sm.tiles[tileNumber]).getMortgageValue()==100);
		//System.out.println(sm.tiles[tileNumber]);
		
		
		////////////////////
		//Tests some UTILITIES
		tileNumber  = 12;
		assertTrue(((Utility)sm.tiles[tileNumber]).getName().equals("Electric Company"));
		assertTrue(((Utility)sm.tiles[tileNumber]).getPrice()==150);
		assertTrue(((Utility)sm.tiles[tileNumber]).getMortgageValue()==75);
		System.out.println(sm.tiles[tileNumber]);
		
		tileNumber  = 28;
		assertTrue(((Utility)sm.tiles[tileNumber]).getName().equals("Water Works"));
		assertTrue(((Utility)sm.tiles[tileNumber]).getPrice()==150);
		assertTrue(((Utility)sm.tiles[tileNumber]).getMortgageValue()==75);
		System.out.println(sm.tiles[tileNumber]);
		
	}
	
	@Test
	public void playerOwnsProperty() {
		Board sm = new Board();
		Player p = new Player("Justin", 5000);
		Tile t = sm.tiles[1];
		p.addProperty(t);
		assertTrue(p.ownsProperty(t));
	}
	
	@Test
	public void propertyTransfer() {
		Board sm = new Board();
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
