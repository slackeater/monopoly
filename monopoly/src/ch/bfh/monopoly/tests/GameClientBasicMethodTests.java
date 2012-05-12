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

public class GameClientBasicMethodTests {

	Locale loc;
	GameClient gameClient;
	Board board;

	@Before
	public void setup() {
		TestInstanceGenerator tig = new TestInstanceGenerator();
		gameClient= tig.getGameClient();
		board=tig.getBoard();
	}


	@Test
	public void returnsCorrectFeeForHouses() {
		int fee = gameClient.getFeeForTileAtId(1);
		assertTrue(fee == 2);
		// get fee for tile 1 with 1 house
		gameClient.buyHouse(1);
		fee = gameClient.getFeeForTileAtId(1);
		assertTrue(fee == 10);
		// get fee for tile 1 with 2 houses
		gameClient.buyHouse(1);
		fee = gameClient.getFeeForTileAtId(1);
		assertTrue(fee == 30);
		// get fee for tile 1 with 3 houses
		gameClient.buyHouse(1);
		fee = gameClient.getFeeForTileAtId(1);
		assertTrue(fee == 90);
		// get fee for tile 1 with 4 houses
		gameClient.buyHouse(1);
		fee = gameClient.getFeeForTileAtId(1);
		assertTrue(fee == 160);
	}

	/**
	 * check that the method gameClient.advanceCurrentPlayerNSpaces
	 * advances the position of a player a given number n spaces forward
	 */
	@Test
	public void playersPositionAdvancesNSpaces() {
		Player p = board.getPlayerByName("Justin");
		gameClient.setCurrentPlayer(p);
		gameClient.advanceCurrentPlayerNSpaces(4);
		int newPosition = gameClient.getCurrentPlayer().getPosition();
		assertTrue(newPosition == 4);
		gameClient.advanceCurrentPlayerNSpaces(10);
		newPosition = gameClient.getCurrentPlayer().getPosition();
		assertTrue(newPosition == 14);
	}
	
	/**
	 * check that the method gameClient.advanceCurrentPlayerNSpaces
	 * properly adjusts the player's position if a tour around the board is completed
	 */
	@Test
	public void playerCompletesTourAroundBoard() {
		Player p = board.getPlayerByName("Justin");
		gameClient.setCurrentPlayer(p);
		gameClient.getCurrentPlayer().setPosition(36);
		gameClient.advanceCurrentPlayerNSpaces(10);
		int newPosition = gameClient.getCurrentPlayer().getPosition();
		assertTrue(newPosition == 6);
	}
	
	/**
	 * Test that the gameClient.hasSufficientFunds method returns
	 * false when a the currentPlayer doesn't have enough money to pay a given fee
	 */
	@Test
	public void currentPlayerHasSufficientFunds(){
		Player p = board.getPlayerByName("Justin");
		gameClient.setCurrentPlayer(p);
		p.withdawMoney(14000);
		assertTrue(!gameClient.hasSufficientFunds(2000));
	}
	
	/**
	 * Test that the gameClient.playerHasSufficientFunds method returns
	 * false when a player other than the currentPlayer doesn't have enough money to pay a given fee
	 */
	@Test
	public void otherPlayerHasSufficientFunds(){
		Player p = board.getPlayerByName("Justin");
		Player p2 = board.getPlayerByName("Giuseppe");
		gameClient.setCurrentPlayer(p);
		p2.withdawMoney(14000);
		assertTrue(!gameClient.playerHasSufficientFunds("Giuseppe",2000));
	}
	
	/**
	 * test that when one player owns both utility tiles that gameClient.hasBothUtilites
	 * returns true
	 * @return true if 1 player owns both utilities
	 */
	@Test
	public void playerHasBothUtilities(){
		Player p = board.getPlayerByName("Justin");
		gameClient.setCurrentPlayer(p);
		gameClient.advanceCurrentPlayerNSpaces(12);
		gameClient.buyCurrentPropertyForPlayer(p.getName());
		//Advance current player to tile number 28
		gameClient.advanceCurrentPlayerNSpaces(16);
		gameClient.buyCurrentPropertyForPlayer(p.getName());
		assertTrue(gameClient.hasBothUtilities());
	}
	
	/**
	 * test when utilities not owned by same player that method gameClient.hasBothUtilites returns false
	 * @return true if utilites owned by different players
	 */
	@Test
	public void playerDoesNotHaveBothUtilities(){
		Player p1= board.getPlayerByName("Justin");
		Player p2 = board.getPlayerByName("Giuseppe");
		gameClient.setCurrentPlayer(p1);
		gameClient.advanceCurrentPlayerNSpaces(12);
		gameClient.buyCurrentPropertyForPlayer(p1.getName());
	
		gameClient.setCurrentPlayer(p2);
		gameClient.advanceCurrentPlayerNSpaces(28);
		gameClient.buyCurrentPropertyForPlayer(p2.getName());
		assertTrue(!gameClient.hasBothUtilities());
	}

}