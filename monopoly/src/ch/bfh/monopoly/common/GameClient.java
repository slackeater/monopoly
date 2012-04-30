package ch.bfh.monopoly.common;

import java.util.ArrayList;
import java.util.Locale;

import org.apache.mina.core.session.IoSession;

import ch.bfh.monopoly.net.Messages;
import ch.bfh.monopoly.net.NetMessage;
import ch.bfh.monopoly.tile.IProperty;
import ch.bfh.monopoly.tile.Property;


public class GameClient {
	
	private Player localPlayer;
	private Player currentPlayer;
	private Player bank;
	private Locale loc;
	private Board board;
	private IoSession session;
	private WindowSubject ws;
	
	
	private class ConcreteSubject implements WindowSubject {

		public ConcreteSubject() {}

		ArrayList<WindowListener> listeners = new ArrayList<WindowListener>();

		public void addListener(WindowListener wl) {
			listeners.add(wl);
		}
		@Override
		public void removeListener(WindowListener wl) {
			listeners.remove(wl);
		}

		public void notifyListeners(String text) {
			
			
			for (WindowListener pl : listeners) {
				pl.updateWindow(text);
			}
		}
	}
	


	
	public GameClient (Locale loc) {
		ws = new ConcreteSubject();
		bank = new Player("bank", 100000000);
		//loc should be received from a netmessage sent when the server calls startGame();
		this.loc = loc;
			
		//TODO this list must be received from a netMessage when the game starts
		String[] playerNames = {"Justin","Giuseppe","Damien","Cyril","Elie"};
		board = new Board(this);
		board.createPlayers(playerNames, loc);
	}
	

	public Locale getLoc(){
		return loc;
	}
	
	/**
	 * calculate the rent of a property, if a player lands on it
	 * @param tileID the tile number of the property
	 */
	public int getFeeForTileAtId(int tileId){
		IProperty p = (IProperty)board.getTileById(tileId);
		return p.feeToCharge();
	}

	public void setIoSession(IoSession session){
		this.session = session;
	}
	
	/**
	 * TODO  ONLY FOR TESTING, REMOVE OR COMMENT OUT FOR FINAL PRODUCT
	 */
	public Board getBoard(){
		return this.board;
	}
	
	public Player getCurrentPlayer() {
		return currentPlayer;
	}
	
	public void setCurrentPlayer(Player p){
		currentPlayer = p;
	}
	
	/**
	 * advance the current player a given number n spaces forward
	 */
	public void advanceCurrentPlayerNSpaces(int n){
		int currentPos = currentPlayer.getPosition();
		currentPlayer.setPosition((currentPos + n)%40);
	}
	
	
	/**
	 * buy a house for a given property
	 * @param tileID the tile number of the property to build a house on
	 */
	public void buyHouse(int tileID){
		board.buyHouse(tileID);
		NetMessage nm = new NetMessage(this.localPlayer, tileID, Messages.BUY_HOUSE);
		session.write(nm);
	}
	
	/**
	 * transfers a given property from one player to another
	 * @param fromName name of the player to take property from
	 * @param toName name of the player to give the property to
	 * @param price the price the property has been sold for
	 * @param propertyID the tile number of the property that was sold
	 */
	public void transferPropertyForPrice(String fromName, String toName, int propertyID, int price){
		board.transferProperty(fromName, toName, propertyID);
		board.transferMoney(fromName, toName, price);
	}

	
	/**
	 * get the description of the event for the tile on which the current player resides
	 */
	public String getEventDescription(){
		int currentPos = currentPlayer.getPosition();
		String eventDescription =board.getTileById(currentPos).getEventDescription();
		return eventDescription;
	}
	
	/**
	 * perform the event for the tile that the current player occupies
	 */
	public void performEvent(){
		int currentPos = currentPlayer.getPosition();
		board.getTileById(currentPos).performEvent();
	}
	
	/**
	 * checks if a given player is the owner of a given tile
	 * @param playerName  the player's name to check
	 * @param tileId  tile to check ownership of
	 */
	public boolean playerIsOwnerOfTile(String playerName, int tileId){
		return board.playerIsOwnerOfTile(playerName, tileId);
	}
	
	/**
	 * checks if the current player has sufficient funds to pay a fee
	 * @param fee  the amount of the fee to be paid
	 */
	public boolean hasSufficientFunds(int fee){
		return board.playerHasSufficientFunds(currentPlayer.getName(), fee);
	}
	
	/**
	 * checks if the current player has sufficient funds to pay a fee
	 * @param playerName the player to check the account of
	 * @param fee  the amount of the fee to be paid
	 */
	public boolean playerHasSufficientFunds(String playerName, int fee){
		return board.playerHasSufficientFunds(playerName, fee);
	}
	
	/**
	 * get the bank player
	 * @return bank player
	 */
	public Player getBankPlayer(){
		return bank;
	}
	
	/**
	 * checks if the utilities are owned by the same player
	 * this is used by the PayUtilityEvent to calculate the fee to charge
	 * @return true if both utility tiles are owned by the same player
	 */
	public boolean hasBothUtilities(){
		String ownerOfUtility1 = ((Property)board.getTileById(12)).getOwner().getName();
		String ownerOfUtility2 = ((Property)board.getTileById(28)).getOwner().getName();
		return ownerOfUtility1.equalsIgnoreCase(ownerOfUtility2);
	}
	
	/**
	 * the current player buys a given property
	 * this method is called when the property is owned by the bank
	 * @param tileId the id of the tile to be bought
	 */
	public void buyPropertyFromBank(int tileId){
		board.buyPropertyFromBank(currentPlayer.getName(), tileId);
	}
	
	/**
	 * the current player is charged a fee and the amount of the fee is 
	 * withdrawn from his bank account.  This amount is added to the FREE PARKING
	 * @param fee the amount of money to withdraw from the current player's account
	 */
	public void payFee(int fee){
		currentPlayer.withdawMoney(fee);
		int freeParking = board.getFreeParking();
		board.setFreeParking(freeParking+fee);
	}
	
	/**
	 * The current player is charged a given fee, which is withdrawn from his account
	 * the amount of the fee is then credited to the toPlayer's account
	 * @param toPlayer the name of hte player to give the money to
	 * @param fee the amount of the fee to charge the current player
	 */
	public void payFeeToPlayer(String toPlayer, int fee){
		currentPlayer.withdawMoney(fee);
		board.getPlayerByName(toPlayer).depositMoney(fee);
	}
	
	public int getFreeParking() {
		return board.getFreeParking();
	}

	public void setFreeParking(int amount) {
		board.setFreeParking(amount);
	}
	
	/**
	 * Send a chat message
	 * @param s the message
	 */
	public void sendChatMessage(String s){
		String text = this.localPlayer.getName().concat(" " + s);
		NetMessage nm = new NetMessage(text, Messages.CHAT_MSG);
		session.write(nm);
	}
	
	public void displayChat(String text){
		ws.notifyListeners(text);
	}
	
	public WindowSubject getWindowSubject(){
		return ws;
	}
	
	
}
