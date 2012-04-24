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
		gameClient = new GameClient(new Locale("EN"));
		board = new Board(gameClient);
		String[] playerNames = { "Justin", "Giuseppe", "Damien", "Cyril",
				"Elie" };
		board.createPlayers(playerNames, gameClient.getLoc());
	}

	@Test
	public void returnsCorrectFeeForHouses() {
		int fee = gameClient.getFeeForTileAtId(1);
		assertTrue(fee == 2);
		//get fee for tile 1 with 1 house 
		gameClient.buyHouse(1);
		fee = gameClient.getFeeForTileAtId(1);
		assertTrue(fee == 10);
		//get fee for tile 1 with 2 houses 
		gameClient.buyHouse(1);
		fee = gameClient.getFeeForTileAtId(1);
		assertTrue(fee == 30);
		//get fee for tile 1 with 3 houses 
		gameClient.buyHouse(1);
		fee = gameClient.getFeeForTileAtId(1);
		assertTrue(fee == 90);
		//get fee for tile 1 with 4 houses 
		gameClient.buyHouse(1);
		fee = gameClient.getFeeForTileAtId(1);
		assertTrue(fee == 160);

	}
}