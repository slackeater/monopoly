package ch.bfh.monopoly.common;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import ch.bfh.monopoly.exception.TransactionException;
import ch.bfh.monopoly.gui.MonopolyGUI;
import ch.bfh.monopoly.net.Messages;
import ch.bfh.monopoly.net.NetMessage;
import ch.bfh.monopoly.observer.TradeInfoEvent;
import ch.bfh.monopoly.observer.WindowListener;
import ch.bfh.monopoly.observer.WindowMessage;
import ch.bfh.monopoly.observer.WindowStateEvent;
import ch.bfh.monopoly.observer.WindowSubject;
import ch.bfh.monopoly.tile.IProperty;
import ch.bfh.monopoly.tile.Property;
import ch.bfh.monopoly.tile.Terrain;
import ch.bfh.monopoly.tile.Tile;

public class GameClient {

	private Player currentPlayer;
	private String localPlayer;
	private Player bank;
	private Locale loc;
	private Board board;
	private ClientNetworkController nc;
	private WindowSubject ws;
	private boolean testOff;
	private Dice dice;
	// used to test / for the roll value to a certain order of values
	private int rollCount = 0;
	private int kickVotes;
	TradeInfoEvent tradeEvent;
	boolean tradePending;

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

	public GameClient(boolean testOff) {
		this.testOff = testOff;
		ws = new ConcreteSubject();
		bank = new Player("bank", 100000000, null);

	}

