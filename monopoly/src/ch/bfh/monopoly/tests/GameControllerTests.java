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
import ch.bfh.monopoly.exception.TransactionException;
import ch.bfh.monopoly.tile.Property;
import ch.bfh.monopoly.tile.Terrain;
import ch.bfh.monopoly.tile.Tile;

public class GameControllerTests {
	boolean sendNetMessage = false;
	Locale loc;
	GameClient gameClient;
	Board board;
	GameController gc;
	int[] terrainPositions = { 1, 3, 6, 8, 9, 11, 13, 14, 16, 18, 19, 21, 23,
			24, 26, 27, 29, 31, 32, 34, 37, 39 };

	@Before
	public void setup() {
		TestInstanceGenerator tig = new TestInstanceGenerator("en");
		gameClient = tig.getGameClient();
		board = tig.getBoard();
		gc = tig.getGc();
	}

	
	/**
	 * test that you get your go money when you go around the board
	 */
	public void passGoDepositsMoney(){}
	
	
	/**
	 * test that buyHouseRow doesn't allow purchase if at least 1 tile has 4
	 * houses already
	 */
	@Test
	public void buyHouseRowTooManyHouses() {
		String player1name = "Justin";
		Player plyr1 = board.getPlayerByName("Justin");
		gameClient.setCurrentPlayer(plyr1, sendNetMessage);
		gameClient.advancePlayerNSpaces(6, sendNetMessage);
		gameClient.buyCurrentPropertyForPlayer(player1name, sendNetMessage);
		gameClient.advancePlayerNSpaces(2, sendNetMessage);
		gameClient.buyCurrentPropertyForPlayer(player1name, sendNetMessage);
		gameClient.advancePlayerNSpaces(1, sendNetMessage);
		gameClient.buyCurrentPropertyForPlayer(player1name, sendNetMessage);
		for (int i = 0; i < 3; i++) {
			gameClient.buyHouse(6, sendNetMessage);
			gameClient.buyHouse(8, sendNetMessage);
			gameClient.buyHouse(9, sendNetMessage);
		}
		// tile 9 will have 4 houses, the others have 3, so buildRow is not
		// possible
		gameClient.buyHouse(9, sendNetMessage);
		int plyr1AccountBefore = plyr1.getAccount();
		gameClient.buyHouseRow(6, sendNetMessage);
		int plyr1AccountAfter = plyr1.getAccount();
		assertTrue(plyr1AccountAfter == plyr1AccountBefore);
	}

	/**
	 * test that buyHouseRow doesn't change any state if player doesn't have
	 * enough money to buy all the houses for the row houses already
	 */
	@Test
	public void buyHouseRowNotEnoughMoney() throws TransactionException {
		int oriental = 6;
		int vermont = 8;
		int connecticut = 9;
		String player1name = "Justin";
		Player plyr1 = board.getPlayerByName("Justin");
		gameClient.setCurrentPlayer(plyr1, sendNetMessage);
		gameClient.advancePlayerNSpaces(6, sendNetMessage);
		gameClient.buyCurrentPropertyForPlayer(player1name, sendNetMessage);
		gameClient.advancePlayerNSpaces(2, sendNetMessage);
		gameClient.buyCurrentPropertyForPlayer(player1name, sendNetMessage);
		gameClient.advancePlayerNSpaces(1, sendNetMessage);
		gameClient.buyCurrentPropertyForPlayer(player1name, sendNetMessage);
		// house cost is 50, must buy 3, so 100 is not enough, set account so
		Tile t1 = board.getTileById(oriental);
		Terrain terrainOriental = board.castTileToTerrain(t1);
		Tile t2 = board.getTileById(vermont);
		Terrain terrainVermont = board.castTileToTerrain(t2);
		Tile t3 = board.getTileById(connecticut);
		Terrain terrainConnecticut = board.castTileToTerrain(t3);
		plyr1.withdawMoney(plyr1.getAccount() - 100);
		int plyr1AccountBefore = plyr1.getAccount();
		gameClient.buyHouseRow(6, sendNetMessage);
		int plyr1AccountAfter = plyr1.getAccount();
		assertTrue(plyr1AccountAfter == plyr1AccountBefore);
		// check that all terrain still ahve no houses
		assertTrue(terrainConnecticut.getHouseCount() == 0
				&& terrainOriental.getHouseCount() == 0
				&& terrainVermont.getHouseCount() == 0);
	}

	/**
	 * test that buyHouseRow doesn't allow construction if the currentPlayer
	 * does not own all properties
	 */
	@Test
	public void buyHouseRowNotDoesntOwnAllProperties()
			throws TransactionException {
		int oriental = 6;
		int vermont = 8;
		int connecticut = 9;
		String player1name = "Justin";
		Player plyr1 = board.getPlayerByName("Justin");
		gameClient.setCurrentPlayer(plyr1, sendNetMessage);
		gameClient.advancePlayerNSpaces(6, sendNetMessage);
		gameClient.buyCurrentPropertyForPlayer(player1name, sendNetMessage);
		gameClient.advancePlayerNSpaces(2, sendNetMessage);
		gameClient.buyCurrentPropertyForPlayer(player1name, sendNetMessage);
		// house cost is 50, must buy 3, so 100 is not enough, set account so
		Tile t1 = board.getTileById(oriental);
		Terrain terrainOriental = board.castTileToTerrain(t1);
		Tile t2 = board.getTileById(vermont);
		Terrain terrainVermont = board.castTileToTerrain(t2);
		Tile t3 = board.getTileById(connecticut);
		Terrain terrainConnecticut = board.castTileToTerrain(t3);
		int plyr1AccountBefore = plyr1.getAccount();
		gameClient.buyHouseRow(6, sendNetMessage);
		int plyr1AccountAfter = plyr1.getAccount();
		assertTrue(plyr1AccountAfter == plyr1AccountBefore);
		// check that all terrain still ahve no houses
		assertTrue(terrainConnecticut.getHouseCount() == 0
				&& terrainOriental.getHouseCount() == 0
				&& terrainVermont.getHouseCount() == 0);
	}

