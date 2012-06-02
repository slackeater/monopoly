package ch.bfh.monopoly.net;

import java.awt.Color;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import com.sun.tools.internal.ws.processor.model.Message;

import ch.bfh.monopoly.common.Player;
import ch.bfh.monopoly.gui.MonopolyGUI;
import ch.bfh.monopoly.observer.TradeInfoEvent;

public class NetMessage implements Serializable {

	private static final long serialVersionUID = -6004188281012651357L;

	private Player player;
	private String fromName;
	private String toName;
	private Color c;
	private List<String> playerNames;
	private Locale loc;
	private String string;
	private int integerValue;
	private int price;
	private Player playerToKick;
	private boolean kickAnswer;
	private Messages m;
	private int propertyID;
	private int[] cardDrawOrder;
	private MonopolyGUI.Direction dir;
	private TradeInfoEvent tie;
	


	public NetMessage(Messages m) {
		this.m = m;
	}

	/**
	 * Construct a NetMessage to transfer MONEY between two players
	 * 
	 * @param player
	 *            to transfer from
	 * @param toName
	 *            player to transfer to
	 * @param integerValue
	 *            the tileID/quantity of Jail cards/amount of money to transfer
	 * @param m
	 *            the message type
	 */
	public NetMessage(String fromName, String toName, int integerValue,
			Messages m) {
		this.toName = toName;
		this.fromName = fromName;
		this.integerValue = integerValue;
		this.m = m;
	}

	/**
	 * Construct a NetMessage
	 * 
	 * @param player
	 *            name of player
	 * @param integerValue
	 * @param m
	 *            the message type
	 */
	public NetMessage(String string, int integerValue, Messages m) {
		this.string = string;
		this.integerValue = integerValue;
		this.m = m;
	}
	
	/**
	 * construct a netmessage that send the trade info 
	 * @param string
	 * @param tie
	 * @param m
	 */
	public NetMessage(String string, TradeInfoEvent tie, Messages m){
		this.string = string;
		this.tie = tie;
		this.m = m;
	}

	/**
	 * Construct a NetMessage
	 * 
	 * @param player
	 *            name of player
	 * @param integerValue
	 * @param m
	 *            the message type
	 */
	public NetMessage(String string, int integerValue,
			MonopolyGUI.Direction dir, Messages m) {
		this.string = string;
		this.integerValue = integerValue;
		this.m = m;
		this.dir = dir;
	}

	/**
	 * Construct a NetMessage to transfer Properties or Jailcards between two
	 * players
	 * 
	 * @param player
	 *            to transfer from
	 * @param toName
	 *            player to transfer to
	 * @param integerValue
	 *            the amount of money to transfer
	 * @param m
	 *            the message type
	 */
	public NetMessage(String fromName, String toName, int integerValue,
			int price, Messages m) {
		this.toName = toName;
		this.fromName = fromName;
		this.integerValue = integerValue;
		this.price = price;
		this.m = m;
	}

	/**
	 * Construct a NetMessage
	 * 
	 * @param property
	 *            the property to auction
	 * @param basePrice
	 *            the base price of the property
	 * @param m
	 *            the message type
	 */
	public NetMessage(int propertyID, int basePrice, Messages m) {
		this.propertyID = propertyID;
		this.integerValue = basePrice;
		this.m = m;
	}

	/**
	 * Construct a NetMessage
	 * 
	 * @param player
	 *            the player who paused / quit / join
	 * @param m
	 *            the message type
	 */
	public NetMessage(Player player, Messages m) {
		this.player = player;
		this.m = m;
	}

	/**
	 * Construct a NetMessage
	 * 
	 * @param kickAnswer
	 *            the answer of a kick request
	 * @param m
	 *            the message type
	 */
	public NetMessage(boolean kickAnswer, Messages m) {
		this.kickAnswer = kickAnswer;
		this.m = m;
	}

	/**
	 * Construct a NetMessage
	 * 
	 * @param loc
	 *            the chosen locale
	 * @param m
	 *            th message type
	 */
	public NetMessage(Locale loc, Messages m) {
		this.loc = loc;
		this.m = m;
	}

	/**
	 * Construct a NetMessage
	 * 
	 * @param player
	 *            a player who wants to sell / buy a row of building / send a
	 *            chat message
	 * @param s
	 *            the name of the group / the chat message
	 * @param m
	 *            the message type
	 */
	public NetMessage(Player player, String s, Messages m) {
		this.player = player;
		this.string = s;
		this.m = m;
	}

	/**
	 * Construct a NetMessage
	 * 
	 * @param player
	 *            the player who wants to sell a property
	 * @param property
	 *            the property to sell
	 * @param basePrice
	 *            the base price for the auction
	 * @param m
	 *            the message type
	 */
	public NetMessage(Player player, int propertyID, int basePrice, Messages m) {
		this.player = player;
		this.propertyID = propertyID;
		this.integerValue = basePrice;
		this.m = m;
	}

	/**
	 * Construct a NetMessage
	 * 
	 * @param player
	 *            the player who proposed the kick
	 * @param playerToKick
	 *            the player to kick
	 * @param m
	 *            the message type
	 */
	public NetMessage(Player player, Player playerToKick, Messages m) {
		this.player = player;
		this.playerToKick = playerToKick;
		this.m = m;
	}

	/**
	 * Construct a NetMessage
	 * 
	 * @param s
	 *            the message of the chat
	 * @param m
	 *            the message type
	 */
	public NetMessage(String s, Messages m) {
		this.string = s;
		this.m = m;
	}

	/**
	 * Construct a NetMessage
	 * 
	 * @param l
	 *            the list of the players
	 * @param m
	 *            the message type
	 */
	public NetMessage(List<String> l, Locale loc, Messages m) {
		this.playerNames = l;
		this.m = m;
		this.loc = loc;
	}

	// only for test
	public int getMessageCode() {
		return m.getInt();
	}

	public Messages getMessageType() {
		return m;
	}

	public String getText() {
		// TODO Auto-generated method stub
		return string;
	}

	/**
	 * Get the list of the user
	 * 
	 * @return List<String> a list containing the user names
	 */
	public List<String> getUserNameList() {
		return this.playerNames;
	}

	/**
	 * Get the integer value of this message
	 * 
	 * @return an int the integer value of this NetMessage
	 */
	public int getInt() {
		return this.integerValue;
	}

	/**
	 * Get the locale included in this message
	 * 
	 * @return Locale the locale
	 */
	public Locale getLocale() {
		return this.loc;
	}

	/**
	 * get the int array which is the draw order for either chance cards or
	 * community chess cards
	 */
	public int[] getDrawOrder() {
		return cardDrawOrder;
	}

	/**
	 * set the int array which is the draw order for either chance cards or
	 * community chess cards
	 */
	public void setDrawOrder(int[] newOrder) {
		cardDrawOrder = newOrder;
	}

	public String getFromName() {
		return fromName;
	}


	public MonopolyGUI.Direction getDir() {
		return dir;
	}
	
	public String getToName() {
		return toName;
	}

	public int getPrice() {
		return price;
	}

}
