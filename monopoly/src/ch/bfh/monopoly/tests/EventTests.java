package ch.bfh.monopoly.tests;

import static org.junit.Assert.*;

import java.awt.List;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import ch.bfh.monopoly.common.Board;
import ch.bfh.monopoly.common.GameClient;
import ch.bfh.monopoly.common.GameController;
import ch.bfh.monopoly.common.Player;
import ch.bfh.monopoly.event.AbstractTileEvent;
import ch.bfh.monopoly.event.BoardEvent;
import ch.bfh.monopoly.event.EventManager;
import ch.bfh.monopoly.tile.AbstractTile;
import ch.bfh.monopoly.tile.Tile;

public class EventTests {
	GameClient gameClient;
	Board board;
	GameController gc;
	EventManager em;

	@Before
	public void setup() {
		TestInstanceGenerator tig = new TestInstanceGenerator();
		gameClient = tig.getGameClient();
		board = tig.getBoard();
		gc = tig.getGc();
		em = tig.em;
	}

	/**
	 * Tests that birthday event transfers money from all players correctly
	 */
	@Test
	public void birthdayEventTransfersRight() {
		int playerCount = board.getPlayers().size();
		Player p = board.getPlayerByName("Justin");
		gameClient.setCurrentPlayer(p);
		int balanceJustinBefore = board.getPlayerByName("Justin").getAccount();
		int balance1before = board.getPlayerByName("Giuseppe").getAccount();
		int balance2before = board.getPlayerByName("Damien").getAccount();
		int balance3before = board.getPlayerByName("Cyril").getAccount();
		int balance4before = board.getPlayerByName("Elie").getAccount();
		BoardEvent[] commChestEvents = em.getCommChestEvents();
		em.setCurrentEvent(commChestEvents[3]);
		em.performEventCommChest();
		int balanceJustinAfter = board.getPlayerByName("Justin").getAccount();
		int balance1after = board.getPlayerByName("Giuseppe").getAccount();
		int balance2after = board.getPlayerByName("Damien").getAccount();
		int balance3after = board.getPlayerByName("Cyril").getAccount();
		int balance4after = board.getPlayerByName("Elie").getAccount();
		assertTrue(balanceJustinBefore ==( balanceJustinAfter - (20*(playerCount-1))) &&
				balance1before == (balance1after+20) &&
				balance2before == (balance2after+20) &&
				balance3before == (balance3after+20) &&
				balance4before == (balance4after+20));
	}

	/**
	 * Test that the Repairs event deducts the appropriate sum from the player's
	 * account
	 */
	@Test
	public void eventManagerRandomizesChanceEvents() {
		Player p = board.getPlayerByName("Justin");
		gameClient.setCurrentPlayer(p);
		p.setPosition(26);
		// advance player to GO TO JAIL
		gameClient.advanceCurrentPlayerNSpaces(4);
		int pos = p.getPosition();
		Tile t = board.getTileById(pos);
		AbstractTile tt = (AbstractTile) t;
		BoardEvent[] chanceEvents = em.getChanceEvents();
		em.setCurrentEvent(chanceEvents[7]);
		System.out
				.println("IF you see this, then eventManagerRandomizesChanceEvents works : "
						+ em.getEventDescriptionChance());
	}

	/**
	 * test that landing on GO TO JAIL send the player to jail and changes
	 * inJail boolean status
	 */
	@Test
	public void goToJailSendsPlayerToJail() {
		Player p = board.getPlayerByName("Justin");
		gameClient.setCurrentPlayer(p);
		p.setPosition(26);
		// advance player to GO TO JAIL
		gameClient.advanceCurrentPlayerNSpaces(4);
		gc.performEvent();
		assertTrue(p.isInJail());
		assertTrue(p.getPosition() == 10);
	}

	/**
	 * simpleFeeEvents create their description dynamically based on amount of
	 * houses and hotels and mortgage condition this tests certain
	 * simpleFeeEvents under certain conditions to make sure their descriptions
	 * are accurate the base string used in the constructor of SimpleFeeEvent is
	 * currently ==>
	 * "If you are not the owner of this tile, you must pay rent.  The rent for this tile is "
	 * + RENT
	 */
	@Test
	public void correctDescriptionOfOrientalAvenue() {
		Player p = board.getPlayerByName("Justin");
		int tileId = 6; // oriental avenue
		gameClient.setCurrentPlayer(p);
		gameClient.advanceCurrentPlayerNSpaces(tileId);
		int rent = gameClient.getFeeForTileAtId(tileId);
		String expectedDescription = "If you are not the owner of this tile, you must pay rent.  The rent for this tile is "
				+ rent;
		String text = gc.getEventDescription();
		assertTrue(expectedDescription.equals(text));

		// check that fee is correct after building one house
		gc.buyHouse(tileId);
		rent = gameClient.getFeeForTileAtId(tileId);
		expectedDescription = "If you are not the owner of this tile, you must pay rent.  The rent for this tile is "
				+ rent;
		text = gc.getEventDescription();
		assertTrue(expectedDescription.equals(text));
	}

