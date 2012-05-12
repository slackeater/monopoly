package ch.bfh.monopoly.tests;

import static org.junit.Assert.assertTrue;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import ch.bfh.monopoly.common.Board;
import ch.bfh.monopoly.common.BoardController;
import ch.bfh.monopoly.common.GameClient;
import ch.bfh.monopoly.common.GameController;
import ch.bfh.monopoly.common.Player;
import ch.bfh.monopoly.tile.Property;

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
		Property prop = (Property)board.getTileById(1);
		gameClient.setCurrentPlayer(plyr);
		gameClient.advanceCurrentPlayerNSpaces(1);
		gc.buyCurrentPropertyForPlayer("Justin");
		gc.buyHouse(1);
	}


}
