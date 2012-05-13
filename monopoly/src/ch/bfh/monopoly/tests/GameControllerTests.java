package ch.bfh.monopoly.tests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import ch.bfh.monopoly.common.Board;
import ch.bfh.monopoly.common.BoardController;
import ch.bfh.monopoly.common.GameClient;
import ch.bfh.monopoly.common.GameController;
import ch.bfh.monopoly.common.Player;
import ch.bfh.monopoly.tile.Property;
import ch.bfh.monopoly.tile.Terrain;

public class GameControllerTests {
	Locale loc;
	GameClient gameClient;
	Board board;
	GameController gc;

	@Before
	public void setup() {
		TestInstanceGenerator tig = new TestInstanceGenerator();
		gameClient= tig.getGameClient();
		board=tig.getBoard();
		gc = tig.getGc();
	}
	
	@Test
	public void buyCurrentPropertyForPlayerChangesPlayersPropertyList(){
		Player plyr = board.getPlayerByName("Justin");
		gameClient.setCurrentPlayer(plyr);
		gameClient.advanceCurrentPlayerNSpaces(1);
		gc.buyCurrentPropertyForPlayer("Justin");
		assertTrue(plyr.ownsProperty(board.getTileById(1)));
	}
	
	@Test
	public void buyCurrentPropertyForPlayerSetsTilesOwner(){
		Player plyr = board.getPlayerByName("Justin");
		gameClient.setCurrentPlayer(plyr);
		gameClient.advanceCurrentPlayerNSpaces(1);
		gc.buyCurrentPropertyForPlayer("Justin");
		Property prop = (Property)board.getTileById(1);
		assertTrue(prop.getOwner().getName().equals("Justin"));
	}
	
	@Test
	public void buyCurrentPropertyForPlayerTransfersMoney(){
		Player plyr = board.getPlayerByName("Justin");
		int plyrAccountBefore = plyr.getAccount();
		Property prop = (Property)board.getTileById(1);
		int tilePrice = prop.getPrice();
		gameClient.setCurrentPlayer(plyr);
		gameClient.advanceCurrentPlayerNSpaces(1);
		gc.buyCurrentPropertyForPlayer("Justin");
		assertTrue(plyr.getAccount()==plyrAccountBefore-tilePrice);
	}
	
