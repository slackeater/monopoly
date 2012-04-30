package ch.bfh.monopoly.common;

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
	
	
	public void sendChatMessage(String s){
		gameClient.sendChatMessage(s);
	}
	public WindowSubject getWindowSubject(){
		return gameClient.getWindowSubject();
	}
}
