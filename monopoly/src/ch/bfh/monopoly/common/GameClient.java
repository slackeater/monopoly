package ch.bfh.monopoly.common;

import java.util.Locale;

import ch.bfh.monopoly.network.Network;


public class GameClient {
	
	Player localPlayer;
	Player currentPlayer;
	Network communicate;
	Locale loc;
	Board board;
	
	public GameClient () {
		
		//loc should be received from a netmessage sent when the server calls startGame();
		loc = new Locale("EN");
	}
	
	public void initBoard(){
		board = new Board(loc, this);
		//a net message should come with the list of player names
		//until then we have a mock object full of player names
		String[] playerNames = {"Justin","Giuseppe","Damien","Cyril","Elie"};
		board.createPlayers(playerNames);
	}

	public Locale getLoc(){
		return loc;
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	
}