	/**
	 * same as above, but for Virginia Avenue
	 */
	@Test
	public void correctDescriptionOfVirginiaAvenue() {
		Player p = board.getPlayerByName("Justin");
		int tileId = 14; // Virginia Avenue
		gameClient.setCurrentPlayer(p);
		gameClient.advanceCurrentPlayerNSpaces(tileId);
		int rent = gameClient.getFeeForTileAtId(tileId);
		String expectedDescription = "If you are not the owner of this tile, you must pay rent.  The rent for this tile is "
				+ rent;
		String text = gc.getEventDescription();
		assertTrue(expectedDescription.equals(text));

		// check that fee is correct after building one house
		gc.buyHouse(tileId);
		rent = gameClient.getFeeForTileAtId(tileId);
		expectedDescription = "If you are not the owner of this tile, you must pay rent.  The rent for this tile is "
				+ rent;
		text = gc.getEventDescription();
		assertTrue(expectedDescription.equals(text));
	}

	/**
	 * same as above, but for Virginia Avenue
	 */
	@Test
	public void performSimpleFeeEventWithdrawsMoneyFromPlayer() {
		Player p = board.getPlayerByName("Justin");
		int tileId = 14; // Virginia Avenue
		gameClient.setCurrentPlayer(p);
		gameClient.advanceCurrentPlayerNSpaces(tileId);
		int rent = gameClient.getFeeForTileAtId(tileId);
		int previousBalance = p.getAccount();
		gc.performEvent();
		assertTrue(p.getAccount() == previousBalance - rent);
	}

	/**
	 * same as above, but for Virginia Avenue, and player doesn't have enough
	 * money to pay the fee
	 */
	@Test
	public void performSimpleFeeEventInsufficientFunds() {
		Player p = board.getPlayerByName("Justin");
		int tileId = 14; // Virginia Avenue
		gameClient.setCurrentPlayer(p);
		gameClient.advanceCurrentPlayerNSpaces(tileId);
		int rent = gameClient.getFeeForTileAtId(tileId);
		// set player's account to 1 dollar
		p.withdawMoney(p.getAccount() - 1);
		try {
			gc.performEvent();
			fail("FAIL: Player paid a fee, but should not have had enough money to do so");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * utility event check
	 */
	@Test
	public void utilityEventDeductsCorrectFee() {
		Player p = board.getPlayerByName("Justin");
		int tileId = 12; // Virginia Avenue
		gameClient.setCurrentPlayer(p);
		gameClient.advanceCurrentPlayerNSpaces(tileId);
		int rent = gameClient.getFeeForTileAtId(tileId);
		int previousBalance = p.getAccount();
		gc.performEvent();
		assertTrue(p.getAccount() == previousBalance - rent);
	}

	/**
	 * Check that GO event deposits money into player's account
	 */
	@Test
	public void collect200DollarsAtLandOnGo() {
		Player p = board.getPlayerByName("Justin");
		int tileId = 37; // Park Place
		gameClient.setCurrentPlayer(p);
		gameClient.advanceCurrentPlayerNSpaces(tileId);
		gameClient.advanceCurrentPlayerNSpaces(3);
		System.out.println(gameClient.getCurrentPlayer().getPosition());
		int previousBalance = p.getAccount();
		gc.performEvent();
		assertTrue(p.getAccount() == previousBalance + 200);
	}

	/**
	 * Check that Free Parking deposits money into player's account
	 */
	@Test
	public void freeParkingDepositsMoneytoPlayer() {
		int fee = 250;
		Player plyr1 = board.getPlayerByName("Justin");
		Player plyr2 = board.getPlayerByName("Giuseppe");
		int tileId = 20; // Park Place
		gameClient.setCurrentPlayer(plyr1);
		gameClient.payFee(fee);
		gameClient.setCurrentPlayer(plyr2);
		gameClient.advanceCurrentPlayerNSpaces(tileId);
		int previousBalance = plyr2.getAccount();
		gc.performEvent();
		assertTrue(plyr2.getAccount() == previousBalance + fee);
	}

	/**
	 * Check that Free Parking amount is incremented when fees are paid to the
	 * bank
	 */
	@Test
	public void freeParkingIncrementedWithFees() {
		// TODO FRee parking is incremented when a player pays a FEE to the bank
	}

	/**
	 * Check that income tax withdraws 10% of player's income
	 */
	@Test
	public void incomeTax10Percent() {
		Player p = board.getPlayerByName("Justin");
		int tileId = 4; // Park Place
		gameClient.setCurrentPlayer(p);
		gameClient.advanceCurrentPlayerNSpaces(tileId);
		int previousBalance = p.getAccount();
		int fee = previousBalance / 10;
		gc.performEvent();
		assertTrue(p.getAccount() == previousBalance - fee);
	}

	/**
	 * Check that chance events trigger when landing on any chance tile
	 */
	@Test
	public void chanceEventsTrigger() {
		Player p = board.getPlayerByName("Justin");
		int tileId = 7; // Park Place
		gameClient.setCurrentPlayer(p);
		gameClient.advanceCurrentPlayerNSpaces(tileId);
		System.out.println("chanceEventsTrigger: " + gc.getEventDescription());
		gc.performEvent();
		assertTrue(p.getPosition() != 7);

	}

}
