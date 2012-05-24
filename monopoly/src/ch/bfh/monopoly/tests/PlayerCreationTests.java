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


public class PlayerCreationTests {

	
	
	Locale loc;
	GameClient gameClient;
	Board board;
	GameController gc;
	BoardController bc;

	
	@Before
	public void setup() {
		TestInstanceGenerator tig = new TestInstanceGenerator("en");
		gameClient= tig.getGameClient();
		board=tig.getBoard();
		gc=tig.getGc();
		bc=tig.getBc();
	}
	
	
	@Test
	public void playerObjectsHaveCorrentNames(){
		Player p = board.getPlayerByName("Justin");
		assertTrue(p.getName().equals("Justin"));
	}
	
	@Test
	public void playerJailStatusChanges(){
		Player p = board.getPlayerByName("Justin");
		p.setInJail(true);
		assertTrue(p.isInJail());
	}
	
}