	/**
	 * test that buyHouseRow builds 1 house on each property when all conditions
	 * are met, and withdraws the correct amount
	 */
	@Test
	public void buyHouseRowBuildsHouses() throws TransactionException {
		int oriental = 6;
		int vermont = 8;
		int connecticut = 9;
		String player1name = "Justin";
		Player plyr1 = board.getPlayerByName("Justin");
		gameClient.setCurrentPlayer(plyr1, sendNetMessage);
		gameClient.advancePlayerNSpaces(6, sendNetMessage);
		gameClient.buyCurrentPropertyForPlayer(player1name, sendNetMessage);
		gameClient.advancePlayerNSpaces(2, sendNetMessage);
		gameClient.buyCurrentPropertyForPlayer(player1name, sendNetMessage);
		gameClient.advancePlayerNSpaces(1, sendNetMessage);
		gameClient.buyCurrentPropertyForPlayer(player1name, sendNetMessage);
		// house cost is 50, must buy 3, so 100 is not enough, set account so
		Tile t1 = board.getTileById(oriental);
		Terrain terrainOriental = board.castTileToTerrain(t1);
		Tile t2 = board.getTileById(vermont);
		Terrain terrainVermont = board.castTileToTerrain(t2);
		Tile t3 = board.getTileById(connecticut);
		Terrain terrainConnecticut = board.castTileToTerrain(t3);
		int plyr1AccountBefore = plyr1.getAccount();
		gameClient.buyHouseRow(6, sendNetMessage);
		int plyr1AccountAfter = plyr1.getAccount();
		assertTrue(plyr1AccountAfter == plyr1AccountBefore - 150);
		assertTrue(terrainConnecticut.getHouseCount() == 1
				&& terrainOriental.getHouseCount() == 1
				&& terrainVermont.getHouseCount() == 1);
		gameClient.buyHouseRow(6, sendNetMessage);
		plyr1AccountAfter = plyr1.getAccount();
		assertTrue(plyr1AccountAfter == plyr1AccountBefore - 300);
		assertTrue(terrainConnecticut.getHouseCount() == 2
				&& terrainOriental.getHouseCount() == 2
				&& terrainVermont.getHouseCount() == 2);
		// can't build more than 4 houses
		gameClient.buyHouseRow(6, sendNetMessage);
		gameClient.buyHouseRow(6, sendNetMessage);
		gameClient.buyHouseRow(6, sendNetMessage);
		plyr1AccountAfter = plyr1.getAccount();
		assertTrue(plyr1AccountAfter == plyr1AccountBefore - 600);
		assertTrue(terrainConnecticut.getHouseCount() == 4
				&& terrainOriental.getHouseCount() == 4
				&& terrainVermont.getHouseCount() == 4);
	}

	/**
	 * test that buyHotelRow doesn't allow purchase if at least 1 tile has 1
	 * hotel already
	 */
	@Test
	public void buyHotelRowTooManyHotels() {
		int oriental = 6;
		int vermont = 8;
		int connecticut = 9;
		String player1name = "Justin";
		Player plyr1 = board.getPlayerByName("Justin");
		gameClient.setCurrentPlayer(plyr1, sendNetMessage);
		gameClient.advancePlayerNSpaces(6, sendNetMessage);
		gameClient.buyCurrentPropertyForPlayer(player1name, sendNetMessage);
		gameClient.advancePlayerNSpaces(2, sendNetMessage);
		gameClient.buyCurrentPropertyForPlayer(player1name, sendNetMessage);
		gameClient.advancePlayerNSpaces(1, sendNetMessage);
		gameClient.buyCurrentPropertyForPlayer(player1name, sendNetMessage);
		Tile t1 = board.getTileById(oriental);
		Terrain terrainOriental = board.castTileToTerrain(t1);
		Tile t2 = board.getTileById(vermont);
		Terrain terrainVermont = board.castTileToTerrain(t2);
		Tile t3 = board.getTileById(connecticut);
		Terrain terrainConnecticut = board.castTileToTerrain(t3);
		for (int i = 0; i < 4; i++) {
			gameClient.buyHouse(6, sendNetMessage);
			gameClient.buyHouse(8, sendNetMessage);
			gameClient.buyHouse(9, sendNetMessage);
		}
		// tile 9 will have 4 houses, the others have 3, so buildRow is not
		// possible
		gameClient.buyHotel(9, sendNetMessage);
		int plyr1AccountBefore = plyr1.getAccount();
		gameClient.buyHouseRow(6, sendNetMessage);
		int plyr1AccountAfter = plyr1.getAccount();
		assertTrue(plyr1AccountAfter == plyr1AccountBefore);
		assertTrue(terrainConnecticut.getHotelCount() == 1
				&& terrainOriental.getHotelCount() == 0
				&& terrainVermont.getHotelCount() == 0);
		assertTrue(terrainOriental.getHouseCount() == 4
				&& terrainVermont.getHouseCount() == 4);
	}

	/**
	 * test that buyHotelRow doesn't change any state if player doesn't have
	 * enough money to buy all the hotels for the row
	 */
	@Test
	public void buyHotelRowNotEnoughMoney() throws TransactionException {
		int oriental = 6;
		int vermont = 8;
		int connecticut = 9;
		String player1name = "Justin";
		Player plyr1 = board.getPlayerByName("Justin");
		gameClient.setCurrentPlayer(plyr1, sendNetMessage);
		gameClient.advancePlayerNSpaces(6, sendNetMessage);
		gameClient.buyCurrentPropertyForPlayer(player1name, sendNetMessage);
		gameClient.advancePlayerNSpaces(2, sendNetMessage);
		gameClient.buyCurrentPropertyForPlayer(player1name, sendNetMessage);
		gameClient.advancePlayerNSpaces(1, sendNetMessage);
		gameClient.buyCurrentPropertyForPlayer(player1name, sendNetMessage);
		Tile t1 = board.getTileById(oriental);
		Terrain terrainOriental = board.castTileToTerrain(t1);
		Tile t2 = board.getTileById(vermont);
		Terrain terrainVermont = board.castTileToTerrain(t2);
		Tile t3 = board.getTileById(connecticut);
		Terrain terrainConnecticut = board.castTileToTerrain(t3);
		for (int i = 0; i < 4; i++) {
			gameClient.buyHouse(6, sendNetMessage);
			gameClient.buyHouse(8, sendNetMessage);
			gameClient.buyHouse(9, sendNetMessage);
		}
		// hotel cost is 50, must buy 3, so 100 is not enough, set account so
		plyr1.withdawMoney(plyr1.getAccount() - 100);
		int plyr1AccountBefore = plyr1.getAccount();
		gameClient.buyHotelRow(6, sendNetMessage);
		int plyr1AccountAfter = plyr1.getAccount();
		assertTrue(plyr1AccountAfter == plyr1AccountBefore);
		// check that all terrain still ahve no houses
		assertTrue(terrainConnecticut.getHouseCount() == 4
				&& terrainOriental.getHouseCount() == 4
				&& terrainVermont.getHouseCount() == 4);
	}

