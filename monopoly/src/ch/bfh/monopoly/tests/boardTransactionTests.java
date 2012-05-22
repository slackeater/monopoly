package ch.bfh.monopoly.tests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import ch.bfh.monopoly.common.Board;
import ch.bfh.monopoly.common.GameClient;
import ch.bfh.monopoly.common.Player;
import ch.bfh.monopoly.exception.TransactionException;
import ch.bfh.monopoly.tile.Property;
import ch.bfh.monopoly.tile.Tile;

public class boardTransactionTests {

	Locale loc;
	GameClient gameClient;
	Board board;

	@Before
	public void setup() {
		TestInstanceGenerator tig = new TestInstanceGenerator();
		gameClient = tig.getGameClient();
		board = tig.getBoard();
	}

	/**
	 * check that the method addPropertyToPlayer gives a property to a given
	 * player and updates that property's owner field
	 * 
	 * @throws TransactionException
	 */
	@Test
	public void addPropertyToPlayer() throws TransactionException {
		Player p = board.getPlayerByName("Justin");
		Tile t = board.getTileById(1);
		gameClient.setCurrentPlayer(p,false);
		gameClient.advancePlayerNSpaces(1,false);
		board.buyCurrentPropertyForPlayer("Justin", 1);
		// System.out.println(((Property)t).getOwner().getName());
		assertTrue(((Property) t).getOwner() == p);
	}


	/**
	 * check that money can be sent from one player to another, and the money is
	 * deducted from one player and added to another player's account.
	 * @throws TransactionException 
	 */
	@Test
	public void moneyTransfersBetweenPlayers() throws TransactionException {
		int transferAmount = 500;
		Player p1 = board.getPlayerByName("Justin");
		int p1account = p1.getAccount();
		Player p2 = board.getPlayerByName("Giuseppe");
		int p2account = p2.getAccount();
		board.transferMoney(p1.getName(), p2.getName(), transferAmount);
		assertTrue(p1.getAccount() == p1account - transferAmount);
		assertTrue(p2.getAccount() == p2account + transferAmount);
	}

	/**
	 * check that it is not possible to transfer more money than a given account
	 * contains
	 */
	@Test
	public void cannotTransferMoreThanYouHave() {
		int transferAmount = 50000;
		Player p1 = board.getPlayerByName("Justin");
		Player p2 = board.getPlayerByName("Giuseppe");
		try {
			board.transferMoney(p1.getName(), p2.getName(), transferAmount);
			fail("FAIL: program allows a transfer for an amount larger than that of the player's account balance");
		} catch (Exception e) {
			//System.out.println(e.getMessage());
		}
	}

	/**
	 * check that the method transferProperty() takes a given property from one
	 * player and gives it to another and updates the owner field of that
	 * property event though the method buyCurrentPropertyForPlayer is here, we
	 * are only interested that ownership changes for the tile
	 * @throws TransactionException 
	 */
	@Test
	public void transferPropertiesChangesOwners() throws TransactionException {
		int price = 0;
		Player p1 = board.getPlayerByName("Justin");
		Player p2 = board.getPlayerByName("Giuseppe");
		gameClient.setCurrentPlayer(p1,false);
		gameClient.advancePlayerNSpaces(1,false);
		Tile t = board.getTileById(1);
		gameClient.buyCurrentPropertyForPlayer(p1.getName(),false);
		assertTrue(((Property) t).getOwner() == p1);
		board.transferProperty(p1.getName(), p2.getName(), t.getTileId(), price);
		assertTrue(((Property) t).getOwner() == p2);
	}

	/**
	 * check that the a player cannot transfer a property to another player
	 * unless he owns the property
	 */
	@Test
	public void cannotTransferPropertyPlayerDoesNotOwn() {
		int price = 0;
		Player jus = board.getPlayerByName("Justin");
		Player giu = board.getPlayerByName("Giuseppe");
		Tile t = board.getTileById(1);
		try {
			board.transferProperty(jus.getName(), giu.getName(), 3, price);
			fail("FAIL: player can sell a property he does not own");
		} catch (Exception e) {
			//System.out.println(e.getMessage());
		}
	}

}
