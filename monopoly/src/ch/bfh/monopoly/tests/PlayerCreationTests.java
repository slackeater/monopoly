package ch.bfh.monopoly.tests;

import static org.junit.Assert.assertTrue;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import ch.bfh.monopoly.common.Board;
import ch.bfh.monopoly.common.GameClient;
import ch.bfh.monopoly.common.Player;
import ch.bfh.monopoly.tile.Tile;

public class PlayerCreationTests {

	
	
	Locale loc;
	GameClient gc ;
	
	@Before
	public void setup(){
		loc = new Locale("EN");
		gc = new GameClient();
	}
	
	
	@Test
	public void playerObjectsHaveCorrentNames(){
		Board board= new Board(loc,gc);
		String[] playerNames = {"Justin","Giuseppe","Damien","Cyril","Elie"};
		board.createPlayers(playerNames);
		Player p = board.getPlayerByName("Justin");
		assertTrue(p.getName().equals("Justin"));
	}
	
	@Test
	public void playerJailStatusChanges(){
		Board board= new Board(loc,gc);
		String[] playerNames = {"Justin","Giuseppe","Damien","Cyril","Elie"};
		board.createPlayers(playerNames);
		Player p = board.getPlayerByName("Justin");
		p.setInJail(true);
		assertTrue(p.isInJail());
	}
	
}