	//
	// /**
	// * test that buyHotelRow doesn't allow construction if the currentPlayer
	// does not own all properties
	// */
	// @Test
	// public void buyHotelRowNotDoesntOwnAllProperties() throws
	// TransactionException {
	// int oriental=6;
	// int vermont=8;
	// int connecticut=9;
	// String player1name = "Justin";
	// Player plyr1 = board.getPlayerByName("Justin");
	// gameClient.setCurrentPlayer(plyr1, sendNetMessage);
	// gameClient.advancePlayerNSpaces(6, sendNetMessage);
	// gameClient.buyCurrentPropertyForPlayer(player1name, sendNetMessage);
	// gameClient.advancePlayerNSpaces(2, sendNetMessage);
	// gameClient.buyCurrentPropertyForPlayer(player1name, sendNetMessage);
	// //house cost is 50, must buy 3, so 100 is not enough, set account so
	// Tile t1 = board.getTileById(oriental);
	// Terrain terrainOriental = board.castTileToTerrain(t1);
	// Tile t2 = board.getTileById(vermont);
	// Terrain terrainVermont = board.castTileToTerrain(t2);
	// Tile t3 = board.getTileById(connecticut);
	// Terrain terrainConnecticut = board.castTileToTerrain(t3);
	// int plyr1AccountBefore = plyr1.getAccount();
	// gameClient.buyHouseRow(6, sendNetMessage);
	// int plyr1AccountAfter = plyr1.getAccount();
	// System.out.println(plyr1AccountAfter +"  and before was:"+
	// plyr1AccountBefore);
	// // assertTrue(plyr1AccountAfter==plyr1AccountBefore);
	// //check that all terrain still ahve no houses
	// assertTrue(terrainConnecticut.getHouseCount()==0
	// &&terrainOriental.getHouseCount()==0
	// &&terrainVermont.getHouseCount()==0);
	// }
	//
	// /**
	// * test that buyHotelRow builds 1 house on each property when all
	// conditions are met, and withdraws the correct amount
	// */
	// @Test
	// public void buyHotelRowBuildsHotels() throws TransactionException {
	// int oriental=6;
	// int vermont=8;
	// int connecticut=9;
	// String player1name = "Justin";
	// Player plyr1 = board.getPlayerByName("Justin");
	// gameClient.setCurrentPlayer(plyr1, sendNetMessage);
	// gameClient.advancePlayerNSpaces(6, sendNetMessage);
	// gameClient.buyCurrentPropertyForPlayer(player1name, sendNetMessage);
	// gameClient.advancePlayerNSpaces(2, sendNetMessage);
	// gameClient.buyCurrentPropertyForPlayer(player1name, sendNetMessage);
	// gameClient.advancePlayerNSpaces(1, sendNetMessage);
	// gameClient.buyCurrentPropertyForPlayer(player1name, sendNetMessage);
	// //house cost is 50, must buy 3, so 100 is not enough, set account so
	// Tile t1 = board.getTileById(oriental);
	// Terrain terrainOriental = board.castTileToTerrain(t1);
	// Tile t2 = board.getTileById(vermont);
	// Terrain terrainVermont = board.castTileToTerrain(t2);
	// Tile t3 = board.getTileById(connecticut);
	// Terrain terrainConnecticut = board.castTileToTerrain(t3);
	// int plyr1AccountBefore = plyr1.getAccount();
	// gameClient.buyHouseRow(6, sendNetMessage);
	// int plyr1AccountAfter = plyr1.getAccount();
	// assertTrue(plyr1AccountAfter==plyr1AccountBefore-150);
	// assertTrue(terrainConnecticut.getHouseCount()==1
	// &&terrainOriental.getHouseCount()==1
	// &&terrainVermont.getHouseCount()==1);
	// gameClient.buyHouseRow(6, sendNetMessage);
	// plyr1AccountAfter = plyr1.getAccount();
	// assertTrue(plyr1AccountAfter==plyr1AccountBefore-300);
	// assertTrue(terrainConnecticut.getHouseCount()==2
	// &&terrainOriental.getHouseCount()==2
	// &&terrainVermont.getHouseCount()==2);
	// //can't build more than 4 houses
	// gameClient.buyHouseRow(6, sendNetMessage);
	// gameClient.buyHouseRow(6, sendNetMessage);
	// gameClient.buyHouseRow(6, sendNetMessage);
	// plyr1AccountAfter = plyr1.getAccount();
	// assertTrue(plyr1AccountAfter==plyr1AccountBefore-600);
	// assertTrue(terrainConnecticut.getHouseCount()==4
	// &&terrainOriental.getHouseCount()==4
	// &&terrainVermont.getHouseCount()==4);
	// }

	/**
	 * test that the method buyCurrentPropertyForPlayer updates the change in
	 * the player's properties list
	 */
	@Test
	public void buyCurrentPropertyForPlayerChangesPlayersPropertyList() {
		Player plyr = board.getPlayerByName("Justin");
		gameClient.setCurrentPlayer(plyr, sendNetMessage);
		// add money to player's account to loop is possible
		plyr.depositMoney(100000);
		for (int i = 0; i < terrainPositions.length; i++) {
			int tileId = terrainPositions[i];
			plyr.setPosition(tileId);
			gameClient.buyCurrentPropertyForPlayer("Justin", sendNetMessage);
			assertTrue(plyr.ownsProperty(board.getTileById(tileId)));
		}
	}

	/**
	 * test that the method buyCurrentPropertyForPlayer sets the ownership of
	 * the tile to the buying player
	 */
	@Test
	public void buyCurrentPropertyForPlayerSetsTilesOwner() {
		Player plyr = board.getPlayerByName("Justin");
		gameClient.setCurrentPlayer(plyr, sendNetMessage);
		gameClient.advancePlayerNSpaces(1, sendNetMessage);
		gameClient.buyCurrentPropertyForPlayer("Justin", false);
		Property prop = (Property) board.getTileById(1);
		assertTrue(prop.getOwner().getName().equals("Justin"));
	}

	/**
	 * test that the method buyCurrentPropertyForPlayer sets the ownership of
	 * the tile to the buying player, the player name is "CurRentPlaYER" which
	 * can be used as a string parameter to carry out out the action for the
	 * currentPlayer
	 */
	@Test
	public void buyCurrentPropertyForPlayerSetsTilesOwnerCurrentPlayer() {
		Player plyr = board.getPlayerByName("Justin");
		gameClient.setCurrentPlayer(plyr, true);
		gameClient.advancePlayerNSpaces(1, sendNetMessage);
		gameClient.buyCurrentPropertyForPlayer("CurRentPlaYER", sendNetMessage);
		Property prop = (Property) board.getTileById(1);
		assertTrue(prop.getOwner().getName().equals("Justin"));
	}

