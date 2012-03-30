package ch.bfh.monopoly.common;

import java.util.ArrayList;

public class GameServer {
	
	// TODO: decide where "state" variables should be stored... here or in a StateManager class?
	//These variables might be better in StateManager, because they have less to do with GameServing
	//and more to do with the State of the game or game play
	private int availableHouses;
	private int availableHotels;
	private final int START_MONEY_US = 5000;
	private final int START_MONEY_SWISS = 200000;
	
	private Board sm;
	private ArrayList<Player> players;
	
	public GameServer (){
	}
	
	//as the players join the game in the pregame phase their data are added to the arrayList players
	//when the game starts, this information is send to the stateManager in an ARRAY
	//the ArrayList in this class should be deleted to prevent problems
	public void addPlayer(Player p){
		players.add(p);
	}
	
	public void startGame(){
		Player[] playerArray = (Player[])players.toArray();
		sm = new Board(playerArray); 
	}
}
