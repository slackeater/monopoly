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
	boolean sendNetMessage=false;
	@Before
	public void setup() {
		TestInstanceGenerator tig = new TestInstanceGenerator("en");
		gameClient= tig.getGameClient();
		board=tig.getBoard();
	}


	@Test
	public void returnsCorrectFeeForHouses() {
		gameClient.setCurrentPlayer("Justin", sendNetMessage);
		gameClient.advancePlayerNSpaces(1, sendNetMessage);
		gameClient.buyCurrentPropertyForPlayer("Justin", sendNetMessage);
		gameClient.advancePlayerNSpaces(2, sendNetMessage);
		gameClient.buyCurrentPropertyForPlayer("Justin", sendNetMessage);
		int fee = gameClient.getFeeForTileAtId(1);
		assertTrue(fee == 2);
		// get fee for tile 1 with 1 house
		gameClient.buyHouse(1,sendNetMessage);
		fee = gameClient.getFeeForTileAtId(1);
		assertTrue(fee == 10);
		// get fee for tile 1 with 2 houses
		gameClient.buyHouse(1,sendNetMessage);
		fee = gameClient.getFeeForTileAtId(1);
		assertTrue(fee == 30);
		// get fee for tile 1 with 3 houses
		gameClient.buyHouse(1,sendNetMessage);
		fee = gameClient.getFeeForTileAtId(1);
		assertTrue(fee == 90);
		// get fee for tile 1 with 4 houses
		gameClient.buyHouse(1,sendNetMessage);
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
		gameClient.setCurrentPlayer(p,false);
		gameClient.advancePlayerNSpaces(4,false);
		int newPosition = gameClient.getCurrentPlayer().getPosition();
		assertTrue(newPosition == 4);
		gameClient.advancePlayerNSpaces(10,false);
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
		gameClient.setCurrentPlayer(p,sendNetMessage);
		gameClient.getCurrentPlayer().setPosition(36);
		gameClient.advancePlayerNSpaces(10,sendNetMessage);
		int newPosition = gameClient.getCurrentPlayer().getPosition();
		assertTrue(newPosition == 6);
	}
	

	
	
	/**
	 * test that when one player owns both utility tiles that gameClient.hasBothUtilites
	 * returns true
	 * @return true if 1 player owns both utilities
	 */
	@Test
	public void playerHasBothUtilities(){
		Player p = board.getPlayerByName("Justin");
		gameClient.setCurrentPlayer(p,sendNetMessage);
		gameClient.advancePlayerNSpaces(12,sendNetMessage);
		gameClient.buyCurrentPropertyForPlayer(p.getName(),sendNetMessage);
		//Advance current player to tile number 28
		gameClient.advancePlayerNSpaces(16,sendNetMessage);
		gameClient.buyCurrentPropertyForPlayer(p.getName(),sendNetMessage);
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
		gameClient.setCurrentPlayer(p1,sendNetMessage);
		gameClient.advancePlayerNSpaces(12,sendNetMessage);
		gameClient.buyCurrentPropertyForPlayer(p1.getName(),sendNetMessage);
	
		gameClient.setCurrentPlayer(p2,sendNetMessage);
		gameClient.advancePlayerNSpaces(28,sendNetMessage);
		gameClient.buyCurrentPropertyForPlayer(p2.getName(),sendNetMessage);
		assertTrue(!gameClient.hasBothUtilities());
	}
	
	/**
	 * test that gameClient.addJailCardToPlayer works
	 */
	@Test
	public void addJailCardsWorks(){
		Player plyr1 = board.getPlayerByName("Justin");
		gameClient.addJailCardToPlayer(plyr1.getName(),sendNetMessage);
		assertTrue(plyr1.getJailCard()==1);
	}
	
	/**
	 * test that gameClient.removeJailCardFromPlayer works
	 */
	@Test
	public void removeJailCardsWorks(){
		Player plyr1 = board.getPlayerByName("Justin");
		plyr1.setJailCard(2);
		gameClient.removeJailCardFromPlayer(plyr1.getName(),sendNetMessage);
		assertTrue(plyr1.getJailCard()==1);
	}
	
	
	
	
	
}