	/**
	 * test that the method buyCurrentPropertyForPlayer transfers the money out
	 * of the buyer's account
	 */
	@Test
	public void buyCurrentPropertyForPlayerTransfersMoney() {
		Player plyr = board.getPlayerByName("Justin");
		int plyrAccountBefore = plyr.getAccount();
		Property prop = (Property) board.getTileById(1);
		int tilePrice = prop.getPrice();
		gameClient.setCurrentPlayer(plyr, sendNetMessage);
		gameClient.advancePlayerNSpaces(1, sendNetMessage);
		gameClient.buyCurrentPropertyForPlayer("Justin", sendNetMessage);
		assertTrue(plyr.getAccount() == plyrAccountBefore - tilePrice);
	}

	/**
	 * test that it is not possible to build more than four houses on a given
	 * tile
	 */
	@Test
	public void buildHousesCannotBuildMoreThanFour() {
		Player plyr = board.getPlayerByName("Justin");
		Tile t = board.getTileById(1);
		Terrain terrain = board.castTileToTerrain(t);
		gameClient.setCurrentPlayer(plyr, sendNetMessage);
		gameClient.advancePlayerNSpaces(1, sendNetMessage);
		gameClient.buyCurrentPropertyForPlayer("Justin", sendNetMessage);
		gameClient.advancePlayerNSpaces(2, sendNetMessage);
		gameClient.buyCurrentPropertyForPlayer("Justin", sendNetMessage);
		gameClient.buyHouse(1, sendNetMessage);
		gameClient.buyHouse(1, sendNetMessage);
		gameClient.buyHouse(1, sendNetMessage);
		gameClient.buyHouse(1, sendNetMessage);
		int accountBefore = plyr.getAccount();
		gameClient.buyHouse(1, sendNetMessage);
		int accountAfter = plyr.getAccount();
		// houses should still be 4
		assertTrue(terrain.getHouseCount() == 4);
		// no money should have changed hands
		assertTrue(accountBefore == accountAfter);
	}

	/**
	 * test that it is not possible to build houses if the player does not have
	 * enough money, and that the states aren't change in such a case tile
	 * 
	 * @throws TransactionException
	 */
	@Test
	public void buildHousesNotEnoughMoney() throws TransactionException {
		Player plyr = board.getPlayerByName("Justin");
		Tile t = board.getTileById(1);
		Terrain terrain = board.castTileToTerrain(t);
		gameClient.setCurrentPlayer(plyr, sendNetMessage);
		gameClient.advancePlayerNSpaces(1, sendNetMessage);
		gameClient.buyCurrentPropertyForPlayer("Justin", sendNetMessage);
		gameClient.advancePlayerNSpaces(2, sendNetMessage);
		gameClient.buyCurrentPropertyForPlayer("Justin", sendNetMessage);
		// house cost for mediterranean is 50, leave enough to build 2 houses
		plyr.withdawMoney(plyr.getAccount() - 100);
		gameClient.buyHouse(1, sendNetMessage);
		gameClient.buyHouse(1, sendNetMessage);
		// the following houses should not be able to be built
		int accountBefore = plyr.getAccount();
		gameClient.buyHouse(1, sendNetMessage);
		gameClient.buyHouse(1, sendNetMessage);
		int accountAfter = plyr.getAccount();
		// houses should still be 4
		assertTrue(terrain.getHouseCount() == 2);
		// no money should have changed hands
		assertTrue(accountBefore == accountAfter);
	}

	/**
	 * test that it is not possible to build hotels if the player does not have
	 * enough money, and that the states aren't change in such a case tile
	 * 
	 * @throws TransactionException
	 */
	@Test
	public void buildHotelsNotEnoughMoney() throws TransactionException {
		Player plyr = board.getPlayerByName("Justin");
		Tile t = board.getTileById(1);
		Terrain terrain = board.castTileToTerrain(t);
		gameClient.setCurrentPlayer(plyr, sendNetMessage);
		gameClient.advancePlayerNSpaces(1, sendNetMessage);
		gameClient.buyCurrentPropertyForPlayer("Justin", sendNetMessage);
		// house cost for mediterranean is 50, leave enough to build 2 houses
		gameClient.buyHouse(1, sendNetMessage);
		gameClient.buyHouse(1, sendNetMessage);
		gameClient.buyHouse(1, sendNetMessage);
		gameClient.buyHouse(1, sendNetMessage);
		// the following hotel should not be able to be built
		plyr.withdawMoney(plyr.getAccount() - 10);
		int accountBefore = plyr.getAccount();
		gameClient.buyHotel(1, sendNetMessage);
		int accountAfter = plyr.getAccount();
		// houses should still be 4
		assertTrue(terrain.getHotelCount() == 0);
		// no money should have changed hands
		assertTrue(accountBefore == accountAfter);
	}

	/**
	 * test that it is not possible to build more than one hotel on a given tile
	 */
	@Test
	public void buildHotelCannotBuildMoreThanOne() {
		int tileId = 1;
		Tile t = board.getTileById(1);
		Terrain terrain = board.castTileToTerrain(t);
		Player plyr = board.getPlayerByName("Justin");
		gameClient.setCurrentPlayer(plyr, sendNetMessage);
		gameClient.advancePlayerNSpaces(tileId, sendNetMessage);
		gameClient.buyCurrentPropertyForPlayer("Justin", sendNetMessage);
		gameClient.advancePlayerNSpaces(2, sendNetMessage);
		gameClient.buyCurrentPropertyForPlayer("Justin", sendNetMessage);
		gameClient.buyHouse(tileId, sendNetMessage);
		gameClient.buyHouse(tileId, sendNetMessage);
		gameClient.buyHouse(tileId, sendNetMessage);
		gameClient.buyHouse(tileId, sendNetMessage);
		gameClient.buyHotel(tileId, sendNetMessage);
		int accountBefore = plyr.getAccount();
		gameClient.buyHotel(tileId, sendNetMessage);
		int accountAfter = plyr.getAccount();
		// houses should still be 4
		assertTrue(terrain.getHotelCount() == 1);
		// no money should have changed hands
		assertTrue(accountBefore == accountAfter);
	}

