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
}
