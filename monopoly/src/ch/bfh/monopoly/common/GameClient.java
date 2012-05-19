package ch.bfh.monopoly.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.mina.core.session.IoSession;

import ch.bfh.monopoly.exception.TransactionException;
import ch.bfh.monopoly.net.Messages;
import ch.bfh.monopoly.net.NetMessage;
import ch.bfh.monopoly.observer.WindowListener;
import ch.bfh.monopoly.observer.WindowMessage;
import ch.bfh.monopoly.observer.WindowStateEvent;
import ch.bfh.monopoly.observer.WindowSubject;
import ch.bfh.monopoly.tile.IProperty;
import ch.bfh.monopoly.tile.Property;

public class GameClient {

	private Player currentPlayer;
	private String localPlayer;
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

		public void notifyListeners(WindowStateEvent wse) {

			for (WindowListener pl : listeners) {
				pl.updateWindow(wse);
			}
		}
	}

	public GameClient() {
		ws = new ConcreteSubject();
		bank = new Player("bank", 100000000, null);

	}

	/**
	 * create the board which in turn creates the tiles, events, and chance-type
	 * cards
	 * 
	 * @param loc
	 *            the locals chosen by the server
	 */
	public void createBoard(Locale loc, List<String> names,
			String localPlayerName) {
		this.loc = loc;
		this.board = new Board(this);
		board.createPlayers(names, loc);
		this.localPlayer = localPlayerName;
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

	/**
	 * Set the IoSession for this game client
	 * 
	 * @param session
	 *            IoSession the IoSession used to communicate with the server
	 */
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

	public void setCurrentPlayer(String playerName) {
		Player p = board.getPlayerByName(playerName);
		currentPlayer = p;
	}

	public void buyCurrentPropertyForPlayer(String playerName) {
		String playerNameAdjusted = adjustNameIfCurrentPlayer(playerName);

		try {
			board.buyCurrentPropertyForPlayer(playerNameAdjusted,
					currentPlayer.getPosition());
		} catch (TransactionException e) {
			sendTransactionErrorToGUI(e);
		}
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
		try {
			board.buyHouse(tileID);
			NetMessage nm = new NetMessage(currentPlayer.getName(), tileID,
					Messages.BUY_HOUSE);
			session.write(nm);
		} catch (TransactionException e) {
			sendTransactionErrorToGUI(e);
		}
	}

	/**
	 * buy a house for a given property
	 * 
	 * @param tileID
	 *            the tile number of the property to build a house on
	 */
	public void buyHotel(int tileID) {
		try {
			board.buyHotel(tileID);
			NetMessage nm = new NetMessage(currentPlayer.getName(), tileID,
					Messages.BUY_HOTEL);
			session.write(nm);
		} catch (TransactionException e) {
			sendTransactionErrorToGUI(e);
		}
	}

	/**
	 * sell a house for a given property
	 * 
	 * @param tileID
	 *            the tile number of the property to sell a house from
	 */
	public void sellHouse(int tileID) {
		try {
			board.sellHouses(tileID);
			NetMessage nm = new NetMessage(currentPlayer.getName(), tileID,
					Messages.SELL_HOUSE);
			session.write(nm);
		} catch (TransactionException e) {
			sendTransactionErrorToGUI(e);
		}

	}

	/**
	 * sell a hotel for a given property
	 * 
	 * @param tileID
	 *            the tile number of the property to sell a hotel from
	 */
	public void sellHotel(int tileID) {
		try {
			board.sellHotel(tileID);
			NetMessage nm = new NetMessage(currentPlayer.getName(), tileID,
					Messages.SELL_HOTEL);
			session.write(nm);
		} catch (TransactionException e) {
			sendTransactionErrorToGUI(e);
		}
	}

	/**
	 * Toggles the mortgage status of a given property
	 * 
	 * @param tileId
	 *            the id that corresponds to a tile for which we want to toggle
	 *            the mortgage status.
	 */
	public void toggleMortgageStatus(int tileId) {

		try {
			board.toggleMortgageStatus(tileId);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransactionException e) {
			sendTransactionErrorToGUI(e);
		}
		NetMessage nm = new NetMessage(currentPlayer.getName(), tileId,
				Messages.TOGGLE_MORTGAGE);
		session.write(nm);

		try {
			board.toggleMortgageStatus(tileId);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransactionException e) {
			sendTransactionErrorToGUI(e);
		}

		// TODO send a message for mortgage ; exception management
		// NetMessage nm = new NetMessage(currentPlayer.getName(), tileId,
		// Messages.TOGGLE_MORTGAGE);
		// session.write(nm);

	}

	/**
	 * transfer property from one player to another. the string "CurrentPlayer"
	 * should be used to represent the currentPlayer.
	 * 
	 * @param fromName
	 *            the name of the player to transfer the property from
	 * @param toName
	 *            the name of the player to transfer the property to
	 * @param tileId
	 *            the integer number which represent the tile to be transfered
	 */
	public void transferProperty(String fromName, String toName, int tileId,
			int price) {
		String fromNameAdjusted = adjustNameIfCurrentPlayer(fromName);
		String toNameAdjusted = adjustNameIfCurrentPlayer(toName);

		try {
			board.transferProperty(fromNameAdjusted, toNameAdjusted, tileId,
					price);
		} catch (TransactionException e) {
			sendTransactionErrorToGUI(e);
		}

	}

	/**
	 * transfer jail cards from one player to another. the string
	 * "CurrentPlayer" should be used to represent the currentPlayer.
	 * 
	 * @param fromName
	 *            the name of the player to transfer the card from
	 * @param toName
	 *            the name of the player to transfer the card to
	 * @param tileId
	 *            the integer number which represent the tile to be transfered
	 */
	public void transferJailCards(String fromName, String toName, int quantity,
			int price) {
		String fromNameAdjusted = adjustNameIfCurrentPlayer(fromName);
		String toNameAdjusted = adjustNameIfCurrentPlayer(toName);

		try {
			board.transferJailCards(fromNameAdjusted, toNameAdjusted, quantity,
					price);
		} catch (TransactionException e) {
			sendTransactionErrorToGUI(e);
		}

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
		String fromNameAdjusted = adjustNameIfCurrentPlayer(fromName);
		String toNameAdjusted = adjustNameIfCurrentPlayer(toName);

		try {
			board.transferMoney(fromNameAdjusted, toNameAdjusted, amount);
		} catch (TransactionException e) {
			sendTransactionErrorToGUI(e);
		}
	}

	public String adjustNameIfCurrentPlayer(String playerName) {
		String adjustedName = playerName;
		if (playerName.equalsIgnoreCase("currentPlayer"))
			adjustedName = currentPlayer.getName();
		return adjustedName;
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
		boolean isOwner = false;
		try {
			isOwner = board.playerIsOwnerOfTile(playerName, tileId);
		} catch (TransactionException e) {
			sendTransactionErrorToGUI(e);
		}
		return isOwner;
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
	public boolean playerHasSufficientFunds(String playerName, int amount) {
		String playerNameAdjusted = adjustNameIfCurrentPlayer(playerName);
		return board.playerHasSufficientFunds(playerNameAdjusted, amount);
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
	 * the current player is charged a fee and the amount of the fee is
	 * withdrawn from his bank account. This amount is added to the FREE PARKING
	 * 
	 * @param fee
	 *            the amount of money to withdraw from the current player's
	 *            account
	 */
	public void payFee(int fee) {
		String currentPlayerName = currentPlayer.getName();
		try {
			board.payFee(currentPlayerName, fee);
		} catch (TransactionException e) {
			WindowStateEvent wse = new WindowStateEvent(
					WindowMessage.MSG_FOR_ERROR, e.getErrorMsg(), 0);
			ws.notifyListeners(wse);
		}
	}

	/**
	 * The current player is charged a given fee, which is withdrawn from his
	 * account the amount of the fee is then credited to the toName's account
	 * 
	 * @param toName
	 *            the name of hte player to give the money to
	 * @param fee
	 *            the amount of the fee to charge the current player
	 */
	public void payFeetoName(String toName, int fee) {
		try {
			currentPlayer.withdawMoney(fee);
		} catch (TransactionException e) {
			WindowStateEvent wse = new WindowStateEvent(
					WindowMessage.MSG_FOR_ERROR, e.getErrorMsg(), 0);
			ws.notifyListeners(wse);
		}
		board.getPlayerByName(toName).depositMoney(fee);
	}

	public int getFreeParking() {
		return board.getFreeParking();
	}

	/**
	 * Send a chat message
	 * 
	 * @param s
	 *            the message
	 */
	public void sendChatMessage(String s) {
		String text = localPlayer.concat(": " + s + "\n");
		NetMessage nm = new NetMessage(text, Messages.CHAT_MSG);
		session.write(nm);
	}

	/**
	 * method is called after reception of a netMessage and sets all Player's
	 * turn tokens to false, except the player whose name was received in the
	 * netMessage
	 * 
	 * @param name
	 *            of the player whose turn it is
	 */
	public void updateTurnTokens(String playerName) {
		currentPlayer.setTurnToken(false);
		board.getPlayerByName(playerName).setTurnToken(true);
	}

	/**
	 * send an array of integers which is the new order that cards should be
	 * drawn for chance card events
	 * 
	 * @param the
	 *            array of int values to be send to the server
	 */
	public void updateChanceDrawOrder(int[] newOrder) {
		NetMessage nm = new NetMessage(currentPlayer.getName(), 0,
				Messages.UPDATE_CHANCE_ORDER);
		nm.setDrawOrder(newOrder);
		session.write(nm);
	}

	/**
	 * send an array of integers which is the new order that cards should be
	 * drawn for chance card events
	 * 
	 * @param the
	 *            array of int values to be send to the server
	 */
	public void updateCommChestDrawOrder(int[] newOrder) {
		NetMessage nm = new NetMessage(currentPlayer.getName(), 0,
				Messages.UPDATE_COMMCHEST_ORDER);
		nm.setDrawOrder(newOrder);
		session.write(nm);
	}

	public void displayChat(String text) {
		WindowStateEvent wse = new WindowStateEvent(WindowMessage.MSG_FOR_CHAT,
				text, 0);
		ws.notifyListeners(wse);
	}

	public void displayTransactionError(String text) {
		WindowStateEvent wse = new WindowStateEvent(
				WindowMessage.MSG_FOR_ERROR, text, 0);
		ws.notifyListeners(wse);
	}

	public WindowSubject getWindowSubject() {
		return ws;
	}

	/**
	 * gathers transactions errors from the methods and forwards them to the GUI
	 */
	public void sendTransactionErrorToGUI(TransactionException e) {
		WindowStateEvent wse = new WindowStateEvent(
				WindowMessage.MSG_FOR_ERROR, e.getErrorMsg(), 0);
		ws.notifyListeners(wse);
	}

	/**
	 * gives the given player a jail card
	 * 
	 * @param String
	 *            the name of the player to change
	 */
	public void addJailCardToPlayer(String playerName) {
		String playerNameAdjusted = adjustNameIfCurrentPlayer(playerName);
		board.addJailCardToPlayer(playerNameAdjusted);
	}

	/**
	 * removes a jail card from the given player
	 * 
	 * @param String
	 *            the name of the player to change
	 */
	public void removeJailCardFromPlayer(String playerName) {
		String playerNameAdjusted = adjustNameIfCurrentPlayer(playerName);
		board.removeJailCardFromPlayer(playerNameAdjusted);
	}

	/**
	 * Get the local player
	 * 
	 * @return Player the local player
	 */
	public String getLocalPlayer() {
		return localPlayer;
	}

	/**
	 * Get the number of available houses used by events to calculate the price
	 * a player must pay for REPAIRS EVENT
	 * 
	 * @return number of available houses
	 */
	public int getAvailableHouses() {
		return board.getAvailableHouses();
	}

	/**
	 * Get the number of available hotels used by events to calculate the price
	 * a player must pay for REPAIRS EVENT
	 * 
	 * @return number of available hotels
	 */
	public int getAvailableHotels() {
		return board.getAvailableHotels();
	}

	/**
	 * get the list of players in the game, used to by the BIRTHDAY EVENT to
	 * transfer $10 to the player with the BIRTHDAY!
	 * 
	 * @return list of players
	 */
	public List<Player> getPlayers() {
		return board.getPlayers();
	}

	/**
	 * get the locale for the game
	 * 
	 * @return the locale for this game
	 */
	public Locale getLoc() {
		return loc;
	}

}
