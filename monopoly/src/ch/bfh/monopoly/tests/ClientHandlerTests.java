package ch.bfh.monopoly.tests;

import java.util.Locale;

import org.junit.Before;

import ch.bfh.monopoly.common.Board;
import ch.bfh.monopoly.common.GameClient;
import ch.bfh.monopoly.common.GameController;

public class ClientHandlerTests {

	Locale loc;
	GameClient gameClient;
	Board board;
	GameController gc;
	int[] terrainPositions = { 1, 3, 6, 8, 9, 11, 13, 14, 16, 18, 19, 21, 23,
			24, 26, 27, 29, 31, 32, 34, 37, 39 };

	@Before
	public void setup() {
		TestInstanceGenerator tig = new TestInstanceGenerator();
		gameClient = tig.getGameClient();
		board = tig.getBoard();
		gc = tig.getGc();
	}
	
}