	/**
	 * test that a hotel cannot be built unless there are 4 houses present on
	 * the given tile
	 */
	@Test
	public void buildHotelCannotBuildHotelUnlessFourHousesPresent() {
		int tileId = 1;
		Tile t = board.getTileById(1);
		Terrain terrain = board.castTileToTerrain(t);
		Player plyr = board.getPlayerByName("Justin");
		gameClient.setCurrentPlayer(plyr, sendNetMessage);
		gameClient.advancePlayerNSpaces(tileId, sendNetMessage);
		gameClient.buyCurrentPropertyForPlayer("Justin", sendNetMessage);
		gameClient.buyHouse(tileId, sendNetMessage);
		gameClient.buyHouse(tileId, sendNetMessage);
		gameClient.buyHouse(tileId, sendNetMessage);
		int accountBefore = plyr.getAccount();
		gameClient.buyHotel(tileId, sendNetMessage);
		int accountAfter = plyr.getAccount();
		// houses should still be 4
		assertTrue(terrain.getHotelCount() == 0);
		// no money should have changed hands
		assertTrue(accountBefore == accountAfter);
	}

	/**
	 * tests that the sale of a hotel cannot be completed if there are no hotels
	 * to sell on the given property
	 */
	@Test
	public void sellHotelCannotSellUnlessHotelPresent() {
		int tileId = 1;
		Tile t = board.getTileById(tileId);
		Terrain terrain = board.castTileToTerrain(t);
		Player plyr = board.getPlayerByName("Justin");
		gameClient.setCurrentPlayer(plyr, sendNetMessage);
		gameClient.advancePlayerNSpaces(tileId, sendNetMessage);
		gameClient.buyCurrentPropertyForPlayer("Justin", sendNetMessage);
		int accountBefore = plyr.getAccount();
		gameClient.sellHotel(tileId, sendNetMessage);
		int accountAfter = plyr.getAccount();
		// houses should still be 0
		assertTrue(terrain.getHotelCount() == 0);
		// no money should have changed hands
		assertTrue(accountBefore == accountAfter);
	}

	/**
	 * tests that the sale of a hotel decreases the hotel count
	 */
	@Test
	public void sellHotelCannotSellUnlessOwner() {
		int tileId = 1;
		Tile t = board.getTileById(tileId);
		Terrain terrain = board.castTileToTerrain(t);
		Player plyr = board.getPlayerByName("Justin");
		// buy the mediterranean and baltic for Justin
		gameClient.setCurrentPlayer(plyr, sendNetMessage);
		gameClient.advancePlayerNSpaces(tileId, sendNetMessage);
		gameClient.buyCurrentPropertyForPlayer("Justin", sendNetMessage);
		gameClient.advancePlayerNSpaces(2, sendNetMessage);
		gameClient.buyCurrentPropertyForPlayer("Justin", sendNetMessage);
		// build hotel on the properties
		for (int i = 0; i < 4; i++) {
			gameClient.buyHouse(tileId, sendNetMessage);
			gameClient.buyHouse(3, sendNetMessage);
		}
		gameClient.buyHotel(tileId, sendNetMessage);
		assertTrue(terrain.getHotelCount() == 1);
		int accountBefore = plyr.getAccount();
		gameClient.sellHotel(tileId, sendNetMessage);
		int accountAfter = plyr.getAccount();
		// houses should still be 0
		assertTrue(terrain.getHotelCount() == 0);
		// no money should have changed hands
		assertTrue(accountBefore == accountAfter-terrain.getHotelCost());
	}
	
	
	/**
	 * tests that another player can't sell hotels from a property he doesn't own
	 */
	@Test
	public void sellHotelWorksOnlyForOwnerOfTile() {
		int tileId = 1;
		Tile t = board.getTileById(tileId);
		Terrain terrain = board.castTileToTerrain(t);
		Player plyr1 = board.getPlayerByName("Justin");
		// buy the mediterranean and baltic for Justin
		gameClient.setCurrentPlayer(plyr1, sendNetMessage);
		gameClient.advancePlayerNSpaces(tileId, sendNetMessage);
		gameClient.buyCurrentPropertyForPlayer("Justin", sendNetMessage);
		gameClient.advancePlayerNSpaces(2, sendNetMessage);
		gameClient.buyCurrentPropertyForPlayer("Justin", sendNetMessage);
		// build hotel on the properties
		for (int i = 0; i < 4; i++) {
			gameClient.buyHouse(tileId, sendNetMessage);
			gameClient.buyHouse(3, sendNetMessage);
		}
		gameClient.buyHotel(tileId, sendNetMessage);
		Player plyr2 = board.getPlayerByName("Giuseppe");
		gameClient.setCurrentPlayer(plyr2, sendNetMessage);
		int player1accountBefore = plyr1.getAccount();
		int player2accountBefore = plyr2.getAccount();
		gameClient.sellHotel(tileId, sendNetMessage);
		int player1accountAfter = plyr1.getAccount();
		int player2accountAfter = plyr2.getAccount();
		// houses should still be 0
		assertTrue(terrain.getHotelCount() == 1);
		// no money should have changed hands
		assertTrue(player1accountBefore == player1accountAfter);
		assertTrue(player2accountBefore == player2accountAfter);	
	}
	

	/**
	 * tests that the sale of a house cannot be completed if there are no houses
	 * to sell on the given property
	 */
	@Test
	public void sellHouseCannotSellUnlessHousePresent() {
		int tileId = 1;
		Tile t = board.getTileById(1);
		Terrain terrain = board.castTileToTerrain(t);
		Player plyr = board.getPlayerByName("Justin");
		gameClient.setCurrentPlayer(plyr, sendNetMessage);
		gameClient.advancePlayerNSpaces(tileId, sendNetMessage);
		gameClient.buyCurrentPropertyForPlayer("Justin", sendNetMessage);
		int accountBefore = plyr.getAccount();
		gameClient.sellHouse(tileId, sendNetMessage);
		int accountAfter = plyr.getAccount();
		// houses should still be 0
		assertTrue(terrain.getHotelCount() == 0);
		// no money should have changed hands
		assertTrue(accountBefore == accountAfter);
	}

	/**
	 * test that the method sellHouses correctly deposits money into the
	 * player's account
	 */
	@Test
	public void sellHouseDepositsMoneyToPlayerAccount() {
		int tileId = 1;
		Player plyr = board.getPlayerByName("Justin");
		Terrain terrain = (Terrain) board.getTileById(1);
		gameClient.setCurrentPlayer(plyr, sendNetMessage);
		gameClient.advancePlayerNSpaces(tileId, sendNetMessage);
		gameClient.buyCurrentPropertyForPlayer("Justin", sendNetMessage);
		gameClient.advancePlayerNSpaces(2, sendNetMessage);
		gameClient.buyCurrentPropertyForPlayer("Justin", sendNetMessage);
		gameClient.buyHouse(tileId, sendNetMessage);
		int accountBefore = plyr.getAccount();
		int houseCost = terrain.getHouseCost();
		gameClient.sellHouse(tileId, sendNetMessage);
		assertTrue(plyr.getAccount() == accountBefore + houseCost);
	}