	public GameClient() {
		this(true);

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
		this.board = new Board(this, testOff);
		board.createPlayers(names, loc);
		this.localPlayer = localPlayerName;
		dice = new Dice(6, 6, this, testOff);
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

	public Dice getDice() {
		return this.dice;
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

	/**
	 * a given player buys the property on which the current player is located.
	 * It must not be the current player who buys it, but the property is
	 * decided by his location
	 * 
	 * @param playerName
	 *            the name of the player who wants to buy the property
	 * @param sendNetMessage
	 *            true if you wish that a netMessage be sent informing the
	 *            clients of this action
	 */
	public void buyCurrentPropertyForPlayer(String playerName,
			boolean sendNetMessage) {
		String playerNameAdjusted = adjustNameIfCurrentPlayer(playerName);

		try {
			board.buyCurrentPropertyForPlayer(playerNameAdjusted,
					currentPlayer.getPosition());
			if (sendNetMessage) {
				// send a netmessage with the roll value of this player
				NetMessage netMsg = new NetMessage(playerNameAdjusted,
						Messages.BUY_PROPERTY);
				nc.sendMessage(netMsg);
			}
		} catch (TransactionException e) {
			sendTransactionErrorToGUI(e, true);
		}
	}

	/**
	 * advance the current player a given number n spaces forward in a given
	 * direction
	 * 
	 * @param the
	 *            direction to move the player
	 * @param n
	 *            is the number of spaces to advance the player
	 * @param sendNetMessage
	 *            true if a net message should be sent to the server
	 */
	public void advancePlayerNSpacesInDirection(int n,
			MonopolyGUI.Direction dir, boolean sendNetMessage) {
		// used to force the roll values to test certain tiles
		// int[] desiredRolls = { 10, 9, 11, 10, 9, 9 };
		int modifiedN = n;
		// int modifiedN = desiredRolls[rollCount];
		// rollCount++;

		String playerName = currentPlayer.getName();
		board.advancePlayerNSpacesInDirection(playerName, modifiedN, dir);

		if (sendNetMessage) {
			// send a netmessage with the roll value of this player
			NetMessage netMsg = new NetMessage(currentPlayer.getName(), n, dir,
					Messages.DICE_ROLL);
			nc.sendMessage(netMsg);
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
		advancePlayerNSpacesInDirection(n, MonopolyGUI.Direction.FORWARDS,
				sendNetMessage);
	}

	/**
	 * advance current player to tile n
	 */
	public void advancePlayerToTile(int tileId, boolean sendNetMessage) {
		boolean printVars = false;
		int currentPosition = currentPlayer.getPosition();
		int rollEquivalent = tileId - currentPosition;
		if (printVars) {
			System.out.println("currentpos" + currentPosition);
			System.out.println("tileID" + tileId);
			System.out.println("afterSubtraction" + rollEquivalent);
		}
		if (rollEquivalent < 0)
			rollEquivalent += 40;
		if (printVars)
			System.out.println("rollEquivalent" + rollEquivalent);
		advancePlayerNSpaces(rollEquivalent, sendNetMessage);
	}

	/**
	 * buy a house for a given property
	 * 
	 * @param tileID
	 *            the tile number of the property to build a house on
	 * @param sendNetMessage
	 *            true if a net message should be sent to the server
	 */
	public void buyHouse(int tileId, boolean sendNetMessage) {
		try {
			board.buyHouse(currentPlayer.getName(), tileId);
			if (sendNetMessage) {
				NetMessage netMsg = new NetMessage(currentPlayer.getName(),
						tileId, Messages.BUY_HOUSE);
				nc.sendMessage(netMsg);
			}
		} catch (TransactionException e) {
			sendTransactionErrorToGUI(e, sendNetMessage);
		}
	}

	/**
	 * buy 1 house for each property belonging to a group
	 * 
	 * @param tileId
	 *            the id of any tile in the group to build on
	 */
	public void buyHouseRow(int tileId, boolean sendNetMessage) {
		try {
			board.buyHouseRow(currentPlayer.getName(), tileId);
			if (sendNetMessage) {
				NetMessage netMsg = new NetMessage(currentPlayer.getName(),
						tileId, Messages.BUY_HOUSEROW);
				nc.sendMessage(netMsg);
			}
		} catch (TransactionException e) {
			sendTransactionErrorToGUI(e, sendNetMessage);
		}
	}

	/**
	 * buy 1 hotel for each property belonging to a group
	 * 
	 * @param tileId
	 *            the id of any tile in the group to build on
	 */
	public void buyHotelRow(int tileId, boolean sendNetMessage) {
		try {
			board.buyHotelRow(currentPlayer.getName(), tileId);
			if (sendNetMessage) {
				NetMessage netMsg = new NetMessage(currentPlayer.getName(),
						tileId, Messages.BUY_HOTELROW);
				nc.sendMessage(netMsg);
			}
		} catch (TransactionException e) {
			sendTransactionErrorToGUI(e, sendNetMessage);
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
			board.buyHotel(currentPlayer.getName(), tileID);
			if (sendNetMessage) {
				NetMessage netMsg = new NetMessage(currentPlayer.getName(),
						tileID, Messages.BUY_HOTEL);
				nc.sendMessage(netMsg);
			}
		} catch (TransactionException e) {
			sendTransactionErrorToGUI(e, sendNetMessage);
		}
	}

	/**
	 * sell a house for a given property
	 * 
	 * @param tileId
	 *            the tile number of the property to sell a house from
	 * @param sendNetMessage
	 *            true if a net message should be sent to the server
	 */
	public void sellHouse(int tileId, boolean sendNetMessage) {
		try {
			board.sellHouses(tileId);
			if (sendNetMessage) {
				NetMessage netMsg = new NetMessage(currentPlayer.getName(),
						tileId, Messages.SELL_HOUSE);
				nc.sendMessage(netMsg);
			}
		} catch (TransactionException e) {
			sendTransactionErrorToGUI(e, sendNetMessage);
		}
	}

	/**
	 * sell 1 house for each property belonging to a group
	 * 
	 * @param tileId
	 *            the id of any tile in the group to sell from
	 * @throws TransactionException
	 */
	public void sellHouseRow(int tileId, boolean sendNetMessage) {
		try {
			board.sellHouseRow(currentPlayer.getName(), tileId);
			if (sendNetMessage) {
				NetMessage netMsg = new NetMessage(currentPlayer.getName(),
						tileId, Messages.SELL_HOUSEROW);
				nc.sendMessage(netMsg);
			}
		} catch (TransactionException e) {
			sendTransactionErrorToGUI(e, sendNetMessage);
		}
	}

	/**
	 * sell a hotel for a given property
	 * 
	 * @param tileId
	 *            the tile number of the property to sell a hotel from
	 * @param sendNetMessage
	 *            true if a net message should be sent to the server
	 */
	public void sellHotel(int tileId, boolean sendNetMessage) {
		try {
			board.sellHotel(currentPlayer.getName(), tileId);
			if (sendNetMessage) {
				NetMessage netMsg = new NetMessage(currentPlayer.getName(),
						tileId, Messages.SELL_HOTEL);
				nc.sendMessage(netMsg);
			}
		} catch (TransactionException e) {
			sendTransactionErrorToGUI(e, sendNetMessage);
		}
	}

	/**
	 * sell 1 hotel for each property belonging to a group
	 * 
	 * @param tileId
	 *            the id of any tile in the group to sell from
	 * @throws TransactionException
	 */
	public void sellHotelRow(int tileId, boolean sendNetMessage) {
		try {
			board.sellHotelRow(currentPlayer.getName(), tileId);
			if (sendNetMessage) {
				NetMessage netMsg = new NetMessage(currentPlayer.getName(),
						tileId, Messages.SELL_HOTELROW);
				nc.sendMessage(netMsg);
			}
		} catch (TransactionException e) {
			sendTransactionErrorToGUI(e, sendNetMessage);
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
			board.toggleMortgageStatus(currentPlayer.getName(), tileId);
			if (sendNetMessage) {
				NetMessage netMsg = new NetMessage(currentPlayer.getName(),
						tileId, Messages.TOGGLE_MORTGAGE);
				nc.sendMessage(netMsg);
			}
		} catch (TransactionException e) {
			sendTransactionErrorToGUI(e, sendNetMessage);
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

		System.out.println("gameClient: transferProperty Id: " + tileId
				+ "from " + fromName + "to" + toName);

		String fromNameAdjusted = adjustNameIfCurrentPlayer(fromName);
		String toNameAdjusted = adjustNameIfCurrentPlayer(toName);
		try {
			board.transferProperty(fromNameAdjusted, toNameAdjusted, tileId,
					price);
			if (sendNetMessage) {
				System.out.println("gameClient: TransferProp created net message: from" + fromNameAdjusted + " to " + toNameAdjusted); 
				NetMessage netMsg = new NetMessage(fromNameAdjusted, toNameAdjusted, tileId,
						Messages.TRANSFER_PROPERTY);
				nc.sendMessage(netMsg);
			}
		} catch (TransactionException e) {
			sendTransactionErrorToGUI(e, sendNetMessage);
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
		System.out.println("gameClient: transferJailCards " + quantity
				+ " from " + fromName + " to" + toName);
		String fromNameAdjusted = adjustNameIfCurrentPlayer(fromName);
		String toNameAdjusted = adjustNameIfCurrentPlayer(toName);
		try {
			board.transferJailCards(fromNameAdjusted, toNameAdjusted, quantity,
					price);
			if (sendNetMessage) {
				System.out.println("gameClient: TransferJail created net message: from" + fromNameAdjusted + " to " + toNameAdjusted );
				NetMessage netMsg = new NetMessage(fromNameAdjusted, toNameAdjusted,
						quantity, Messages.TRANSFER_JAILCARD);
				nc.sendMessage(netMsg);
			}
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
		System.out.println("gameClient: transferMoney "
				+ amount + " from " + fromName + " to"
				+ toName);
		String fromNameAdjusted = adjustNameIfCurrentPlayer(fromName);
		String toNameAdjusted = adjustNameIfCurrentPlayer(toName);

		try {
			board.transferMoney(fromNameAdjusted, toNameAdjusted, amount);
			if (sendNetMessage) {
				System.out.println("gameClient: TransferMoney created net message: from" + fromNameAdjusted + " to " + toNameAdjusted );
				NetMessage netMsg = new NetMessage(fromNameAdjusted, toNameAdjusted,
						amount, Messages.TRANSFER_MONEY);
				nc.sendMessage(netMsg);
			}
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
	 * Get the JPanel to start the turn
	 * 
	 * @return the JPanel that the GUI will display
	 */
	public JPanel getStartTurnPanel(boolean sendNetMessage) {
		JPanel jp;
		if (currentPlayer.isInJail())
			jp = dice.getNewJailStart();
		else
			jp = dice.getNewStartRoll();
		if (sendNetMessage) {
			NetMessage netMsg = new NetMessage(Messages.START_TURN_PANEL);
			sendNetMessageToGUI(netMsg);
		}
		return jp;
	}

	/**
	 * Get the JPanel for the tile's event. Should be called when a player rolls
	 * and lands on a new tile
	 * 
	 * @param the
	 *            id of the tile of which to get the JPanel
	 * @return the JPanel that the GUI will display
	 */
	public JPanel getTileEventPanel(boolean sendNetMessage) {
		int tileId = currentPlayer.getPosition();
		JPanel jpanel = board.getTileEventPanelForTile(tileId);
		if (sendNetMessage) {
			NetMessage netMsg = new NetMessage(Messages.GET_EVENT_WINDOW);
			sendNetMessageToGUI(netMsg);
		}
		return jpanel;
	}

	// TODO This method might never be used?
	// /**
	// * checks if a given player is the owner of a given tile
	// *
	// * @param playerName
	// * the player's name to check
	// * @param tileId
	// * tile to check ownership of
	// * @param sendNetMessage
	// * true if a net message should be sent to the server
	// */
	// public boolean playerIsOwnerOfTile(String playerName, int tileId) {
	// boolean isOwner = false;
	// isOwner = board.checkPlayerIsOwnerOfTile(playerName, tileId);
	// return isOwner;
	// }

	/**
	 * checks if the current player has sufficient funds to pay a fee
	 * 
	 * @param fee
	 *            the amount of the fee to be paid
	 * @throws TransactionException
	 * @throws RuntimeException
	 */
	public void hasSufficientFunds(int fee) throws RuntimeException,
	TransactionException {
		board.playerHasSufficientFunds(currentPlayer.getName(), fee);
	}

	/**
	 * checks if the current player has sufficient funds to pay a fee
	 * 
	 * @param playerName
	 *            the player to check the account of
	 * @param fee
	 *            the amount of the fee to be paid
	 * @throws TransactionException
	 */
	public void playerHasSufficientFunds(String playerName, int amount)
	throws TransactionException {
		String playerNameAdjusted = adjustNameIfCurrentPlayer(playerName);
		board.playerHasSufficientFunds(playerNameAdjusted, amount);
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
			if (sendNetMessage) {
				NetMessage msg = new NetMessage(currentPlayer.getName(), fee,
						Messages.PAY_FEE);
				sendNetMessageToGUI(msg);
			}
		} catch (TransactionException e) {
			sendTransactionErrorToGUI(e, sendNetMessage);
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
	public void payUtilityFee(int fee, boolean sendNetMessage) {
		String currentPlayerName = currentPlayer.getName();
		String owner = board
		.castTileToProperty(
				board.getTileById(currentPlayer.getPosition()))
				.getOwner().getName();
		transferMoney(currentPlayerName, owner, fee, false);
		if (sendNetMessage) {
			NetMessage msg = new NetMessage(currentPlayer.getName(), fee,
					Messages.PAY_UTILITY_FEE);
			sendNetMessageToGUI(msg);
		}
	}

	/**
	 * sends the currentPlayer to jail
	 */
	public void goToJail(boolean sendNetMessage) {
		int jail = 10;
		advancePlayerToTile(jail, false);
		board.setPlayerJailStatus(currentPlayer.getName(), true);
		if (sendNetMessage) {
			NetMessage msg = new NetMessage(currentPlayer.getName(),
					Messages.GO_TO_JAIL);
			sendNetMessageToGUI(msg);
		}
	}

	/**
	 * gets the currentPlayer out of jail
	 */
	public void getOutOfJailByPayment(boolean sendNetMessage) {
		try {
			board.getOutOfJailByPayment(currentPlayer.getName());
			if (sendNetMessage) {
				NetMessage msg = new NetMessage(currentPlayer.getName(),
						Messages.GET_OUT_OF_JAIL_PAY);
				sendNetMessageToGUI(msg);
			}
		} catch (TransactionException e) {
			sendTransactionErrorToGUI(e, sendNetMessage);
		}

	}

	/**
	 * gets the currentPlayer out of jail
	 */
	public void getOutOfJailByCard(boolean sendNetMessage) {
		board.getOutOfJailByCard(currentPlayer.getName());
		if (sendNetMessage) {
			NetMessage msg = new NetMessage(currentPlayer.getName(),
					Messages.GET_OUT_OF_JAIL_USECARD);
			sendNetMessageToGUI(msg);
		}
	}

	/**
	 * gets the currentPlayer out of jail by means of rolling
	 */
	public void getOutOfJailByRoll(boolean sendNetMessage) {
		board.getOutOfJailByRoll(currentPlayer.getName());
		if (sendNetMessage) {
			NetMessage msg = new NetMessage(currentPlayer.getName(),
					Messages.GET_OUT_OF_JAIL_ROLL);
			sendNetMessageToGUI(msg);
		}
	}

	/**
	 * called by BirthdayEvent class, transfer a given amount of money from all
	 * players but the current player to the current player's account
	 */
	public void birthdayEvent(int amount, boolean sendNetMessage) {
		try {
			board.birthdayEvent(currentPlayer.getName(), amount);
			if (sendNetMessage) {
				NetMessage msg = new NetMessage(currentPlayer.getName(),
						amount, Messages.BIRTHDAY_EVENT);
				sendNetMessageToGUI(msg);
			}
		} catch (TransactionException e) {
			sendTransactionErrorToGUI(e, sendNetMessage);
		}
	}

	/**
	 * player pays 10% of his cash into free parking
	 */
	public void payIncome10Percent(boolean sendNetMessage) {
		int fee = currentPlayer.getAccount() / 10;
		payFee(fee, sendNetMessage);
	}

	/**
	 * called by the change event getJailCard increases the jailCard count of
	 * the current player by 1
	 */
	public void winJailCard(boolean sendNetMessage) {
		board.addJailCardToPlayer(currentPlayer.getName());
		if (sendNetMessage) {
			NetMessage msg = new NetMessage(currentPlayer.getName(),
					Messages.WIN_JAIL_CARD);
			sendNetMessageToGUI(msg);
		}
	}

	/**
	 * the current player gets all the money in the free parking account
	 */
	public void freeParking(boolean sendNetMessage) {
		board.freeParking(currentPlayer.getName());
		if (sendNetMessage) {
			NetMessage msg = new NetMessage(currentPlayer.getName(),
					Messages.FREE_PARKING);
			sendNetMessageToGUI(msg);
		}
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
		// System.out.println("GAME CLIENT UPDATE TURN TOKEN");
		// System.out.println(">>UpdateTurnToken<< playerName received"
		// + playerName);

		if (currentPlayer != null) {
			// System.out.println(">>UpdateTurnToken<< Current Player is "
			// + currentPlayer.getName());
			// System.out
			// .println(">>UpdateTurnToken<< Current Player turn token before change:"
			// + currentPlayer.hasTurnToken());

			currentPlayerName = currentPlayer.getName();
		} else
			currentPlayerName = null;

		board.updateTurnTokens(playerName, currentPlayerName);
		// USED FOR DEBUGGING THE GUI
		// System.out
		// .println(">>UpdateTurnToken<< NEW PLAYER turn token after change:"
		// + board.getPlayerByName(playerName).hasTurnToken());
		setCurrentPlayer(playerName, false);
		// System.out.println(">>UpdateTurnToken<< The current player is now "
		// + currentPlayer.getName());
		// System.out
		// .println(">>UpdateTurnToken<< The current player's turn token is  "
		// + currentPlayer.hasTurnToken());
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
		board.castTileToProperty(board.getTileById(1)).getEventManager()
		.setChanceOrder(newOrder);
		if (sendNetMessage) {
			NetMessage nm = new NetMessage(currentPlayer.getName(), 0,
					Messages.UPDATE_CHANCE_ORDER);
			nm.setDrawOrder(newOrder);
			nc.sendMessage(nm);
		}

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
		board.castTileToProperty(board.getTileById(1)).getEventManager()
		.setCommChestOrder(newOrder);
		if (sendNetMessage) {
			NetMessage nm = new NetMessage("NoNameNeeded", 0,
					Messages.UPDATE_COMMCHEST_ORDER);
			nm.setDrawOrder(newOrder);
			nc.sendMessage(nm);
		}
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
		if (sendNetMessage) {
			WindowStateEvent wse = new WindowStateEvent(
					WindowMessage.MSG_FOR_ERROR, e.getErrorMsg(), 0);
			ws.notifyListeners(wse);
		} else {
			System.out.println(e.getErrorMsg());
		}
	}

	/**
	 * gathers transactions errors from the methods and forwards them to the GUI
	 */
	public void sendTransactionSuccesToGUI(boolean sendNetMessage) {
		// TODO this doesn't seem like the best way to signal success to the GUI
		TransactionException te = new TransactionException(
		"The event was completed successfully");
		WindowStateEvent wse = new WindowStateEvent(
				WindowMessage.MSG_EVENT_COMPLETION, te.getErrorMsg(), 0);
		ws.notifyListeners(wse);
	}

	/**
	 * gathers transactions errors from the methods and forwards them to the GUI
	 */
	public void sendNetMessageToGUI(NetMessage netMsg) {
		nc.sendMessage(netMsg);
	}

	/**
	 * send a trade request to a player
	 */
	public void sendTradeRequestToPlayer(String playerName, TradeInfoEvent tie) {
		tradePending = true;
		tradeEvent = tie;
		System.out.println("SEND TRADE FROM GAME CLIENT");
		NetMessage nm = new NetMessage(playerName, tie, Messages.TRADE_REQUEST);
		nc.sendMessage(nm);

	}

	/**
	 * receive a trade request
	 */
	public void receiveTradeRequest(TradeInfoEvent tie) {
		System.out.println("gameClient: trade request arrived");
		WindowStateEvent wse = new WindowStateEvent(
				WindowMessage.MSG_TRADE_REQUEST, tie);
		ws.notifyListeners(wse);
	}

	/**
	 * confirm/reject a trade request that you have received
	 */
	public void sendTradeAnswer(boolean answer) {
		System.out.println("gameClient send TradeAnswer of " + answer);
		NetMessage nm = new NetMessage(answer, Messages.TRADE_ANSWER);
		nc.sendMessage(nm);
	}

	/**
	 * receive the response from a trade request
	 */
	public void receiveTradeAnswer(boolean answer) {
		if (tradePending) {
			if (answer) {
				System.out
				.println("gameClient received TRADE ANSWER:" + answer);
				performTrade();
				System.out.println("gameClient: performTrade() completed");

			}
			System.out.println("sending WindowStateEvent to GUI");

			WindowStateEvent wse = new WindowStateEvent(
					WindowMessage.MSG_TRADE_ANSWER, answer);
			System.out.println("trying to send message to GUI: type:" + wse.getType() + wse.getAnswer());
			ws.notifyListeners(wse);	
			tradePending = false;
		}
	}

	/**
	 * perform the trade in the tradeEvent
	 */
	public void performTrade() {
		boolean sendNetMessage = false;
		System.out.println("performing trade");
		String sourcePlayer = tradeEvent.getSourcePlayer();
		String otherPlayer = tradeEvent.getOtherPlayer();
		if (tradeEvent.getMoneyOffer() > 0) {
			System.out.println("gameClient.performTrade() moneyOffer:"
					+ tradeEvent.getMoneyOffer());
			transferMoney(sourcePlayer, otherPlayer,
					tradeEvent.getMoneyOffer(), sendNetMessage);
		}
		if (tradeEvent.getMoneyDemand() > 0) {
			System.out.println("gameClient.performTrade() moneyDemand:"
					+ tradeEvent.getMoneyDemand());
			transferMoney(otherPlayer, sourcePlayer,
					tradeEvent.getMoneyDemand(), sendNetMessage);
		}
		if (tradeEvent.getPropertiesOffer() != null) {
			System.out.println("gameClient.performTrade() PropertiesOffer:"
					+ tradeEvent.getPropertiesOffer());
			for (String prop : tradeEvent.getPropertiesOffer()) {
				int tileId = board.getTileIdByName(prop);
				transferProperty(sourcePlayer, otherPlayer, tileId, 0, sendNetMessage);
			}
		}
		if (tradeEvent.getPropertiesDemand() != null) {
			System.out.println("gameClient.performTrade() PropertiesDemand:"
					+ tradeEvent.getPropertiesDemand());
			for (String prop : tradeEvent.getPropertiesDemand()) {
				int tileId = board.getTileIdByName(prop);
				transferProperty(otherPlayer, sourcePlayer, tileId, 0, sendNetMessage);
			}
		}
		if (tradeEvent.getJailcardOffer() > 0) {
			System.out.println("gameClient.performTrade() JailcardOffer:"
					+ tradeEvent.getJailcardOffer());
			transferJailCards(sourcePlayer, otherPlayer,
					tradeEvent.getJailcardOffer(), 0, sendNetMessage);
		}
		if (tradeEvent.getJailcardDemand() > 0) {
			System.out.println("gameClient.performTrade() JailcardDemand:"
					+ tradeEvent.getJailcardDemand());
			transferJailCards(otherPlayer, sourcePlayer,
					tradeEvent.getJailcardDemand(), 0, sendNetMessage);
		}
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
	 * return the amount of money that is in free parking used to display the
	 * amount to the player when the event occurs
	 */
	public int getFreeParkingAccount() {
		return board.getFreeParkingAccount();
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
	 * Close the connection with the server
	 */
	public void sendQuitGame() {
		nc.closeConnection();
	}

	/**
	 * create a motion to kick a player
	 * 
	 * @param the
	 *            name of the player who might be kicked out of the game
	 */
	public void createKickRequest(String playerName) {
		kickVotes = 0;
		NetMessage nm = new NetMessage(localPlayer, playerName,
				Messages.KICK_REQUEST);
		nc.sendMessage(nm);
	}

	/**
	 * receive a kick request
	 * 
	 * @param the
	 *            name of the player who might be kicked out of the game
	 */
	public void receiveKickRequest(String playerName, String playerToKick) {
		WindowStateEvent wse = new WindowStateEvent(
				WindowMessage.MSG_KICK_REQUEST, playerName, playerToKick);
		ws.notifyListeners(wse);
	}

	/**
	 * receive an answer to a kick request
	 * 
	 * @param the
	 *            name of the player who might be kicked out of the game
	 */
	public void receiveKickAnswer(String playerName, boolean answer) {
		if (answer)
			kickVotes++;
		if (kickVotes > board.getPlayers().size() / 2) {
			// sendKickSignal();
		}
		WindowStateEvent wse = new WindowStateEvent(
				WindowMessage.MSG_KICK_ANSWER, playerName, answer);
		ws.notifyListeners(wse);
	}

}