	@Test
	public void buildHousesCannotBuildMoreThanFour(){
		Player plyr = board.getPlayerByName("Justin");
		gameClient.setCurrentPlayer(plyr);
		gameClient.advanceCurrentPlayerNSpaces(1);
		gc.buyCurrentPropertyForPlayer("Justin");
		gc.buyHouse(1);
		gc.buyHouse(1);
		gc.buyHouse(1);
		gc.buyHouse(1);
		try {
			gc.buyHouse(1);
			fail("FAIL: Player allowed to build more than 4 houses on a tile");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	@Test
	public void buildHotelCannotBuildMoreThanOne(){
		int tileId = 1;
		Player plyr = board.getPlayerByName("Justin");
		gameClient.setCurrentPlayer(plyr);
		gameClient.advanceCurrentPlayerNSpaces(tileId);
		gc.buyCurrentPropertyForPlayer("Justin");
		gc.buyHouse(tileId);
		gc.buyHouse(tileId);
		gc.buyHouse(tileId);
		gc.buyHouse(tileId);
		gc.buyHotel(tileId);
		try {
			gc.buyHotel(tileId);
			fail("FAIL: Player allowed to build more than 1 hotel on a tile");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	
	@Test
	public void buildHotelCannotBuildHotelUnlessFourHousesPresent(){
		int tileId = 1;
		Player plyr = board.getPlayerByName("Justin");
		gameClient.setCurrentPlayer(plyr);
		gameClient.advanceCurrentPlayerNSpaces(tileId);
		gc.buyCurrentPropertyForPlayer("Justin");
		gc.buyHouse(tileId);
		gc.buyHouse(tileId);
		gc.buyHouse(tileId);
		try {
			gc.buyHotel(tileId);
			fail("FAIL: Player allowed to build more than 1 hotel on a tile");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	
	@Test
	public void sellHotelCannotSellUnlessHotelPresent(){
		int tileId = 1;
		Player plyr = board.getPlayerByName("Justin");
		gameClient.setCurrentPlayer(plyr);
		gameClient.advanceCurrentPlayerNSpaces(tileId);
		gc.buyCurrentPropertyForPlayer("Justin");
		try {
			gc.sellHotel(tileId);
			fail("FAIL: Player allowed to sell a hotel, but there are no hotels to sell");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	@Test
	public void sellHouseCannotSellUnlessHousePresent(){
		int tileId = 1;
		Player plyr = board.getPlayerByName("Justin");
		gameClient.setCurrentPlayer(plyr);
		gameClient.advanceCurrentPlayerNSpaces(tileId);
		gc.buyCurrentPropertyForPlayer("Justin");
		try {
			gc.sellHouse(tileId);
			fail("FAIL: Player allowed to sell a house, but there are no houses to sell");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	
	@Test
	public void sellHouseDepositsMoneyToPlayerAccount(){
		int tileId = 1;
		Player plyr = board.getPlayerByName("Justin");
		Terrain terrain = (Terrain)board.getTileById(1);
		gameClient.setCurrentPlayer(plyr);
		gameClient.advanceCurrentPlayerNSpaces(tileId);
		gc.buyCurrentPropertyForPlayer("Justin");
		gc.buyHouse(tileId);
		int accountBefore = plyr.getAccount();
		int houseCost = terrain.getHouseCost();
		gc.sellHouse(tileId);
		assertTrue(plyr.getAccount() == accountBefore+houseCost);
	}
	
	@Test
	public void sellHotelsDepositsMoneyToPlayerAccount(){
		int tileId = 1;
		Player plyr = board.getPlayerByName("Justin");
		Terrain terrain = (Terrain)board.getTileById(1);
		gameClient.setCurrentPlayer(plyr);
		gameClient.advanceCurrentPlayerNSpaces(tileId);
		gc.buyCurrentPropertyForPlayer("Justin");
		gc.buyHouse(tileId);
		gc.buyHouse(tileId);
		gc.buyHouse(tileId);
		gc.buyHouse(tileId);
		gc.buyHotel(tileId);
		int accountBefore = plyr.getAccount();
		int houseCost = terrain.getHotelCost();
		gc.sellHotel(tileId);
		assertTrue(plyr.getAccount() == accountBefore+houseCost);
	}
	
	@Test
	public void toggleMortgageStatusWorks(){
		int tileId = 1;
		Player plyr = board.getPlayerByName("Justin");
		Terrain terrain = (Terrain)board.getTileById(1);
		gameClient.setCurrentPlayer(plyr);
		gameClient.advanceCurrentPlayerNSpaces(tileId);
		gc.buyCurrentPropertyForPlayer("Justin");
		//is false after creation, will be set to true
		gc.toggleMortgageStatus(tileId);
		assertTrue(terrain.isMortgageActive());
		//And once more, just for fun
		gc.toggleMortgageStatus(tileId);
		assertTrue(!terrain.isMortgageActive());
	}

	@Test
	public void transferPlayerChangesOwnership(){
		int tileId = 1;
		Player plyr1 = board.getPlayerByName("Justin");
		Player plyr2 = board.getPlayerByName("Giuseppe");
		Terrain terrain = (Terrain)board.getTileById(1);
		gameClient.setCurrentPlayer(plyr1);
		gameClient.advanceCurrentPlayerNSpaces(tileId);
		gc.buyCurrentPropertyForPlayer("Justin");
		assertTrue(terrain.getOwner().getName().equals(plyr1.getName()));
		gc.transferProperty(plyr1.getName(), plyr2.getName(), tileId);
		assertTrue(terrain.getOwner().getName().equals(plyr2.getName()));
	}
	
	@Test
	public void transferPlayerDepositsMoneyToSeller(){
		int price = 300;
		int tileId = 1;
		Player plyr1 = board.getPlayerByName("Justin");
		Player plyr2 = board.getPlayerByName("Giuseppe");
		Terrain terrain = (Terrain)board.getTileById(1);
		gameClient.setCurrentPlayer(plyr1);
		gameClient.advanceCurrentPlayerNSpaces(tileId);
		gc.buyCurrentPropertyForPlayer("Justin");
		int	plyr1AccountBefore = plyr1.getAccount();
		int	plyr2AccountBefore = plyr2.getAccount();
		gc.transferPropertyForPrice(plyr1.getName(), plyr2.getName(), tileId, price);
		assertTrue(plyr1.getAccount()==plyr1AccountBefore+price);
	}
	
	@Test
	public void transferPlayerWithdrawsMoneyFromBuyer(){
		int price = 300;
		int tileId = 1;
		Player plyr1 = board.getPlayerByName("Justin");
		Player plyr2 = board.getPlayerByName("Giuseppe");
		Terrain terrain = (Terrain)board.getTileById(1);
		gameClient.setCurrentPlayer(plyr1);
		gameClient.advanceCurrentPlayerNSpaces(tileId);
		gc.buyCurrentPropertyForPlayer("Justin");
		int	plyr1AccountBefore = plyr1.getAccount();
		int	plyr2AccountBefore = plyr2.getAccount();
		gc.transferPropertyForPrice(plyr1.getName(), plyr2.getName(), tileId, price);
		assertTrue(plyr2.getAccount()==plyr2AccountBefore-price);
	}
	
	@Test
	public void transferPlayerAddsPropertyToPropertyList(){
		int price = 300;
		int tileId = 1;
		Player plyr1 = board.getPlayerByName("Justin");
		Player plyr2 = board.getPlayerByName("Giuseppe");
		Terrain terrain = (Terrain)board.getTileById(1);
		gameClient.setCurrentPlayer(plyr1);
		gameClient.advanceCurrentPlayerNSpaces(tileId);
		gc.buyCurrentPropertyForPlayer("Justin");
		gc.transferPropertyForPrice(plyr1.getName(), plyr2.getName(), tileId, price);
		assertTrue(plyr2.ownsProperty(terrain));
	}
	
	
	
	
	

}
