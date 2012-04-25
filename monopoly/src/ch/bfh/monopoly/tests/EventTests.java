package ch.bfh.monopoly.tests;

import static org.junit.Assert.*;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import ch.bfh.monopoly.common.Board;
import ch.bfh.monopoly.common.GameClient;
import ch.bfh.monopoly.common.GameController;
import ch.bfh.monopoly.common.Player;

public class EventTests {
	Locale loc;
	GameClient gameClient;
	Board board;
	GameController gc;


	@Before
	public void setup() {
		gameClient = new GameClient(new Locale("EN"));
		gc =new GameController(gameClient);
		board = new Board(gameClient);
		String[] playerNames = { "Justin", "Giuseppe", "Damien", "Cyril",
				"Elie" };
		board.createPlayers(playerNames, gameClient.getLoc());
	}

	/**
	 * test that when a certain movement event is triggered, that hte position of the current player is changed
	 */
	@Test
	public void movementEventChangesPositionOfPlayer() {
		Player p =board.getPlayerByName("Justin");
		gameClient.setCurrentPlayer(p);
		p.setPosition(26);
		//advance player to GO TO JAIL
		gameClient.advanceCurrentPlayerNSpaces(4);
		gc.performEvent();
		assertTrue(p.isInJail());
		assertTrue(p.getPosition() == 10);
	}
	/**
	 * simpleFeeEvents create their description dynamically based on amount of houses and hotels and mortgage condition
	 * this tests certain simpleFeeEvents under certain conditions to make sure their descriptions are accurate
	 * the base string used in the constructor of SimpleFeeEvent is currently 
	 *    ==> "If you are not the owner of this tile, you must pay rent.  The rent for this tile is " + RENT
	 */
	@Test
	public void correctDescriptionOfOrientalAvenue(){
		Player p =board.getPlayerByName("Justin");
		int tileId = 6; //oriental avenue
		gameClient.setCurrentPlayer(p);
		gameClient.advanceCurrentPlayerNSpaces(tileId);
		int rent = gameClient.getFeeForTileAtId(tileId);
		String expectedDescription = "If you are not the owner of this tile, you must pay rent.  The rent for this tile is " + rent;
		String text = gc.getEventDescription();
		assertTrue(expectedDescription.equals(text));
		
		//check that fee is correct after building one house
		gc.buyHouse(tileId);
		rent = gameClient.getFeeForTileAtId(tileId);
		expectedDescription = "If you are not the owner of this tile, you must pay rent.  The rent for this tile is " + rent;
		text = gc.getEventDescription();
		assertTrue(expectedDescription.equals(text));
	}
	
	/**
	 * same as above, but for Virginia Avenue
	 */
	@Test
	public void correctDescriptionOfVirginiaAvenue(){
		Player p =board.getPlayerByName("Justin");
		int tileId = 14; //Virginia Avenue
		gameClient.setCurrentPlayer(p);
		gameClient.advanceCurrentPlayerNSpaces(tileId);
		int rent = gameClient.getFeeForTileAtId(tileId);
		String expectedDescription = "If you are not the owner of this tile, you must pay rent.  The rent for this tile is " + rent;
		String text = gc.getEventDescription();
		assertTrue(expectedDescription.equals(text));
		
		//check that fee is correct after building one house
		gc.buyHouse(tileId);
		rent = gameClient.getFeeForTileAtId(tileId);
		expectedDescription = "If you are not the owner of this tile, you must pay rent.  The rent for this tile is " + rent;
		text = gc.getEventDescription();
		assertTrue(expectedDescription.equals(text));
	}
	
	/**
	 * same as above, but for Virginia Avenue
	 */
	@Test
	public void performSimpleFeeEventWithdrawsMoneyFromPlayer(){
		Player p =board.getPlayerByName("Justin");
		int tileId = 14; //Virginia Avenue
		gameClient.setCurrentPlayer(p);
		gameClient.advanceCurrentPlayerNSpaces(tileId);
		int rent = gameClient.getFeeForTileAtId(tileId);
		int previousBalance = p.getAccount();
		gc.performEvent();
		assertTrue(p.getAccount()==previousBalance-rent);
	}
	
	/**
	 * same as above, but for Virginia Avenue
	 */
	@Test
	public void performSimpleFeeEventInsufficientFunds(){
		Player p =board.getPlayerByName("Justin");
		int tileId = 14; //Virginia Avenue
		gameClient.setCurrentPlayer(p);
		gameClient.advanceCurrentPlayerNSpaces(tileId);
		int rent = gameClient.getFeeForTileAtId(tileId);
		//set player's account to 1 dollar
		p.withdawMoney(p.getAccount()-1);
		try {
			gc.performEvent();
			fail("FAIL: Player paid a fee, but should not have had enough money to do so");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}


	}
	
}
