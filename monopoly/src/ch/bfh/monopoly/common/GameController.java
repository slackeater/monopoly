package ch.bfh.monopoly.common;

import java.util.List;

import ch.bfh.monopoly.observer.WindowSubject;

public class GameController {

	GameClient gameClient;
	
	public GameController(GameClient gameClient){
		this.gameClient = gameClient;
	}
	
	/**
	 * buy the current property for the currentPlayer where the current player is located. 
	 */
	public void buyCurrentPropertyForPlayer(String playerName){
		gameClient.buyCurrentPropertyForPlayer(playerName);
	}
	
	/**
	 * buy/build a house on the given tile 
	 * @param tileID the id corresponding to the tile
	 */
	public void buyHouse(int tileId){
		gameClient.buyHouse(tileId);
	}
	
	/**
	 * buy/build a hotel on the given tile 
	 * @param tileID the id corresponding to the tile
	 */
	public void buyHotel(int tileId){
		gameClient.buyHotel(tileId);
	}
	

	/**
	 * sell a house from the given tile 
	 * @param tileID the id corresponding to the tile
	 */
	public void sellHouse(int tileId){
		gameClient.sellHouse(tileId);
	}
	
	
	/**
	 * sell a hotel from the given tile 
	 * @param tileID the id corresponding to the tile
	 */
	public void sellHotel(int tileId){
		gameClient.sellHotel(tileId);
	}
	
	/**
	 * Toggles the mortgage status of a given property
	 * @param tileId the id that corresponds to a tile for which we want to toggle the mortgage status.
	 */
	public void toggleMortgageStatus(int tileId){
		gameClient.toggleMortgageStatus(tileId);
	}
	
	/**
	 * transfer property from one player to another.  the string "CurrentPlayer" should be used to represent the currentPlayer.
	 * this method just calls the local method  transferPropertyForPrice with the added property of price = 0
	 * @param fromPlayer the name of the player to transfer the property from
	 * @param toPlayer the name of the player to transfer the property to
	 * @param tileId the integer number which represent the tile to be transfered
	 */
	public void transferProperty(String fromPlayer, String toPlayer, int tileId){
		transferPropertyForPrice(fromPlayer, toPlayer, tileId,0);
	}
	
	/**
	 * transfer property from one player to another for a given price.  the string "CurrentPlayer" should be used to represent the currentPlayer.
	 * @param fromPlayer the name of the player to transfer the property from
	 * @param toPlayer the name of the player to transfer the property to
	 * @param tileId the integer number which represent the tile to be transfered
	 */
	public void transferPropertyForPrice(String fromPlayer, String toPlayer, int tileId, int price){
		gameClient.transferProperty(fromPlayer, toPlayer, tileId, price);
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
	
	/** TODO => Maybe is useless
	 * Get the name of the local player
	 * @return String
	 * 			the name of the local player
	 */
	public String getLocalPlayerName(){
		return gameClient.getLocalPlayer().getName();
	}

	public Player getLocalPlayer(){
		return gameClient.getLocalPlayer();
	}
	
	public WindowSubject getWindowSubject(){
		return gameClient.getWindowSubject();
	}
	
	
}
