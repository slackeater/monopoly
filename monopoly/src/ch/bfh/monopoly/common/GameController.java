		package ch.bfh.monopoly.common;

import java.util.Locale;
import javax.swing.JPanel;

import ch.bfh.monopoly.net.Messages;
import ch.bfh.monopoly.net.NetMessage;
import ch.bfh.monopoly.observer.TradeInfoEvent;
import ch.bfh.monopoly.observer.WindowMessage;
import ch.bfh.monopoly.observer.WindowStateEvent;
import ch.bfh.monopoly.observer.WindowSubject;

public class GameController {

	GameClient gameClient;

	public GameController(GameClient gameClient) {
		this.gameClient = gameClient;
	}

	/**
	 * Get the JPanel to start the turn
	 * 
	 * @return the JPanel that the GUI will display
	 */
	public JPanel getStartTurnPanel() {
		return gameClient.getStartTurnPanel(true);
	}
	

	/**
	 * Get the JPanel for the tile's event. Should be called when a player rolls
	 * and lands on a new tile

	 * @return the JPanel that the GUI will display
	 */
	public JPanel getTileEventPanel() {
		return gameClient.getTileEventPanel(true);
	}

	/**
	 * buy the current property for the currentPlayer where the current player
	 * is located.
	 * 
	 * @param the
	 *            name of the player to but a property for. Giving
	 *            "currentPlayer" will buy the property for the current player
	 */
	public void buyCurrentPropertyForPlayer(String playerName) {
		gameClient.buyCurrentPropertyForPlayer(playerName, true);
	}

	/**
	 * buy/build a house on the given tile
	 * 
	 * @param tileID
	 *            the id corresponding to the tile
	 */
	public void buyHouse(int tileId) {
		gameClient.localPlayerCanCallMethods();
		gameClient.buyHouse(tileId, true);
	}

	/**
	 * buy 1 house for each property belonging to a group
	 * @param tileId the id of any tile in the group to build on
	 */
	public void buyHouseRow(int tileId) {
		gameClient.localPlayerCanCallMethods();
		gameClient.buyHouseRow(tileId, true);
	}

	/**
	 * buy/build a hotel on the given tile
	 * 
	 * @param tileID
	 *            the id corresponding to the tile
	 */
	public void buyHotel(int tileId) {
		gameClient.buyHotel(tileId, true);
	}

	/**
	 * buy 1 hotel for each property belonging to a group
	 * @param tileId the id of any tile in the group to build on
	 */
	public void buyHotelRow(int tileId) {
		gameClient.localPlayerCanCallMethods();
		gameClient.buyHotelRow(tileId, true);
	}
	
	/**
	 * sell a house from the given tile
	 * 
	 * @param tileID
	 *            the id corresponding to the tile
	 */
	public void sellHouse(int tileId) {
		gameClient.localPlayerCanCallMethods();
		gameClient.sellHouse(tileId, true);
	}

	/**
	 * sell 1 house for each property belonging to a group
	 * @param tileId the id of any tile in the group to sell from
	 */
	public void sellHouseRow(int tileId) {
		gameClient.sellHouseRow(tileId, true);
	}
	
	/**
	 * sell a hotel from the given tile
	 * 
	 * @param tileID
	 *            the id corresponding to the tile
	 */
	public void sellHotel(int tileId) {
		gameClient.localPlayerCanCallMethods();
		gameClient.sellHotel(tileId, true);
	}

	/**
	 * sell 1 hotel for each property belonging to a group
	 * @param tileId the id of any tile in the group to sell from
	 */
	public void sellHotelRow(int tileId) {
		gameClient.localPlayerCanCallMethods();
		gameClient.sellHotelRow(tileId, true);
	}
	
	/**
	 * Toggles the mortgage status of a given property and credits or debits the
	 * player's account
	 * 
	 * @param tileId
	 *            the id that corresponds to a tile for which we want to toggle
	 *            the mortgage status.
	 */
	public void toggleMortgageStatus(int tileId) {
		gameClient.localPlayerCanCallMethods();
		gameClient.toggleMortgageStatus(tileId, true);
	}

	/**
	 * transfer property from one player to another. the string "CurrentPlayer"
	 * should be used to represent the currentPlayer. this method just calls the
	 * local method transferPropertyForPrice with the added parameter of price =
	 * 0
	 * 
	 * @param fromName
	 *            the name of the player to transfer the property from
	 * @param toName
	 *            the name of the player to transfer the property to
	 * @param tileId
	 *            the integer number which represent the tile to be transfered
	 */
	public void transferProperty(String fromName, String toName, int tileId) {
		transferPropertyForPrice(fromName, toName, tileId, 0);
	}

