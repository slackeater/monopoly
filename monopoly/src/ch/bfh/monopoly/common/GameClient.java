package ch.bfh.monopoly.common;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
	private ClientNetworkController nc;
	private WindowSubject ws;

	/**
	 * a subject that is used in an observer pattern with the GUI information
	 * that must be displayer in the chat message window and in the game history
	 * or game message windows gets relayed by this class
	 */
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
	 * end the turn for the current player
	 */
	public void endTurn() {
		NetMessage nm = new NetMessage(Messages.END_TURN);
		nc.sendMessage(nm);

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
	public void setClientNetworkController(ClientNetworkController nCliCtrl) {
		this.nc = nCliCtrl;
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

	/**
	 * set current player by means of a reference to the current player object
	 * 
	 * @param p
	 *            the player object to be set to current player
	 * @param sendNetMessage
	 *            true if a net message should be sent for this change
	 * @param sendNetMessage
	 *            true if a net message should be sent to the server
	 */
	public void setCurrentPlayer(Player p, boolean sendNetMessage) {
		// TODO this method is never called by the GUI, and maybe doesn't need
		// the boolean parameter
		currentPlayer = p;
	}

	/**
	 * set the current player by means of a string with the name of the current
	 * player
	 * 
	 * @param playerName
	 *            the name of the player to be set to current player
	 * @param sendNetMessage
	 *            true if a net message should be sent for this change
	 */
	public void setCurrentPlayer(String playerName, boolean sendNetMessage) {
		// TODO this method is never called by the GUI, and maybe doesn't need
		// the boolean parameter
		Player p = board.getPlayerByName(playerName);
		currentPlayer = p;
	}

	public void buyCurrentPropertyForPlayer(String playerName,
			boolean sendNetMessage) {
		String playerNameAdjusted = adjustNameIfCurrentPlayer(playerName);

		try {
			board.buyCurrentPropertyForPlayer(playerNameAdjusted,
					currentPlayer.getPosition());
		} catch (TransactionException e) {
			sendTransactionErrorToGUI(e, true);
		}
	}

	/**
	 * advance the current player a given number n spaces forward
	 * 
	 * @param n
	 *            is the number of spaces to advance the player
	 * @param sendNetMessage
	 *            true if a net message should be sent to the server
	 */
	public void advancePlayerNSpaces(int n, boolean sendNetMessage) {
		String playerName = currentPlayer.getName();

		int previousPosition = currentPlayer.getPosition();
		board.advanceCurrentPlayerNSpaces(playerName, n);
		int newPosition = currentPlayer.getPosition();

		// if passes go
		if (newPosition < previousPosition)
			board.passGo(currentPlayer);

		if (sendNetMessage) {
			// send a netmessage with the roll value of this player
			NetMessage netMsg = new NetMessage(currentPlayer.getName(), n,
					Messages.DICE_ROLL);
			nc.sendMessage(netMsg);
		}
	}

	/**
	 * buy a house for a given property
	 * 
	 * @param tileID
	 *            the tile number of the property to build a house on
	 * @param sendNetMessage
	 *            true if a net message should be sent to the server
	 */
	public void buyHouse(int tileID, boolean sendNetMessage) {
		try {
			board.buyHouse(tileID);
		} catch (TransactionException e) {
			sendTransactionErrorToGUI(e, sendNetMessage);
		}
		if (sendNetMessage) {
			NetMessage netMsg = new NetMessage(currentPlayer.getName(), tileID,
					Messages.BUY_HOUSE);
			nc.sendMessage(netMsg);
		}

	}

	/**
	 * buy a house for a given property
	 * 
	 * @param tileID
	 *            the tile number of the property to build a house on
	 * @param sendNetMessage
	 *            true if a net message should be sent to the server
	 */
	public void buyHotel(int tileID, boolean sendNetMessage) {
		try {
			board.buyHotel(tileID);
		} catch (TransactionException e) {
			sendTransactionErrorToGUI(e, sendNetMessage);
		}
		if (sendNetMessage) {
			NetMessage netMsg = new NetMessage(currentPlayer.getName(), tileID,
					Messages.BUY_HOTEL);
			nc.sendMessage(netMsg);
		}
	}

	/**
	 * sell a house for a given property
	 * 
	 * @param tileID
	 *            the tile number of the property to sell a house from
	 * @param sendNetMessage
	 *            true if a net message should be sent to the server
	 */
	public void sellHouse(int tileID, boolean sendNetMessage) {
		try {
			board.sellHouses(tileID);
		} catch (TransactionException e) {
			sendTransactionErrorToGUI(e, sendNetMessage);
		}
		if (sendNetMessage) {
			NetMessage netMsg = new NetMessage(currentPlayer.getName(), tileID,
					Messages.SELL_HOUSE);
			nc.sendMessage(netMsg);
		}
	}

	/**
	 * sell a hotel for a given property
	 * 
	 * @param tileID
	 *            the tile number of the property to sell a hotel from
	 * @param sendNetMessage
	 *            true if a net message should be sent to the server
	 */
	public void sellHotel(int tileID, boolean sendNetMessage) {
		try {
			board.sellHotel(tileID);
		} catch (TransactionException e) {
			sendTransactionErrorToGUI(e, sendNetMessage);
		}
		if (sendNetMessage) {
			NetMessage netMsg = new NetMessage(currentPlayer.getName(), tileID,
					Messages.SELL_HOTEL);
			nc.sendMessage(netMsg);
		}
	}

	/**
	 * Toggles the mortgage status of a given property
	 * 
	 * @param tileId
	 *            the id that corresponds to a tile for which we want to toggle
	 *            the mortgage status.
	 * @param sendNetMessage
	 *            true if a net message should be sent to the server
	 */
	public void toggleMortgageStatus(int tileId, boolean sendNetMessage) {
		try {
			board.toggleMortgageStatus(tileId);
		} catch (TransactionException e) {
			sendTransactionErrorToGUI(e, sendNetMessage);
		}
		if (sendNetMessage) {
			NetMessage netMsg = new NetMessage(currentPlayer.getName(), tileId,
					Messages.TOGGLE_MORTGAGE);
			nc.sendMessage(netMsg);
		}
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
	 * @param price
	 *            the price agreed upon by the players for the transfer
	 * @param sendNetMessage
	 *            true if a net message should be sent to the server
	 */
	public void transferProperty(String fromName, String toName, int tileId,
			int price, boolean sendNetMessage) {
		String fromNameAdjusted = adjustNameIfCurrentPlayer(fromName);
		String toNameAdjusted = adjustNameIfCurrentPlayer(toName);

		try {
			board.transferProperty(fromNameAdjusted, toNameAdjusted, tileId,
					price);
		} catch (TransactionException e) {
			sendTransactionErrorToGUI(e, sendNetMessage);
		}
		
		if (sendNetMessage) {
			NetMessage netMsg = new NetMessage(currentPlayer.getName(), tileId,
					Messages.TOGGLE_MORTGAGE);
			nc.sendMessage(netMsg);
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
	 * @param quantity
	 *            the number of jail cards to transfer
	 * @param price
	 *            the price agreed upon by the players for the transfer
	 * @param sendNetMessage
	 *            true if a net message should be sent to the server
	 */
	public void transferJailCards(String fromName, String toName, int quantity,
			int price, boolean sendNetMessage) {
		String fromNameAdjusted = adjustNameIfCurrentPlayer(fromName);
		String toNameAdjusted = adjustNameIfCurrentPlayer(toName);

		try {
			board.transferJailCards(fromNameAdjusted, toNameAdjusted, quantity,
					price);
		} catch (TransactionException e) {
			sendTransactionErrorToGUI(e, sendNetMessage);
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
	 * @param sendNetMessage
	 *            true if a net message should be sent to the server
	 */
	public void transferMoney(String fromName, String toName, int amount,
			boolean sendNetMessage) {
		String fromNameAdjusted = adjustNameIfCurrentPlayer(fromName);
		String toNameAdjusted = adjustNameIfCurrentPlayer(toName);

		try {
			board.transferMoney(fromNameAdjusted, toNameAdjusted, amount);
		} catch (TransactionException e) {
			sendTransactionErrorToGUI(e, sendNetMessage);
		}
	}

	public String adjustNameIfCurrentPlayer(String playerName) {
		String adjustedName = playerName;
		if (playerName.equalsIgnoreCase("currentPlayer"))
			adjustedName = currentPlayer.getName();
		return adjustedName;
	}

	/**
	 * get the window builder object needed for the GUI to display a window in
	 * response to landing on a tile
	 * 
	 * @param sendNetMessage
	 *            true if a net message should be sent to the server
	 */
	public WindowBuilder getWindowBuilder(boolean sendNetMessage) {
		int currentPos = currentPlayer.getPosition();
		WindowBuilder wb = board.getWindowBuilderForTile(currentPos);
		return wb;
	}

	/**
	 * perform the event for the tile that the current player occupies
	 * 
	 * @param sendNetMessage
	 *            true if a net message should be sent to the server
	 */
	public void performEvent(boolean sendNetMessage) {
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
	 * @param sendNetMessage
	 *            true if a net message should be sent to the server
	 */
	public boolean playerIsOwnerOfTile(String playerName, int tileId) {
		boolean isOwner = false;
		isOwner = board.playerIsOwnerOfTile(playerName, tileId);
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
	 * the current player is charge the rent for the tile he is on the fee is
	 * withdrawn from his bank account and added to the tile owner's account
	 * 
	 * @param sendNetMessage
	 *            true if a net message should be sent to the server
	 */
	public void payRent(boolean sendNetMessage) {
		String currentPlayerName = currentPlayer.getName();
		int currentPosition = currentPlayer.getPosition();
		Property prop = board.castTileToProperty(board
				.getTileById(currentPosition));
		String owner = prop.getOwner().getName();
		int amount = getFeeForTileAtId(currentPosition);
		transferMoney(currentPlayerName, owner, amount, false);
		if (sendNetMessage) {
			NetMessage msg = new NetMessage(currentPlayer.getName(),
					Messages.PAY_RENT);
			sendNetMessageToGUI(msg);
		}
	}

	/**
	 * the current player is charged a fee and the amount of the fee is
	 * withdrawn from his bank account. This amount is added to the FREE PARKING
	 * 
	 * @param fee
	 *            the amount of money to withdraw from the current player's
	 *            account
	 * @param sendNetMessage
	 *            true if a net message should be sent to the server
	 */
	public void payFee(int fee, boolean sendNetMessage) {
		String currentPlayerName = currentPlayer.getName();
		try {
			board.payFee(currentPlayerName, fee);
		} catch (TransactionException e) {
			WindowStateEvent wse = new WindowStateEvent(
					WindowMessage.MSG_FOR_ERROR, e.getErrorMsg(), 0);
			ws.notifyListeners(wse);
		}
		if (sendNetMessage) {
			NetMessage msg = new NetMessage(currentPlayer.getName(),
					Messages.PAY_FEE);
			sendNetMessageToGUI(msg);
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
	 * @param sendNetMessage
	 *            true if a net message should be sent to the server
	 */
	public void payFeetoName(String toName, int fee, boolean sendNetMessage) {
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
		nc.sendMessage(nm);
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
		String currentPlayerName;
		System.out.println("GAME CLIENT UPDATE TURN TOKEN");
		System.out.println(">>UpdateTurnToken<< playerName received"
				+ playerName);

		if (currentPlayer != null) {
			System.out.println(">>UpdateTurnToken<< Current Player is "
					+ currentPlayer.getName());
			System.out
					.println(">>UpdateTurnToken<< Current Player turn token before change:"
							+ currentPlayer.hasTurnToken());
			currentPlayerName = currentPlayer.getName();
		} else
			currentPlayerName = null;

		board.updateTurnTokens(playerName, currentPlayerName);

		System.out
				.println(">>UpdateTurnToken<< NEW PLAYER turn token after change:"
						+ board.getPlayerByName(playerName).hasTurnToken());
		setCurrentPlayer(playerName, false);
		System.out.println(">>UpdateTurnToken<< The current player is now "
				+ currentPlayer.getName());
		System.out
				.println(">>UpdateTurnToken<< The current player's turn token is  "
						+ currentPlayer.hasTurnToken());
	}

	/**
	 * send an array of integers which is the new order that cards should be
	 * drawn for chance card events
	 * 
	 * @param the
	 *            array of int values to be send to the server
	 * @param sendNetMessage
	 *            true if a net message should be sent to the server
	 */
	public void updateChanceDrawOrder(int[] newOrder, boolean sendNetMessage) {
		NetMessage nm = new NetMessage(currentPlayer.getName(), 0,
				Messages.UPDATE_CHANCE_ORDER);
		nm.setDrawOrder(newOrder);
		nc.sendMessage(nm);

	}

	/**
	 * send an array of integers which is the new order that cards should be
	 * drawn for chance card events
	 * 
	 * @param the
	 *            array of int values to be send to the server
	 * @param sendNetMessage
	 *            true if a net message should be sent to the server
	 */
	public void updateCommChestDrawOrder(int[] newOrder, boolean sendNetMessage) {

		NetMessage nm = new NetMessage("NoNameNeeded", 0,
				Messages.UPDATE_COMMCHEST_ORDER);
		nm.setDrawOrder(newOrder);
		nc.sendMessage(nm);

	}

	public void displayChat(String text) {
		WindowStateEvent wse = new WindowStateEvent(WindowMessage.MSG_FOR_CHAT,
				text, 0);
		ws.notifyListeners(wse);
	}

	public void displayTransactionError(String text, boolean sendNetMessage) {
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
	public void sendTransactionErrorToGUI(TransactionException e,
			boolean sendNetMessage) {
		WindowStateEvent wse = new WindowStateEvent(
				WindowMessage.MSG_FOR_ERROR, e.getErrorMsg(), 0);
		ws.notifyListeners(wse);
	}

	/**
	 * gathers transactions errors from the methods and forwards them to the GUI
	 */
	public void sendTransactionSuccesToGUI(TransactionException e,
			boolean sendNetMessage) {
		WindowStateEvent wse = new WindowStateEvent(
				WindowMessage.MSG_EVENT_COMPLETION, e.getErrorMsg(), 0);
		ws.notifyListeners(wse);
	}

	/**
	 * gathers transactions errors from the methods and forwards them to the GUI
	 */
	public void sendNetMessageToGUI(NetMessage netMsg) {
		nc.sendMessage(netMsg);
	}

	/**
	 * gives the given player a jail card
	 * 
	 * @param String
	 *            the name of the player to change
	 */
	public void addJailCardToPlayer(String playerName, boolean sendNetMessage) {
		String playerNameAdjusted = adjustNameIfCurrentPlayer(playerName);
		board.addJailCardToPlayer(playerNameAdjusted);
	}

	/**
	 * removes a jail card from the given player
	 * 
	 * @param String
	 *            the name of the player to change
	 */
	public void removeJailCardFromPlayer(String playerName,
			boolean sendNetMessage) {
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

	/**
	 * Send a message of type QUIT_GAME to the other players
	 */
	public void sendQuitGame() {
		NetMessage nm = new NetMessage(this.localPlayer, Messages.QUIT_GAME);
		nc.sendMessage(nm);
	}

}
