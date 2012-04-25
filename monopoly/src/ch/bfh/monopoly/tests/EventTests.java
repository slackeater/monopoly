package ch.bfh.monopoly.tests;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import ch.bfh.monopoly.common.Board;
import ch.bfh.monopoly.common.GameClient;
import ch.bfh.monopoly.common.Player;

public class EventTests {
	Locale loc;
	GameClient gameClient;
	Board board;

	@Before
	public void setup() {
		gameClient = new GameClient(new Locale("EN"));
		board = new Board(gameClient);
		String[] playerNames = { "Justin", "Giuseppe", "Damien", "Cyril",
				"Elie" };
		board.createPlayers(playerNames, gameClient.getLoc());
	}

	@Test
	public void returnsCorrectFeeForHouses() {
		System.out.println(board.getPlayerByName("Justin").getPosition());
		Player p =board.getPlayerByName("Justin");
		gameClient.setCurrentPlayer(p);
		
		
	}
}
