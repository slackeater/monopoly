package common;

import java.util.ArrayList;

public class GameServer {
	
	private ArrayList<Player> players;
	
	public GameServer (){
	}
	
	//as the players join the game names are added to the arrayList players
	public void addPlayer(Player p){
		players.add(p);
	}
	
	public void startGame(){
		//TODO: send player list to all players
		
	}
}