	/**
	 * test that the method sellHotels correctly deposits money into the
	 * player's account
	 */
	@Test
	public void sellHotelsDepositsMoneyToPlayerAccount() {
		int tileId = 1;
		Player plyr = board.getPlayerByName("Justin");
		Terrain terrain = (Terrain) board.getTileById(1);
		gameClient.setCurrentPlayer(plyr, sendNetMessage);
		gameClient.advancePlayerNSpaces(tileId, sendNetMessage);
		gameClient.buyCurrentPropertyForPlayer("Justin", sendNetMessage);
		gameClient.advancePlayerNSpaces(2, sendNetMessage);
		gameClient.buyCurrentPropertyForPlayer("Justin", sendNetMessage);
		gameClient.buyHouse(tileId, sendNetMessage);
		gameClient.buyHouse(tileId, sendNetMessage);
		gameClient.buyHouse(tileId, sendNetMessage);
		gameClient.buyHouse(tileId, sendNetMessage);
		gameClient.buyHotel(tileId, sendNetMessage);
		int accountBefore = plyr.getAccount();
		int houseCost = terrain.getHotelCost();
		gameClient.sellHotel(tileId, sendNetMessage);
		assertTrue(plyr.getAccount() == accountBefore + houseCost);
	}

	/**
	 * check that the method toggleMortgageStatus toggles the status of the
	 * boolean field mortgageActive in objects of the Property class
	 */
	@Test
	public void toggleMortgageStatusChangesMortgageStatus() {
		int tileId = 1;
		Player plyr = board.getPlayerByName("Justin");
		Terrain terrain = (Terrain) board.getTileById(1);
		gameClient.setCurrentPlayer(plyr, sendNetMessage);
		gameClient.advancePlayerNSpaces(tileId, sendNetMessage);
		gameClient.buyCurrentPropertyForPlayer("Justin", sendNetMessage);
		// issendNetMessage after creation, will be set to true
		gameClient.toggleMortgageStatus(tileId, sendNetMessage);
		assertTrue(terrain.isMortgageActive());
		// And once more, just for fun
		gameClient.toggleMortgageStatus(tileId, sendNetMessage);
		assertTrue(!terrain.isMortgageActive());
	}

	/**
	 * test that if you mortgage a property the money is transfered to your
	 * account
	 */
	@Test
	public void toggleMortgageStatusDepositsMoney() {
		int tileId = 1;
		Player plyr = board.getPlayerByName("Justin");
		Terrain terrain = (Terrain) board.getTileById(1);
		gameClient.setCurrentPlayer(plyr, sendNetMessage);
		gameClient.advancePlayerNSpaces(tileId, sendNetMessage);
		gameClient.buyCurrentPropertyForPlayer("Justin", sendNetMessage);
		int accountBefore = plyr.getAccount();
		// issendNetMessage after creation, will be set to true
		gameClient.toggleMortgageStatus(tileId, sendNetMessage);
		int accountNow = plyr.getAccount();
		assertTrue(accountNow == (accountBefore + terrain.getMortgageValue()));
	}

	/**
	 * test that a the method transferProperty correctly adjusts the owner field
	 * in the object of type Property
	 */
	@Test
	public void transferPropertyChangesOwnership() {
		int tileId = 1;
		Player plyr1 = board.getPlayerByName("Justin");
		Player plyr2 = board.getPlayerByName("Giuseppe");
		Terrain terrain = (Terrain) board.getTileById(1);
		gameClient.setCurrentPlayer(plyr1, sendNetMessage);
		gameClient.advancePlayerNSpaces(tileId, sendNetMessage);
		gameClient.buyCurrentPropertyForPlayer("Justin", sendNetMessage);
		assertTrue(terrain.getOwner().getName().equals(plyr1.getName()));
		gameClient.transferProperty(plyr1.getName(), plyr2.getName(), tileId,
				0, sendNetMessage);
		assertTrue(terrain.getOwner().getName().equals(plyr2.getName()));
	}

	/**
	 * test that a the method transferProperty correctly deposits money to the
	 * seller's account
	 */
	@Test
	public void transferPropertyDepositsMoneyToSeller() {
		int price = 300;
		int tileId = 1;
		Player plyr1 = board.getPlayerByName("Justin");
		Player plyr2 = board.getPlayerByName("Giuseppe");
		Terrain terrain = (Terrain) board.getTileById(1);
		gameClient.setCurrentPlayer(plyr1, sendNetMessage);
		gameClient.advancePlayerNSpaces(tileId, sendNetMessage);
		gameClient.buyCurrentPropertyForPlayer("Justin", sendNetMessage);
		int plyr1AccountBefore = plyr1.getAccount();
		int plyr2AccountBefore = plyr2.getAccount();
		gameClient.transferProperty(plyr1.getName(), plyr2.getName(), tileId,
				price, sendNetMessage);
		assertTrue(plyr1.getAccount() == plyr1AccountBefore + price);
	}

	/**
	 * test that a the method transferProperty correctly withdraws money from
	 * the buyer's account
	 */
	@Test
	public void transferPropertyWithdrawsMoneyFromBuyer() {
		int price = 300;
		int tileId = 1;
		Player plyr1 = board.getPlayerByName("Justin");
		Player plyr2 = board.getPlayerByName("Giuseppe");
		Terrain terrain = (Terrain) board.getTileById(1);
		gameClient.setCurrentPlayer(plyr1, sendNetMessage);
		gameClient.advancePlayerNSpaces(tileId, sendNetMessage);
		gameClient.buyCurrentPropertyForPlayer("Justin", sendNetMessage);
		int plyr1AccountBefore = plyr1.getAccount();
		int plyr2AccountBefore = plyr2.getAccount();
		gameClient.transferProperty(plyr1.getName(), plyr2.getName(), tileId,
				price, sendNetMessage);
		assertTrue(plyr2.getAccount() == plyr2AccountBefore - price);
	}

	/**
	 * test that a the method transferProperty correctly adds the property to
	 * the new owner's property list
	 */

	public void transferPropertyAddsPropertyToPropertyList(String player1name) {
		int price = 300;
		int tileId = 1;
		Player plyr1 = board.getPlayerByName(player1name);
		Player plyr2 = board.getPlayerByName("Giuseppe");
		Terrain terrain = (Terrain) board.getTileById(1);
		gameClient.setCurrentPlayer(plyr1, sendNetMessage);
		gameClient.advancePlayerNSpaces(tileId, sendNetMessage);
		gameClient.buyCurrentPropertyForPlayer(player1name, sendNetMessage);
		gameClient.transferProperty(plyr1.getName(), plyr2.getName(), tileId,
				price, sendNetMessage);
		assertTrue(plyr2.ownsProperty(terrain));
	}

