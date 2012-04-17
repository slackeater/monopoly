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
	GameClient gc;
	Board board;

	@Before
	public void setup() {
		gc = new GameClient(new Locale("EN"));
		board = new Board(gc);
		String[] playerNames = { "Justin", "Giuseppe", "Damien", "Cyril",
				"Elie" };
		board.createPlayers(playerNames, gc.getLoc());
	}

	@Test
	public void addPropertyToPlayer() {
		Player p = board.getPlayerByName("Justin");
		Tile t = board.getTileByID(1);
		board.addPropertyToPlayer("Justin", 1);
		// System.out.println(((Property)t).getOwner().getName());
		assertTrue(((Property) t).getOwner() == p);

	}

	@Test
	public void transferPropertiesChangesOwners() {
		Player jus = board.getPlayerByName("Justin");
		Player giu = board.getPlayerByName("Giuseppe");
		Tile t = board.getTileByID(1);
		board.addPropertyToPlayer(jus.getName(), t.getID());
		assertTrue(((Property) t).getOwner() == jus);
		board.transferProperty(jus.getName(), giu.getName(), t.getID());
		assertTrue(((Property) t).getOwner() == giu);
	}

	@Test
	public void playersStartWithCorrectBalance() {
		Player jus = board.getPlayerByName("Justin");
		Player giu = board.getPlayerByName("Giuseppe");
		assertTrue(jus.getAccount() == 1500);
		assertTrue(giu.getAccount() == 1500);
	}

	@Test
	public void moneyTransfersBetweenPlayers() {
		Player jus = board.getPlayerByName("Justin");
		Player giu = board.getPlayerByName("Giuseppe");
		board.transferMoney(jus.getName(), giu.getName(), 500);
		assertTrue(jus.getAccount() == 1000);
		assertTrue(giu.getAccount() == 2000);
	}

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

}