	/**
	 * transfer property from one player to another for a given price. the
	 * string "CurrentPlayer" should be used to represent the currentPlayer.
	 * 
	 * @param fromName
	 *            the name of the player to transfer the property from
	 * @param toName
	 *            the name of the player to transfer the property to
	 * @param tileId
	 *            the integer number which represent the tile to be transfered
	 */
	public void transferPropertyForPrice(String fromName, String toName,
			int tileId, int price) {
		gameClient.transferProperty(fromName, toName, tileId, price, true);
	}

	/**
	 * transfer "get out of jail card" from one player to another for a given
	 * price. the string "CurrentPlayer" should be used to represent the
	 * currentPlayer. this method just calls the local method
	 * transferJailCardsForPrice with the added parameter of price = 0
	 * 
	 * @param fromName
	 *            the name of the player to transfer the card from
	 * @param toName
	 *            the name of the player to transfer the card to
	 * @param quantity
	 *            the number of cards that should be transfered. In a rare case
	 *            it is possible that this is greater than 1.
	 */
	public void transferJailCards(String fromName, String toName, int quantity) {
		transferJailCardsForPrice(fromName, toName, quantity, 0);
	}

	/**
	 * transfer "get out of jail card" from one player to another for a given
	 * price. the string "CurrentPlayer" should be used to represent the
	 * currentPlayer.
	 * 
	 * @param fromName
	 *            the name of the player to transfer the card from
	 * @param toName
	 *            the name of the player to transfer the card to
	 * @param quantity
	 *            the number of cards that should be transfered. In a rare case
	 *            it is possible that this is greater than 1.
	 */
	public void transferJailCardsForPrice(String fromName, String toName,
			int quantity, int price) {
		gameClient.transferJailCards(fromName, toName, quantity, price, true);
	}

	/**
	 * transfer money from one player to another
	 * 
	 * @param fromName
	 *            the name of the player to withdraw money from
	 * @param toName
	 *            the name of the player to deposit money to
	 * @param amount
	 *            the amount of money to be transfered
	 */
	public void transferMoney(String fromName, String toName, int amount) {
		gameClient.transferMoney(fromName, toName, amount, true);
	}

	/**
	 * advance the current player the number of spaces n
	 * 
	 * @param n
	 *            is the number of spaces to advance the currentPlayer on the
	 *            game board
	 */
	public void advancePlayerNSpaces(int n) {
		gameClient.advancePlayerNSpaces(n, true);
	}

	/**
	 * Send a chat message
	 * 
	 * @param s
	 *            the message to be sent
	 */
	public void sendChatMessage(String s) {
		gameClient.sendChatMessage(s);
	}

	/**
	 * Get the name of the local player
	 * 
	 * @return String the name of the local player
	 */
	public String getLocalPlayerName() {
		return gameClient.getLocalPlayer();
	}

	/**
	 * get the subject to register window listeners
	 * 
	 * @return the WindowSubject to register listeners in
	 */
	public WindowSubject getWindowSubject() {
		return gameClient.getWindowSubject();
	}

	/**
	 * Get the locale of the game
	 * 
	 * @return Locale the locale used in this game
	 */
	public Locale getLocale() {
		return gameClient.getLoc();
	}

	/**
	 * end the turn for the current player
	 */
	public void endTurn() {
		gameClient.endTurn();
	}

	/**
	 * send a trade request to a player
	 */
	public void sendTradeRequestToPlayer(String playerName, TradeInfoEvent tie) {
		System.out.println("SEND TRADE GController");
		gameClient.sendTradeRequestToPlayer(playerName, tie);
		
	}


	/**
	 * confirm/reject a trade request that you have received
	 */
	public void sendTradeAnswer(boolean answer) {
		gameClient.sendTradeAnswer(answer);

	}


	/**
	 * returns the name of the current player
	 */
	public String getCurrentPlayerName(){
		return gameClient.getCurrentPlayer().getName();
	}
	
	/**
	 * send a message when the player quit the game
	 */
	public void sendQuitGame() {
		gameClient.sendQuitGame();
	}
	
	/**
	 * create a motion to kick a player
	 * @param the name of the player who might be kicked out of the game
	 */
	public void createKickRequest(String playerName){
		gameClient.createKickRequest(playerName);
	}
	
	/**
	 * send a your vote in response to a kick request
	 */
	public void sendKickVote(boolean kick){
		gameClient.sendKickVote(kick);
	}

}
