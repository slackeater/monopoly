package common;

import java.util.Locale;

import network.Network;
import network.NetworkClient;

public class GameClient {
	
	Player localPlayer;
	Player currentPlayer;
	NetworkClient connection;
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
	
	private void throwDice (){
		/**
		 * generate random number
		 * show dice rolling
		 * GUI shows user the number
		 * create net message "PlayerX Rolled 8"
		 * localPlayer.setPosition(+8)
		*/ 
	}
	

	


	
}
