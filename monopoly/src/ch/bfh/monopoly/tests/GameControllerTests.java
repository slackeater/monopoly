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
	Locale loc;
	GameClient gameClient;
	Board board;
	GameController gc;
	int[] terrainPositions = { 1, 3, 6, 8, 9, 11, 13, 14, 16, 18, 19, 21, 23,
			24, 26, 27, 29, 31, 32, 34, 37, 39 };

	@Before
	public void setup() {
		TestInstanceGenerator tig = new TestInstanceGenerator();
		gameClient = tig.getGameClient();
		board = tig.getBoard();
		gc = tig.getGc();
	}

	/**
	 * test that the method buyCurrentPropertyForPlayer updates the change in
	 * the player's properties list
	 */
	@Test
	public void buyCurrentPropertyForPlayerChangesPlayersPropertyList() {
		Player plyr = board.getPlayerByName("Justin");
		gameClient.setCurrentPlayer(plyr, true);
		// add money to player's account to loop is possible
		plyr.depositMoney(100000);
		for (int i = 0; i < terrainPositions.length; i++) {
			int tileId = terrainPositions[i];
			plyr.setPosition(tileId);
			gc.buyCurrentPropertyForPlayer("Justin");
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
		gameClient.setCurrentPlayer(plyr, true);
		gameClient.advancePlayerNSpaces(1, false);
		gc.buyCurrentPropertyForPlayer("Justin");
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
		gameClient.advancePlayerNSpaces(1, false);
		gc.buyCurrentPropertyForPlayer("CurRentPlaYER");
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
		gameClient.setCurrentPlayer(plyr, true);
		gameClient.advancePlayerNSpaces(1, false);
		gc.buyCurrentPropertyForPlayer("Justin");
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
		gameClient.setCurrentPlayer(plyr, false);
		gameClient.advancePlayerNSpaces(1, false);
		gc.buyCurrentPropertyForPlayer("Justin");
		gameClient.buyHouse(1, false);
		gameClient.buyHouse(1, false);
		gameClient.buyHouse(1, false);
		gameClient.buyHouse(1, false);
		int accountBefore = plyr.getAccount();
		gameClient.buyHouse(1, false);
		int accountAfter = plyr.getAccount();
		// houses should still be 4
		assertTrue(terrain.getHouseCount() == 4);
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
		gameClient.setCurrentPlayer(plyr, false);
		gameClient.advancePlayerNSpaces(tileId, false);
		gc.buyCurrentPropertyForPlayer("Justin");
		gameClient.buyHouse(tileId, false);
		gameClient.buyHouse(tileId, false);
		gameClient.buyHouse(tileId, false);
		gameClient.buyHouse(tileId, false);
		gameClient.buyHotel(tileId, false);
		int accountBefore = plyr.getAccount();
		gameClient.buyHotel(tileId, false);
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
		gameClient.setCurrentPlayer(plyr, false);
		gameClient.advancePlayerNSpaces(tileId, false);
		gc.buyCurrentPropertyForPlayer("Justin");
		gameClient.buyHouse(tileId, false);
		gameClient.buyHouse(tileId, false);
		gameClient.buyHouse(tileId, false);
		int accountBefore = plyr.getAccount();
		gameClient.buyHotel(tileId, false);
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
		Tile t = board.getTileById(1);
		Terrain terrain = board.castTileToTerrain(t);
		Player plyr = board.getPlayerByName("Justin");
		gameClient.setCurrentPlayer(plyr, false);
		gameClient.advancePlayerNSpaces(tileId, false);
		gc.buyCurrentPropertyForPlayer("Justin");
		int accountBefore = plyr.getAccount();
		gameClient.sellHotel(tileId, false);
		int accountAfter = plyr.getAccount();
		// houses should still be 0
		assertTrue(terrain.getHotelCount() == 0);
		// no money should have changed hands
		assertTrue(accountBefore == accountAfter);
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
		gameClient.setCurrentPlayer(plyr, false);
		gameClient.advancePlayerNSpaces(tileId, false);
		gc.buyCurrentPropertyForPlayer("Justin");
		int accountBefore = plyr.getAccount();
		gameClient.sellHouse(tileId, false);
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
		gameClient.setCurrentPlayer(plyr, false);
		gameClient.advancePlayerNSpaces(tileId, false);
		gameClient.buyCurrentPropertyForPlayer("Justin", false);
		gameClient.buyHouse(tileId, false);
		int accountBefore = plyr.getAccount();
		int houseCost = terrain.getHouseCost();
		gameClient.sellHouse(tileId, false);
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
		gameClient.setCurrentPlayer(plyr, false);
		gameClient.advancePlayerNSpaces(tileId, false);
		gameClient.buyCurrentPropertyForPlayer("Justin", false);
		gameClient.buyHouse(tileId, false);
		gameClient.buyHouse(tileId, false);
		gameClient.buyHouse(tileId, false);
		gameClient.buyHouse(tileId, false);
		gameClient.buyHotel(tileId, false);
		int accountBefore = plyr.getAccount();
		int houseCost = terrain.getHotelCost();
		gameClient.sellHotel(tileId, false);
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
		gameClient.setCurrentPlayer(plyr, false);
		gameClient.advancePlayerNSpaces(tileId, false);
		gc.buyCurrentPropertyForPlayer("Justin");
		// is false after creation, will be set to true
		gameClient.toggleMortgageStatus(tileId, false);
		assertTrue(terrain.isMortgageActive());
		// And once more, just for fun
		gameClient.toggleMortgageStatus(tileId,false);
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
		gameClient.setCurrentPlayer(plyr, false);
		gameClient.advancePlayerNSpaces(tileId, false);
		gc.buyCurrentPropertyForPlayer("Justin");
		int accountBefore = plyr.getAccount();
		// is false after creation, will be set to true
		gameClient.toggleMortgageStatus(tileId,false);
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
		gameClient.setCurrentPlayer(plyr1, false);
		gameClient.advancePlayerNSpaces(tileId, false);
		gameClient.buyCurrentPropertyForPlayer("Justin",false);
		assertTrue(terrain.getOwner().getName().equals(plyr1.getName()));
		gameClient.transferProperty(plyr1.getName(), plyr2.getName(),tileId,0,false);
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
		gameClient.setCurrentPlayer(plyr1, false);
		gameClient.advancePlayerNSpaces(tileId, false);
		gc.buyCurrentPropertyForPlayer("Justin");
		int plyr1AccountBefore = plyr1.getAccount();
		int plyr2AccountBefore = plyr2.getAccount();
		gameClient.transferProperty(plyr1.getName(), plyr2.getName(), tileId,
				price, false);
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
		gameClient.setCurrentPlayer(plyr1, false);
		gameClient.advancePlayerNSpaces(tileId, false);
		gc.buyCurrentPropertyForPlayer("Justin");
		int plyr1AccountBefore = plyr1.getAccount();
		int plyr2AccountBefore = plyr2.getAccount();
		gameClient.transferProperty(plyr1.getName(), plyr2.getName(), tileId,
				price, false);
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
		gameClient.setCurrentPlayer(plyr1, false);
		gameClient.advancePlayerNSpaces(tileId, false);
		gameClient.buyCurrentPropertyForPlayer(player1name, false);
		gameClient.transferProperty(plyr1.getName(), plyr2.getName(), tileId,
				price, false);
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
		gameClient.setCurrentPlayer(plyr1, false);
		gameClient.advancePlayerNSpaces(tileId, false);
		gc.buyCurrentPropertyForPlayer("Justin");
		assertTrue(plyr1.ownsProperty(terrain));
		gameClient.transferProperty("curReNTPlayeR", plyr2.getName(), tileId,
				price, false);
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
		gameClient.setCurrentPlayer(plyr1, false);
		plyr1.setJailCard(1);
		gc.transferJailCards(player1name, player2name, 1);
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
		gc.transferJailCardsForPrice(player1name, player2name, 1, price);
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
		gc.transferJailCardsForPrice(player1name, player2name, 1, price);
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
		gc.transferMoney(player1name, player2name, amount);
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
		gameClient.setCurrentPlayer(plyr1, true);
		int plyr1AccountBefore = plyr1.getAccount();
		int plyr2AccountBefore = plyr2.getAccount();
		gc.transferMoney("CurRenTPlaYEr", player2name, amount);
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
		gameClient.setCurrentPlayer(plyr1, true);
		int plyr1AccountBefore = plyr1.getAccount();
		int plyr2AccountBefore = plyr2.getAccount();
		gc.transferMoney(player2name, "CurRenTPlaYEr", amount);
		assertTrue(plyr1.getAccount() == plyr1AccountBefore + amount);
		assertTrue(plyr2.getAccount() == plyr2AccountBefore - amount);
	}

	/**
	 * test that the game controller can advance the current player a given
	 * number of spaces in the game board
	 */
	@Test
	public void canAdvancedPlayerNSpaces() {
		String player1name = "Justin";
		Player plyr1 = board.getPlayerByName(player1name);
		gameClient.setCurrentPlayer(plyr1, false);
		gameClient.advancePlayerNSpaces(3,false);
		assertTrue(gameClient.getCurrentPlayer().getPosition() == 3);
		// check that modulo is working
		gameClient.advancePlayerNSpaces(40,false);
		assertTrue(gameClient.getCurrentPlayer().getPosition() == 3);

	}

	
	/**
	 * test gc.playerHasSufficientFunds returns true if a player has more than a
	 * given amount of money
	 */
	@Test
	public void playerHasSufficientFunds() {
		String player1name = "Justin";
		int amount = 10000;
		assertTrue(gameClient.playerHasSufficientFunds(player1name, amount));
	}

	/**
	 * test gc.playerHasSufficientFunds returns false if a player has less than
	 * a given amount of money
	 */
	@Test
	public void playerDoesNotHaveSufficientFunds() {
		String player1name = "Justin";
		int amount = 16000;
		assertTrue(!gameClient.playerHasSufficientFunds(player1name, amount));
	}
	
	
	/**
	 *  check that the method gameClient.buyCurrentPropertyForPlayer deducts the money from the players account correctly
	 */
	@Test
	public void buyPropertyFromBankWithdrawsCorrectly() {
		int tileId = 39;
		Player p = board.getPlayerByName("Justin");
		int previousBalance = p.getAccount();
		gameClient.setCurrentPlayer(p,false);
		gameClient.advancePlayerNSpaces(39,false);
		gameClient.buyCurrentPropertyForPlayer(p.getName(),false);
		Tile t = board.getTileById(tileId);
		Property prop =(Property)t;
		int priceOfProperty = prop.getPrice();
		int newBalance = p.getAccount();
		assertTrue(newBalance==(previousBalance-priceOfProperty));
	}
	
	
	
	/**
	 *  check that a player cannot buy a property from the bank, unless he has enough money
	 * @throws TransactionException 
	 */
	@Test
	public void propertyFromBankInsufficientFunds() throws TransactionException {
		int tileId = 39;
		Player plyr = board.getPlayerByName("Justin");
		plyr.withdawMoney(plyr.getAccount()-1);
		gameClient.setCurrentPlayer(plyr,false);
		gameClient.advancePlayerNSpaces(tileId,false);
		int plyrAccountBefore=plyr.getAccount();
		gameClient.buyCurrentPropertyForPlayer(plyr.getName(),false);
		int plyrAccountAfter=plyr.getAccount();
		assertTrue(plyrAccountAfter==plyrAccountBefore);

	}
	
	/**
	 * the method payFee deducts the correct amount from the current player's account
	 */
	@Test 
	public void payFeeDeductsMoneyFromCurrentPlayer(){
		String playerName = "Justin";
		Player plyr = board.getPlayerByName(playerName);
		int playerAccountBefore = plyr.getAccount();
		gameClient.setCurrentPlayer(plyr,false);
		int fee = 100;
		gameClient.payFee(fee,false);
		assertTrue(plyr.getAccount()==playerAccountBefore-fee);
	}
	
	/**
	 * the method payFee() credits the amount of the fee to the variable freeParking
	 */
	@Test 
	public void payFeeCreditsFeeToFreeParking(){
		String playerName = "Justin";
		Player plyr = board.getPlayerByName(playerName);
		gameClient.setCurrentPlayer(plyr,false);
		int fee = 100;
		int freeParkingAmount = gameClient.getFreeParking();
		gameClient.payFee(fee,false);
		assertTrue(gameClient.getFreeParking()==freeParkingAmount+fee);	
	}
	
	
	/**
	 * check that a player cannot buy a property from the bank, unless he has
	 * enough money
	 * 
	 * @throws TransactionException
	 *             if there isn't enough money in the account, this is thrown
	 */
	@Test
	public void cantBuyPropertyWithInsufficientFunds() throws TransactionException {
		int tileId = 39;
		Player p = board.getPlayerByName("Justin");
		p.withdawMoney(p.getAccount() - 1);
		gameClient.setCurrentPlayer(p, false);
		gameClient.advancePlayerNSpaces(tileId, false);
		int plyrAccountBefore = p.getAccount();
		gameClient.buyCurrentPropertyForPlayer(p.getName(), false);
		int plyrAccountAfter = p.getAccount();
		assertTrue(plyrAccountAfter == plyrAccountBefore);
	}


}
