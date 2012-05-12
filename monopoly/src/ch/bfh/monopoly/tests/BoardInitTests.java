package ch.bfh.monopoly.tests;

import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import ch.bfh.monopoly.common.*;
import ch.bfh.monopoly.event.MovementEvent;
import ch.bfh.monopoly.tile.Chance;
import ch.bfh.monopoly.tile.Property;
import ch.bfh.monopoly.tile.Railroad;
import ch.bfh.monopoly.tile.Terrain;
import ch.bfh.monopoly.tile.Tile;
import ch.bfh.monopoly.tile.TileInfo;
import ch.bfh.monopoly.tile.Utility;




public class BoardInitTests {
	final int TILES_LENGTH = 40;
	Locale loc;
	GameClient gameClient ;
	int[] terrainPositions = { 1, 3, 6, 8, 9, 11, 13, 14, 16, 18, 19, 21, 23, 24, 26, 27, 29, 31, 32, 34, 37, 39 };
	Board board;

	
	@Before
	public void setup() {
		TestInstanceGenerator tig = new TestInstanceGenerator();
		gameClient= tig.getGameClient();
		board=tig.getBoard();
	}
	

	
	/**
	 * check that the player's were created with the proper balance based upon the a given local
	 * in this case the local is EN for all tests
	 */
	@Test
	public void playersStartWithCorrectBalance() {
		Player jus = board.getPlayerByName("Justin");
		Player giu = board.getPlayerByName("Giuseppe");
		assertTrue(jus.getAccount() == 15000);
		assertTrue(giu.getAccount() == 15000);
	}
	
	/**
	 * check that the tiles have been initialized with a value from the resource bundle
	 */
	@Test
	public void boardTilesHaveNames(){
		Board board= new Board(gameClient);
		Tile t = board.getTileById(1);
		assertTrue((t.getName() != null));
	}
	
	/**
	 * check that the tiles have been initialized with x and y coordinates
	 */
	@Test
	public void boardTilesHaveXYCoord(){
		Board board= new Board(gameClient);
		for (int i = 0; i<TILES_LENGTH; i++){
			Tile t = board.getTileById(i);
			if (t instanceof Terrain){
			assertTrue((t.getCoordX() >= 0));
			assertTrue((t.getCoordY() >= 0));}
		}
	}
	
	/**
	 * check that a tile has been initialized with the information from the resource bundle
	 */
	@Test
	public void boardControllerGetsTileInformation(){
		Board board= new Board(gameClient);
		BoardController bc = new BoardController(board);
		TileInfo ti = bc.getTileInfoById(1);
		assertTrue(ti.getName().equals("Mediterranean Avenue"));
		assertTrue(ti.getPrice()==60);
		assertTrue(ti.getGroup().equals("purple"));
		
	}
	
	
	/**
	 * check that the chance cards are created with the name chance
	 */
	@Test
	public void chanceCardsCreated() {
		Board board= new Board(gameClient);
		assertTrue(((Chance)board.getTileById(7)).getName().equals("Chance"));
		assertTrue(((Chance)board.getTileById(22)).getName().equals("Chance"));
		assertTrue(((Chance)board.getTileById(36)).getName().equals("Chance"));
	}
	
	
	@Test
	public void printNameOfTile(){
		Board board= new Board(gameClient);
//		for (int i = 0; i < board.tiles.length; i++) {
//			System.out.println("Tile" + i + ":  " + board.tiles[i].getName()
//					+ "  :  " + board.tiles[i].getId() + " xyCoord: "
//					+ board.tiles[i].getCoordX() + ","
//					+ board.tiles[i].getCoordY());
//		}
		assertTrue(((Terrain)board.getTileById(1)).getName().equals("Mediterranean Avenue"));
	}
	
	
	/**
	 * check that various tiles have been initialized with the proper information from the resource bundle
	 */
	@Test
	public void tilesCreatedWithCorrectInfo() {

		Board board= new Board(gameClient);
		int tileNumber;
		//Tests some Terrains
		//System.out.println(sm.tiles[1]);
		assertTrue((board.getTileById(1).getName()).equals("Mediterranean Avenue"));
		assertTrue(((Terrain)board.getTileById(1)).getPrice()==60);
		assertTrue(((Terrain)board.getTileById(1)).getHouseCost()==50);
		assertTrue(((Terrain)board.getTileById(1)).getHotelCost()==50);
		assertTrue(((Terrain)board.getTileById(1)).getMortgageValue()==30);
	
		assertTrue(((Terrain)board.getTileById(14)).getName().equals("Virginia Avenue"));
		assertTrue(((Terrain)board.getTileById(14)).getPrice()==160);
		assertTrue(((Terrain)board.getTileById(14)).getHouseCost()==100);
		assertTrue(((Terrain)board.getTileById(14)).getHotelCost()==100);
		assertTrue(((Terrain)board.getTileById(14)).getMortgageValue()==80);
		//System.out.println(sm.getTileById(14));
		
		assertTrue(((Terrain)board.getTileById(39)).getName().equals("Boardwalk"));
		assertTrue(((Terrain)board.getTileById(39)).getPrice()==400);
		assertTrue(((Terrain)board.getTileById(39)).getHouseCost()==200);
		assertTrue(((Terrain)board.getTileById(39)).getHotelCost()==200);
		assertTrue(((Terrain)board.getTileById(39)).getMortgageValue()==200);
		//System.out.println(sm.tiles[39]);
		
		////////////////////
		//Tests some Railroads
		tileNumber  = 5;
		assertTrue(((Railroad)board.getTileById(tileNumber)).getName().equals("Reading Railroad"));
		assertTrue(((Railroad)board.getTileById(tileNumber)).getPrice()==200);
		assertTrue(((Railroad)board.getTileById(tileNumber)).getRent()==25);
		assertTrue(((Railroad)board.getTileById(tileNumber)).getMortgageValue()==100);
		//System.out.println(sm.getTileById(tileNumber));
		
		tileNumber  = 25;
		assertTrue(((Railroad)board.getTileById(tileNumber)).getName().equals("B&O Railroad"));
		assertTrue(((Railroad)board.getTileById(tileNumber)).getPrice()==200);
		assertTrue(((Railroad)board.getTileById(tileNumber)).getRent()==25);
		assertTrue(((Railroad)board.getTileById(tileNumber)).getMortgageValue()==100);
		//System.out.println(sm.getTileById(tileNumber));
		
		
		////////////////////
		//Tests some UTILITIES
		tileNumber  = 12;
		assertTrue(((Utility)board.getTileById(tileNumber)).getName().equals("Electric Company"));
		assertTrue(((Utility)board.getTileById(tileNumber)).getPrice()==150);
		assertTrue(((Utility)board.getTileById(tileNumber)).getMortgageValue()==75);
		//System.out.println(board.getTileById(tileNumber));
		
		tileNumber  = 28;
		assertTrue(((Utility)board.getTileById(tileNumber)).getName().equals("Water Works"));
		assertTrue(((Utility)board.getTileById(tileNumber)).getPrice()==150);
		assertTrue(((Utility)board.getTileById(tileNumber)).getMortgageValue()==75);
		//System.out.println(board.tiles[tileNumber]);
		
	}
	
	
}