	/**
	 * tests that when a transfer is initiated with a parameter containing the
	 * string "currentPlayer" that everything still works
	 */
	@Test
	public void transferPropertyWithStringParamCurrentPlayer() {
		int price = 300;
		int tileId = 3;
		Player plyr1 = board.getPlayerByName("Justin");
		Player plyr2 = board.getPlayerByName("Giuseppe");
		Terrain terrain = (Terrain) board.getTileById(tileId);
		gameClient.setCurrentPlayer(plyr1, sendNetMessage);
		gameClient.advancePlayerNSpaces(tileId, sendNetMessage);
		gameClient.buyCurrentPropertyForPlayer("Justin", sendNetMessage);
		assertTrue(plyr1.ownsProperty(terrain));
		gameClient.transferProperty("curReNTPlayeR", plyr2.getName(), tileId,
				price, sendNetMessage);
		assertTrue(plyr2.ownsProperty(terrain));
	}

	/**
	 * tests that when a jail card transfers from one player to another
	 */
	@Test
	public void transferJailCardsChangesCards() {
		String player1name = "Justin";
		String player2name = "Giuseppe";
		Player plyr1 = board.getPlayerByName("Justin");
		Player plyr2 = board.getPlayerByName("Giuseppe");
		gameClient.setCurrentPlayer(plyr1, sendNetMessage);
		plyr1.setJailCard(1);
		gameClient.transferJailCards(player1name, player2name, 1, 0,
				sendNetMessage);
		assertTrue(plyr1.getJailCard() == 0 && plyr2.getJailCard() == 1);
	}

	/**
	 * tests that when a jail card transfers from one player to another
	 */
	@Test
	public void transferJailCardsTransfersMoney() {
		int price = 100;
		String player1name = "Justin";
		String player2name = "Giuseppe";
		Player plyr1 = board.getPlayerByName("Justin");
		Player plyr2 = board.getPlayerByName("Giuseppe");
		int plyr1AccountBefore = plyr1.getAccount();
		int plyr2AccountBefore = plyr2.getAccount();
		plyr1.setJailCard(1);
		gameClient.transferJailCards(player1name, player2name, 1, price,
				sendNetMessage);
		assertTrue(plyr1.getAccount() == plyr1AccountBefore + price);
		assertTrue(plyr2.getAccount() == plyr2AccountBefore - price);
	}

	/**
	 * tests that you can't transfer more jail cards than you have
	 */
	@Test
	public void transferJailCardsNoMoreThanAvailable() {
		int price = 100;
		String player1name = "Justin";
		String player2name = "Giuseppe";
		Player plyr1 = board.getPlayerByName("Justin");
		Player plyr2 = board.getPlayerByName("Giuseppe");
		assertTrue(plyr1.getJailCard() == 0);
		int jailsCardsBeforePlyr1 = plyr1.getJailCard();
		int jailsCardsBeforePlyr2 = plyr2.getJailCard();
		gameClient.transferJailCards(player1name, player2name, 1, price,
				sendNetMessage);
		int jailsCardsAfterPlyr1 = plyr1.getJailCard();
		int jailsCardsAfterPlyr2 = plyr2.getJailCard();
		assertTrue(jailsCardsAfterPlyr1 == jailsCardsBeforePlyr1);
		assertTrue(jailsCardsAfterPlyr2 == jailsCardsBeforePlyr2);
	}

	/**
	 * tests the method in gameController.transferMoney(...)
	 */
	@Test
	public void transferMoney() {
		int amount = 100;
		String player1name = "Justin";
		String player2name = "Giuseppe";
		Player plyr1 = board.getPlayerByName(player1name);
		Player plyr2 = board.getPlayerByName(player2name);
		int plyr1AccountBefore = plyr1.getAccount();
		int plyr2AccountBefore = plyr2.getAccount();
		gameClient.transferMoney(player1name, player2name, amount,
				sendNetMessage);
		assertTrue(plyr1.getAccount() == plyr1AccountBefore - amount);
		assertTrue(plyr2.getAccount() == plyr2AccountBefore + amount);
	}

	/**
	 * tests the method in gameController.transferMoney(...) use the string
	 * "CurRenTPlaYEr" to make sure the tag resolves to the appropriate name
	 */
	@Test
	public void transferMoneyCurrentPlayerAsFromPlayer() {
		int amount = 100;
		String player1name = "Justin";
		String player2name = "Giuseppe";
		Player plyr1 = board.getPlayerByName(player1name);
		Player plyr2 = board.getPlayerByName(player2name);
		gameClient.setCurrentPlayer(plyr1, sendNetMessage);
		int plyr1AccountBefore = plyr1.getAccount();
		int plyr2AccountBefore = plyr2.getAccount();
		gameClient.transferMoney("CurRenTPlaYEr", player2name, amount,
				sendNetMessage);
		assertTrue(plyr1.getAccount() == plyr1AccountBefore - amount);
		assertTrue(plyr2.getAccount() == plyr2AccountBefore + amount);
	}

	/**
	 * tests the method in gameController.transferMoney(...) use the string
	 * "CurRenTPlaYEr" to make sure the tag resolves to the appropriate name
	 */
	@Test
	public void transferMoneyCurrentPlayerAsToPlayer() {
		int amount = 100;
		String player1name = "Justin";
		String player2name = "Giuseppe";
		Player plyr1 = board.getPlayerByName(player1name);
		Player plyr2 = board.getPlayerByName(player2name);
		gameClient.setCurrentPlayer(plyr1, sendNetMessage);
		int plyr1AccountBefore = plyr1.getAccount();
		int plyr2AccountBefore = plyr2.getAccount();
		gameClient.transferMoney(player2name, "CurRenTPlaYEr", amount,
				sendNetMessage);
		assertTrue(plyr1.getAccount() == plyr1AccountBefore + amount);
		assertTrue(plyr2.getAccount() == plyr2AccountBefore - amount);
	}
	
	
	/**
	 * tests the method in gameController.transferMoney(...) using the bank
	 * and the currentPlayer
	 */
	@Test
	public void transferMoneyFromBank() {
		int amount = 100;
		String player1name = "Justin";
		String playerBank = "Bank";
		Player plyr1 = board.getPlayerByName(player1name);
		Player plyrBank = board.getPlayerByName(playerBank);
		gameClient.setCurrentPlayer(plyr1, sendNetMessage);
		int plyr1AccountBefore = plyr1.getAccount();
		int plyr2AccountBefore = plyrBank.getAccount();
		gameClient.transferMoney(playerBank, "CurRenTPlaYEr", amount,
				sendNetMessage);
		assertTrue(plyr1.getAccount() == plyr1AccountBefore + amount);
		assertTrue(plyrBank.getAccount() == plyr2AccountBefore - amount);
	}

