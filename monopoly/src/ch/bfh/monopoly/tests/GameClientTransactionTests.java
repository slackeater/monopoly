package ch.bfh.monopoly.tests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import ch.bfh.monopoly.common.Board;
import ch.bfh.monopoly.common.GameClient;
import ch.bfh.monopoly.common.Player;
import ch.bfh.monopoly.tile.Property;
import ch.bfh.monopoly.tile.Tile;

public class GameClientTransactionTests {

	Locale loc;
	GameClient gameClient;
	Board board;

	@Before
	public void setup() {
		TestInstanceGenerator tig = new TestInstanceGenerator();
		gameClient= tig.getGameClient();
		board=tig.getBoard();
	}


	/**
	 * check that the method addPropertyToPlayer gives a property to a give player
	 * and updates that property's owner field 
	 */
	@Test
	public void addPropertyToPlayer() {
		Player p = board.getPlayerByName("Justin");
		Tile t = board.getTileById(1);
		board.addPropertyToPlayer("Justin", 1);
		// System.out.println(((Property)t).getOwner().getName());
		assertTrue(((Property) t).getOwner() == p);

	}

	/**
	 * check that the method transferProperty() takes a given property from one player
	 * and gives it to another and updates the owner field of that property
	 */
	@Test
	public void transferPropertiesChangesOwners() {
		Player jus = board.getPlayerByName("Justin");
		Player giu = board.getPlayerByName("Giuseppe");
		Tile t = board.getTileById(1);
		board.addPropertyToPlayer(jus.getName(), t.getId());
		assertTrue(((Property) t).getOwner() == jus);
		board.transferProperty(jus.getName(), giu.getName(), t.getId());
		assertTrue(((Property) t).getOwner() == giu);
	}
	
	
	/**
	 * check that the a player cannot transfer a property to another player 
	 * unless he owns the property
	 */
	@Test
	public void transferPropertyPlayerDoesNotOwn() {
		Player jus = board.getPlayerByName("Justin");
		Player giu = board.getPlayerByName("Giuseppe");
		Tile t = board.getTileById(1);
		try {
			board.transferProperty(jus.getName(), giu.getName(), 3);
			fail("FAIL: player can sell a property he does not own");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	
	/**
	 * check that the player's were created with the proper balance based upon the a given local
	 * in this case the local is EN for all tests
	 */
	@Test
	public void playersStartWithCorrectBalance() {
		Player jus = board.getPlayerByName("Justin");
		Player giu = board.getPlayerByName("Giuseppe");
		assertTrue(jus.getAccount() == 1500);
		assertTrue(giu.getAccount() == 1500);
	}

	/**
	 * check that money can be sent from one player to another, and the money is deducted from one player and added
	 * to another player's account.  
	 */
	@Test
	public void moneyTransfersBetweenPlayers() {
		Player jus = board.getPlayerByName("Justin");
		Player giu = board.getPlayerByName("Giuseppe");
		board.transferMoney(jus.getName(), giu.getName(), 500);
		assertTrue(jus.getAccount() == 1000);
		assertTrue(giu.getAccount() == 2000);
	}

	/**
	 * check that it is not possible to transfer more money than a given account contains
	 */
	@Test
	public void cannotTransferMoreThanYouHave() {
		Player jus = board.getPlayerByName("Justin");
		Player giu = board.getPlayerByName("Giuseppe");
		try {
			board.transferMoney(jus.getName(), giu.getName(), 2000);
			fail("FAIL: program allows a transfer for an amount larger than that of the player's account balance");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	
	/**
	 *  check that the method gameClient.buyPropertyFromBank deducts the money from the players account correctly
	 */
	@Test
	public void buyPropertyFromBankWithdrawsCorrectly() {
		int tileId = 39;
		Player p = board.getPlayerByName("Justin");
		int previousBalance = p.getAccount();
		gameClient.setCurrentPlayer(p);
		gameClient.buyPropertyFromBank(tileId);
		Tile t = board.getTileById(tileId);
		Property prop =(Property)t;
		int priceOfProperty = prop.getPrice();
		int newBalance = p.getAccount();
		assertTrue(newBalance==(previousBalance-priceOfProperty));
	}
	
	
	/**
	 *  check that a player can by a property from the bank, only if the bank owns it
	 */
	@Test
	public void buyPropertyFromBank() {
		int tileId = 39;
		Player p = board.getPlayerByName("Justin");
		gameClient.setCurrentPlayer(p);
		gameClient.buyPropertyFromBank(tileId);
		Tile t = board.getTileById(tileId);
		assertTrue(((Property)t).getOwner()==p);
	}
	
	/**
	 *  check that a player cannot buy a property from the bank, unless he has enough money
	 */
	@Test
	public void propertyFromBankInsufficientFunds() {
		int tileId = 39;
		Player p = board.getPlayerByName("Justin");
		p.withdawMoney(p.getAccount()-1);
		gameClient.setCurrentPlayer(p);
		try {
			gameClient.buyPropertyFromBank(tileId);
			fail("FAIL: Player bought a property from the bank, but had insufficient funds");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
