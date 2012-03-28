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
		
		//check tile 1
		//System.out.println(sm.tiles[1]);
		assertTrue(((Terrain)sm.tiles[0]).getName().equals("baltic avenue"));
		assertTrue(((Terrain)sm.tiles[0]).getPrice()==60);
		assertTrue(((Terrain)sm.tiles[0]).getHouseCost()==50);
		assertTrue(((Terrain)sm.tiles[0]).getHotelCost()==50);
		assertTrue(((Terrain)sm.tiles[0]).getMortgageValue()==30);
		
		//check tile 14
		System.out.println(sm.tiles[14]);
		assertTrue(((Terrain)sm.tiles[0]).getName().equals("baltic avenue"));
		assertTrue(((Terrain)sm.tiles[0]).getPrice()==60);
		assertTrue(((Terrain)sm.tiles[0]).getHouseCost()==50);
		assertTrue(((Terrain)sm.tiles[0]).getHotelCost()==50);
		assertTrue(((Terrain)sm.tiles[0]).getMortgageValue()==30);

		
	}
	
}