	/**
	 * test that the game controller can advance the current player a given
	 * number of spaces in the game board
	 */
	@Test
	public void canAdvancedPlayerNSpaces() {
		String player1name = "Justin";
		Player plyr1 = board.getPlayerByName(player1name);
		gameClient.setCurrentPlayer(plyr1, sendNetMessage);
		gameClient.advancePlayerNSpaces(3, sendNetMessage);
		assertTrue(gameClient.getCurrentPlayer().getPosition() == 3);
		// check that modulo is working
		gameClient.advancePlayerNSpaces(40, sendNetMessage);
		assertTrue(gameClient.getCurrentPlayer().getPosition() == 3);
	}

	/**
	 * win jail card adds jail card to player
	 */
	@Test
	public void winJailCardAddsCard() {
		String player1name = "Justin";
		Player plyr1 = board.getPlayerByName(player1name);
		gameClient.setCurrentPlayer(plyr1, sendNetMessage);
		gameClient.addJailCardToPlayer(player1name, sendNetMessage);
		int jailCardCount = plyr1.getJailCard();
		assertTrue(jailCardCount == 1);
	}
	
	/**
	 * win jail card adds jail card to player
	 */
	@Test
	public void getOutOfJailUsesCards() {
		String player1name = "Justin";
		Player plyr1 = board.getPlayerByName(player1name);
		gameClient.setCurrentPlayer(plyr1, sendNetMessage);
		gameClient.addJailCardToPlayer(player1name, sendNetMessage);
		int jailCardCountBefore = plyr1.getJailCard();
		gameClient.goToJail(sendNetMessage);
		assertTrue(jailCardCountBefore==1);
		System.out.println(jailCardCountBefore);
		gameClient.getOutOfJailByCard(sendNetMessage);
		int jailCardCounAfter = plyr1.getJailCard();
		System.out.println(jailCardCounAfter);
		assertTrue(jailCardCounAfter==0);
	}
	
	/**
	 * test that landing on GO TO JAIL send the player to jail and changes
	 * inJail boolean status
	 */
	@Test
	public void goToJailSendsPlayerToJail() {
		Player plyr = board.getPlayerByName("Justin");
		gameClient.setCurrentPlayer(plyr,sendNetMessage);
		gameClient.advancePlayerNSpaces(8, sendNetMessage);
		gameClient.goToJail(sendNetMessage);
		assertTrue(plyr.isInJail());
		assertTrue(plyr.getPosition() == 10);
	}

	/**
	 * check that the method gameClient.buyCurrentPropertyForPlayer deducts the
	 * money from the players account correctly
	 */
	@Test
	public void buyPropertyFromBankWithdrawsCorrectly() {
		int tileId = 39;
		Player p = board.getPlayerByName("Justin");
		int previousBalance = p.getAccount();
		gameClient.setCurrentPlayer(p, sendNetMessage);
		gameClient.advancePlayerNSpaces(39, sendNetMessage);
		gameClient.buyCurrentPropertyForPlayer(p.getName(), sendNetMessage);
		Tile t = board.getTileById(tileId);
		Property prop = (Property) t;
		int priceOfProperty = prop.getPrice();
		int newBalance = p.getAccount();
		assertTrue(newBalance == (previousBalance - priceOfProperty));
	}

	/**
	 * check that a player cannot buy a property from the bank, unless he has
	 * enough money
	 * 
	 * @throws TransactionException
	 */
	@Test
	public void propertyFromBankInsufficientFunds() throws TransactionException {
		int tileId = 39;
		Player plyr = board.getPlayerByName("Justin");
		plyr.withdawMoney(plyr.getAccount() - 1);
		gameClient.setCurrentPlayer(plyr, sendNetMessage);
		gameClient.advancePlayerNSpaces(tileId, sendNetMessage);
		int plyrAccountBefore = plyr.getAccount();
		gameClient.buyCurrentPropertyForPlayer(plyr.getName(), sendNetMessage);
		int plyrAccountAfter = plyr.getAccount();
		assertTrue(plyrAccountAfter == plyrAccountBefore);

	}

	/**
	 * the method payFee deducts the correct amount from the current player's
	 * account
	 */
	@Test
	public void payFeeDeductsMoneyFromCurrentPlayer() {
		String playerName = "Justin";
		Player plyr = board.getPlayerByName(playerName);
		int playerAccountBefore = plyr.getAccount();
		gameClient.setCurrentPlayer(plyr, sendNetMessage);
		int fee = 100;
		gameClient.payFee(fee, sendNetMessage);
		assertTrue(plyr.getAccount() == playerAccountBefore - fee);
	}

	/**
	 * the method payFee() credits the amount of the fee to the variable
	 * freeParking
	 */
	@Test
	public void payFeeCreditsFeeToFreeParking() {
		String playerName1 = "Justin";
		Player plyr1 = board.getPlayerByName(playerName1);
		gameClient.setCurrentPlayer(plyr1, sendNetMessage);
		int fee = 100;
		gameClient.payFee(fee, sendNetMessage);
		String playerName2 = "Giuseppe";
		Player plyr2 = board.getPlayerByName(playerName2);
		gameClient.setCurrentPlayer(plyr2, sendNetMessage);
		int plyr2accountBefore = plyr2.getAccount();
		gameClient.freeParking(sendNetMessage);
		int plyr2accountAfter = plyr2.getAccount();
		//we start with 500 in the free parking account
		assertTrue(plyr2accountAfter == plyr2accountBefore + fee + 500);
	}

	/**
	 * check that a player cannot buy a property from the bank, unless he has
	 * enough money
	 * 
	 * @throws TransactionException
	 *             if there isn't enough money in the account, this is thrown
	 */
	@Test
	public void cantBuyPropertyWithInsufficientFunds()
			throws TransactionException {
		int tileId = 39;
		Player p = board.getPlayerByName("Justin");
		p.withdawMoney(p.getAccount() - 1);
		gameClient.setCurrentPlayer(p, sendNetMessage);
		gameClient.advancePlayerNSpaces(tileId, sendNetMessage);
		int plyrAccountBefore = p.getAccount();
		gameClient.buyCurrentPropertyForPlayer(p.getName(), sendNetMessage);
		int plyrAccountAfter = p.getAccount();
		assertTrue(plyrAccountAfter == plyrAccountBefore);
	}

}
