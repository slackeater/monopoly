package ch.bfh.monopoly.tests;

import java.util.Locale;

import org.junit.Before;

import ch.bfh.monopoly.common.Board;
import ch.bfh.monopoly.common.BoardController;
import ch.bfh.monopoly.common.GameClient;
import ch.bfh.monopoly.common.GameController;

public class BoardControllerTests {
	Locale loc;
	GameClient gameClient;
	Board board;
	BoardController bc;

	@Before
	public void setup() {
		TestInstanceGenerator tig = new TestInstanceGenerator("en");
		gameClient= tig.getGameClient();
		board=tig.getBoard();
		bc = tig.getBc();
	}
	



}
