package common;

import java.util.Locale;

import network.Network;

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
	}

	public Locale getLoc(){
		return loc;
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	
}
