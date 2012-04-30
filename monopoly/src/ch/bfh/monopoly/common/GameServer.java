package ch.bfh.monopoly.common;

import java.util.ArrayList;

public class GameServer {
	
	private Board board;
	private ArrayList<String> players;
	
	public GameServer (){
	}
	
	public void startGame(){
		Player[] playerArray = (Player[])players.toArray();
		//sends netMessage containing list of player names to all clients participating in the game
	}
}
