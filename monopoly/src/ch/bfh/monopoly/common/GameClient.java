package ch.bfh.monopoly.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.mina.core.session.IoSession;

import ch.bfh.monopoly.net.Messages;
import ch.bfh.monopoly.net.NetMessage;
import ch.bfh.monopoly.observer.WindowListener;
import ch.bfh.monopoly.observer.WindowSubject;
import ch.bfh.monopoly.tile.IProperty;
import ch.bfh.monopoly.tile.Property;

public class GameClient {

	private Player currentPlayer;
	private Player bank;
	private Locale loc;
	private Board board;
	private IoSession session;
	private WindowSubject ws;

	private class ConcreteSubject implements WindowSubject {

		public ConcreteSubject() {
		}

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

	public GameClient(Locale loc) {
		ws = new ConcreteSubject();
		bank = new Player("bank", 100000000);
		this.loc = loc;
		this.board = new Board(this);
	}

	public Locale getLoc() {
		return loc;
	}

	/**
	 * calculate the rent of a property, if a player lands on it
	 * 
	 * @param tileID
	 *            the tile number of the property
	 */
	public int getFeeForTileAtId(int tileId) {
		IProperty p = (IProperty) board.getTileById(tileId);
		return p.feeToCharge();
	}

	public void setIoSession(IoSession session) {
		this.session = session;
	}

	/**
	 * TODO ONLY FOR TESTING, REMOVE OR COMMENT OUT FOR FINAL PRODUCT
	 */
	public Board getBoard() {
		return this.board;
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(Player p) {
		currentPlayer = p;
	}

	/**
	 * advance the current player a given number n spaces forward
	 */
	public void advanceCurrentPlayerNSpaces(int n) {
		int currentPos = currentPlayer.getPosition();
		currentPlayer.setPosition((currentPos + n) % 40);
	}

	/**
	 * buy a house for a given property
	 * 
	 * @param tileID
	 *            the tile number of the property to build a house on
	 */
	public void buyHouse(int tileID) {
		board.buyHouse(tileID);
		NetMessage nm = new NetMessage(currentPlayer, tileID,
				Messages.BUY_HOUSE);
		session.write(nm);
	}

	/**
	 * transfers a given property from one player to another
	 * 
	 * @param fromName
	 *            name of the player to take property from
	 * @param toName
	 *            name of the player to give the property to
	 * @param price
	 *            the price the property has been sold for
	 * @param propertyID
	 *            the tile number of the property that was sold
	 */
	public void buyPropertyFromPlayer(String fromName, String toName,
			int propertyID, int price) {
		if (board.playerHasSufficientFunds(fromName, price)) {
			board.transferProperty(fromName, toName, propertyID);
			board.transferMoney(fromName, toName, price);
		}
		else throw new RuntimeException("Player doesn't have enough money");

	}

	/**
	 * get the description of the event for the tile on which the current player
	 * resides
	 */
	public String getEventDescription() {
		int currentPos = currentPlayer.getPosition();
		String eventDescription = board.getTileById(currentPos)
				.getEventDescription();
		return eventDescription;
	}

	/**
	 * perform the event for the tile that the current player occupies
	 */
	public void performEvent() {
		int currentPos = currentPlayer.getPosition();
		board.getTileById(currentPos).performEvent();
	}

	/**
	 * checks if a given player is the owner of a given tile
	 * 
	 * @param playerName
	 *            the player's name to check
	 * @param tileId
	 *            tile to check ownership of
	 */
	public boolean playerIsOwnerOfTile(String playerName, int tileId) {
		return board.playerIsOwnerOfTile(playerName, tileId);
	}

	/**
	 * checks if the current player has sufficient funds to pay a fee
	 * 
	 * @param fee
	 *            the amount of the fee to be paid
	 */
	public boolean hasSufficientFunds(int fee) {
		return board.playerHasSufficientFunds(currentPlayer.getName(), fee);
	}

	/**
	 * checks if the current player has sufficient funds to pay a fee
	 * 
	 * @param playerName
	 *            the player to check the account of
	 * @param fee
	 *            the amount of the fee to be paid
	 */
	public boolean playerHasSufficientFunds(String playerName, int fee) {
		return board.playerHasSufficientFunds(playerName, fee);
	}

	/**
	 * get the bank player
	 * 
	 * @return bank player
	 */
	public Player getBankPlayer() {
		return bank;
	}

	/**
	 * checks if the utilities are owned by the same player this is used by the
	 * PayUtilityEvent to calculate the fee to charge
	 * 
	 * @return true if both utility tiles are owned by the same player
	 */
	public boolean hasBothUtilities() {
		String ownerOfUtility1 = ((Property) board.getTileById(12)).getOwner()
				.getName();
		String ownerOfUtility2 = ((Property) board.getTileById(28)).getOwner()
				.getName();
		return ownerOfUtility1.equalsIgnoreCase(ownerOfUtility2);
	}

	/**
	 * the current player buys a given property this method is called when the
	 * property is owned by the bank
	 * 
	 * @param tileId
	 *            the id of the tile to be bought
	 */
	public void buyPropertyFromBank(int tileId) {
		board.buyPropertyFromBank(currentPlayer.getName(), tileId);
	}

	/**
	 * the current player is charged a fee and the amount of the fee is
	 * withdrawn from his bank account. This amount is added to the FREE PARKING
	 * 
	 * @param fee
	 *            the amount of money to withdraw from the current player's
	 *            account
	 */
	public void payFee(int fee) {
		currentPlayer.withdawMoney(fee);
		int freeParking = board.getFreeParking();
		board.setFreeParking(freeParking + fee);
	}

	/**
	 * The current player is charged a given fee, which is withdrawn from his
	 * account the amount of the fee is then credited to the toPlayer's account
	 * 
	 * @param toPlayer
	 *            the name of hte player to give the money to
	 * @param fee
	 *            the amount of the fee to charge the current player
	 */
	public void payFeeToPlayer(String toPlayer, int fee) {
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
	 * 
	 * @param s
	 *            the message
	 */
	public void sendChatMessage(String s) {
		String text = board.getLocalPlayer().getName().concat(": " + s + "\n");
		NetMessage nm = new NetMessage(text, Messages.CHAT_MSG);
		session.write(nm);
	}

	public void displayChat(String text) {
		ws.notifyListeners(text);
	}

	public WindowSubject getWindowSubject() {
		return ws;
	}

	
	/**
	 * Set the list of player in the game and create
	 * the relative player instance
	 * @param names the list of names of the player
	 */
	public void setUsersList(List<String> names, String localPlayerName){
		board.createPlayers(names, loc, localPlayerName);
	}
	
	/**
	 * Get the local player
	 * @return Player
	 * 			the local player
	 */
	public Player getLocalPlayer(){
		return board.getLocalPlayer();
	}
	
	/**
	 * Get the number of available houses 
	 * used by events to calculate the price a player must pay for REPAIRS EVENT
	 * @return number of available houses
	 */
	public int getAvailableHouses(){
		return board.getAvailableHouses();
	}
	
	/**
	 * Get the number of available hotels 
	 * used by events to calculate the price a player must pay for REPAIRS EVENT
	 * @return number of available hotels
	 */
	public int getAvailableHotels(){
		return board.getAvailableHotels();
	}
	
	/**
	 * get the list of players in the game, used to by the BIRTHDAY EVENT to transfer $10 to the player with the BIRTHDAY!
	 * @return list of players
	 */
	public List<Player> getPlayers(){
		return board.getPlayers();
	}
	
	
}
