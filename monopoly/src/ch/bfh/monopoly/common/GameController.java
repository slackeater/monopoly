package ch.bfh.monopoly.common;

import java.util.List;

import ch.bfh.monopoly.observer.WindowSubject;

public class GameController {

	GameClient gameClient;
	
	public GameController(GameClient gameClient){
		this.gameClient = gameClient;
	}
	
	/**
	 * buy/build a house on the given tile 
	 * @param tileID the id corresponding to the tile
	 */
	public void buyHouse(int tileId){
		gameClient.buyHouse(tileId);
	}
	
	/**
	 * advance the current player the number of spaces n
	 * @param n
	 */
	public void advancePlayerNSpaces(int n){
		gameClient.advanceCurrentPlayerNSpaces(n);
	}
	
	
	/**
	 * get the description of the event for the tile on which the current player resides
	 */
	public String getEventDescription(){
		return gameClient.getEventDescription();
	}
	
	/**
	 * perform the event for the tile that the current player occupies
	 */
	public void performEvent(){
		gameClient.performEvent();
	}
	
	/**
	 * Send a chat message
	 * @param s 
	 * 			the message to be sent
	 */
	public void sendChatMessage(String s){
		gameClient.sendChatMessage(s);
	}
	
	/**
	 * Get the name of the local player
	 * @return String
	 * 			the name of the local player
	 */
	public String getLocalPlayerName(){
		return gameClient.getLocalPlayer().getName();
	}
	
	public List<Player> getPlayers(){
		return gameClient.getPlayers();
	}
	
	public WindowSubject getWindowSubject(){
		return gameClient.getWindowSubject();
	}
	
	
}
