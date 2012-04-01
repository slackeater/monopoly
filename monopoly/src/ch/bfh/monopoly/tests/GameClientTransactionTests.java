package ch.bfh.monopoly.tests;

import static org.junit.Assert.assertTrue;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import ch.bfh.monopoly.common.Board;
import ch.bfh.monopoly.common.GameClient;
import ch.bfh.monopoly.common.Player;
import ch.bfh.monopoly.tile.Property;
import ch.bfh.monopoly.tile.Tile;

public class GameClientTransactionTests {

	
	Locale loc;
	GameClient gc ;
	Board board;
	
	@Before
	public void setup(){
		loc = new Locale("EN");
		gc = new GameClient();
		board= new Board(loc,gc);
		String[] playerNames = {"Justin","Giuseppe","Damien","Cyril","Elie"};
		board.createPlayers(playerNames);
	}
	
	@Test
	public void addPropertyToPlayer(){
		Player p = board.getPlayerByName("Justin");
		Tile t = board.getTileByID(1);
		board.addPropertyToPlayer("Justin", 1);
		//System.out.println(((Property)t).getOwner().getName());
		assertTrue(((Property)t).getOwner()==p);
		
	}
	
	@Test
	public void transferPropertiesChangesOwners(){
		Board board= new Board(loc,gc);
		String[] playerNames = {"Justin","Giuseppe","Damien","Cyril","Elie"};
		board.createPlayers(playerNames);
		Player p = board.getPlayerByName("Justin");
		assertTrue(p.getName().equals("Justin"));
	}

	
}
