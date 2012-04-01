package ch.bfh.monopoly.tests;

import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import ch.bfh.monopoly.common.*;
import ch.bfh.monopoly.tile.Chance;
import ch.bfh.monopoly.tile.Property;
import ch.bfh.monopoly.tile.Railroad;
import ch.bfh.monopoly.tile.Terrain;
import ch.bfh.monopoly.tile.Tile;
import ch.bfh.monopoly.tile.TileInfo;
import ch.bfh.monopoly.tile.Utility;




//I can't stand merging

public class BoardInitTests {
	final int TILES_LENGTH = 40;
	Locale loc;
	GameClient gc ;
	int[] terrainPositions = { 1, 3, 6, 8, 9, 11, 13, 14, 16, 18, 19, 21, 23, 24, 26, 27, 29, 31, 32, 34, 37, 39 };
	/** 
	 * Setup this test class to test the creation of the board with 
	 * the local set to English
	 * */
	@Before
	public void setup(){
		loc = new Locale("EN");
		gc = new GameClient();
	}
	
	
	@Test
	public void boardTilesHaveNames(){
		Board board= new Board(loc,gc);
		Tile t = board.getTileByID(1);
		assertTrue((t.getName() != null));
	}
	
	@Test
	public void boardTilesHaveXYCoord(){
		Board board= new Board(loc,gc);
		for (int i = 0; i<TILES_LENGTH; i++){
			Tile t = board.getTileByID(i);
			if (t instanceof Terrain){
			assertTrue((t.getCoordX() >= 0));
			assertTrue((t.getCoordY() >= 0));}
		}
	}
	
	@Test
	public void boardControllerGetsTileInformation(){
		Board board= new Board(loc,gc);
		BoardController bc = new BoardController(board);
		TileInfo ti = bc.getTileInfoByID(1);
		assertTrue(ti.getName().equals("Mediterranean Avenue"));
		assertTrue(ti.getPrice()==60);
		assertTrue(ti.getGroup().equals("purple"));
		
	}
	
	@Test
	public void chanceCardsCreatedWithCorrectInfo() {
		Board board= new Board(loc,gc);
		assertTrue(((Chance)board.getTileByID(7)).getName().equals("Chance"));
		assertTrue(((Chance)board.getTileByID(22)).getName().equals("Chance"));
		assertTrue(((Chance)board.getTileByID(36)).getName().equals("Chance"));

		Chance chanceTile = (Chance)board.getTileByID(36);
		MovementEvent mv = ((MovementEvent)chanceTile.chanceCardDeck[0]);
		assertTrue(mv.getName().equals("Back you go!"));
		assertTrue(mv.getNewPosition()== -3);
		mv = ((MovementEvent)chanceTile.chanceCardDeck[1]);
		assertTrue(mv.getName().equals("Advance to Pennsylvania Railroad"));
	}
	
	@Test
	public void printNameOfTile(){
		Board board= new Board(loc,gc);
//		for (int i = 0; i < board.tiles.length; i++) {
//			System.out.println("Tile" + i + ":  " + board.tiles[i].getName()
//					+ "  :  " + board.tiles[i].getID() + " xyCoord: "
//					+ board.tiles[i].getCoordX() + ","
//					+ board.tiles[i].getCoordY());
//		}
		assertTrue(((Terrain)board.getTileByID(1)).getName().equals("Mediterranean Avenue"));
	}
	
	@Test
	public void tilesCreatedWithCorrectInfo() {

		Board board= new Board(loc,gc);
		int tileNumber;
		//Tests some Terrains
		//System.out.println(sm.tiles[1]);
		assertTrue((board.getTileByID(1).getName()).equals("Mediterranean Avenue"));
		assertTrue(((Terrain)board.getTileByID(1)).getPrice()==60);
		assertTrue(((Terrain)board.getTileByID(1)).getHouseCost()==50);
		assertTrue(((Terrain)board.getTileByID(1)).getHotelCost()==50);
		assertTrue(((Terrain)board.getTileByID(1)).getMortgageValue()==30);
	
		assertTrue(((Terrain)board.getTileByID(14)).getName().equals("Virginia Avenue"));
		assertTrue(((Terrain)board.getTileByID(14)).getPrice()==160);
		assertTrue(((Terrain)board.getTileByID(14)).getHouseCost()==100);
		assertTrue(((Terrain)board.getTileByID(14)).getHotelCost()==100);
		assertTrue(((Terrain)board.getTileByID(14)).getMortgageValue()==80);
		//System.out.println(sm.getTileByID(14));
		
		assertTrue(((Terrain)board.getTileByID(39)).getName().equals("Boardwalk"));
		assertTrue(((Terrain)board.getTileByID(39)).getPrice()==400);
		assertTrue(((Terrain)board.getTileByID(39)).getHouseCost()==200);
		assertTrue(((Terrain)board.getTileByID(39)).getHotelCost()==200);
		assertTrue(((Terrain)board.getTileByID(39)).getMortgageValue()==200);
		//System.out.println(sm.tiles[39]);
		
		////////////////////
		//Tests some Railroads
		tileNumber  = 5;
		assertTrue(((Railroad)board.getTileByID(tileNumber)).getName().equals("Reading Railroad"));
		assertTrue(((Railroad)board.getTileByID(tileNumber)).getPrice()==200);
		assertTrue(((Railroad)board.getTileByID(tileNumber)).getRent()==25);
		assertTrue(((Railroad)board.getTileByID(tileNumber)).getMortgageValue()==100);
		//System.out.println(sm.getTileByID(tileNumber));
		
		tileNumber  = 25;
		assertTrue(((Railroad)board.getTileByID(tileNumber)).getName().equals("B&O Railroad"));
		assertTrue(((Railroad)board.getTileByID(tileNumber)).getPrice()==200);
		assertTrue(((Railroad)board.getTileByID(tileNumber)).getRent()==25);
		assertTrue(((Railroad)board.getTileByID(tileNumber)).getMortgageValue()==100);
		//System.out.println(sm.getTileByID(tileNumber));
		
		
		////////////////////
		//Tests some UTILITIES
		tileNumber  = 12;
		assertTrue(((Utility)board.getTileByID(tileNumber)).getName().equals("Electric Company"));
		assertTrue(((Utility)board.getTileByID(tileNumber)).getPrice()==150);
		assertTrue(((Utility)board.getTileByID(tileNumber)).getMortgageValue()==75);
		//System.out.println(board.getTileByID(tileNumber));
		
		tileNumber  = 28;
		assertTrue(((Utility)board.getTileByID(tileNumber)).getName().equals("Water Works"));
		assertTrue(((Utility)board.getTileByID(tileNumber)).getPrice()==150);
		assertTrue(((Utility)board.getTileByID(tileNumber)).getMortgageValue()==75);
		//System.out.println(board.tiles[tileNumber]);
		
	}
	
	
}
