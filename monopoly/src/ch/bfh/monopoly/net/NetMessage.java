package ch.bfh.monopoly.net;

import java.awt.Color;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;


import ch.bfh.monopoly.common.Player;
import ch.bfh.monopoly.gui.MonopolyGUI;
import ch.bfh.monopoly.observer.TradeInfoEvent;

public class NetMessage implements Serializable {

	private static final long serialVersionUID = -6004188281012651357L;

	private String string1;
	private String string2;
	private Color c;
	private List<String> playerNames;
	private Locale loc;
	private int integerValue;
	private int integerValue2;
	private boolean bool;
	private Messages m;
	private int[] cardDrawOrder;
	private MonopolyGUI.Direction dir;
	private TradeInfoEvent tie;

	public NetMessage(Messages m) {
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
	public NetMessage(String string1, Messages m) {
		this.string1 = string1;
		this.m = m;
	}
	
	public NetMessage(String string1, int integerValue, Messages m) {
		this.string1 = string1;
		this.integerValue = integerValue;
		this.m = m;
	}
	
	public NetMessage(String string1, String string2, int integerValue,
			Messages m) {
		this.string1 = string1;
		this.string2 = string2;
		this.integerValue = integerValue;
		this.m = m;
	}
	
	/**
	 * construct a netmessage that send the trade info 
	 * @param string
	 * @param tie
	 * @param m
	 */
	public NetMessage(String string1, TradeInfoEvent tie, Messages m){
		this.string1 = string1;
		this.tie = tie;
		this.m = m;
	}


	public NetMessage(String string1, int integerValue,
			MonopolyGUI.Direction dir, Messages m) {
		this.string1 = string1;
		this.integerValue = integerValue;
		this.m = m;
		this.dir = dir;
	}


	public NetMessage(String string1, String string2, int integerValue,
			int price, Messages m) {
		this.string1 = string1;
		this.string2 = string2;
		this.integerValue = integerValue;
		this.integerValue = price;
		this.m = m;
	}

//	/**
//	 * Construct a NetMessage
//	 * 
//	 * @param property
//	 *            the property to auction
//	 * @param basePrice
//	 *            the base price of the property
//	 * @param m
//	 *            the message type
//	 */
//	public NetMessage(int propertyID, int basePrice, Messages m) {
//		this.propertyID = propertyID;
//		this.integerValue = basePrice;
//		this.m = m;
//	}


	/**
	 * Construct a NetMessage
	 * 
	 * @param kickAnswer
	 *            the answer of a kick request
	 * @param m
	 *            the message type
	 */
	public NetMessage(String string1, boolean kickAnswer, Messages m) {
		this.string1=string1;
		this.bool = kickAnswer;
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
	 *            the name of the player who proposed the kick
	 * @param playerToKick
	 *            the name of the player to kick
	 * @param m
	 *            the message type
	 */
	public NetMessage(String string1, String string2, Messages m) {
		this.string1 = string1;
		this.string2 = string2;
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

	public String getString1() {
		// TODO Auto-generated method stub
		return string1;
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
		return string1;
	}


	public MonopolyGUI.Direction getDir() {
		return dir;
	}
	
	public String getToName() {
		return string2;
	}

	public int getPrice() {
		return integerValue;
	}
	

	public boolean getKickAnswer() {
		return bool;
	}
	
	public boolean getTradeAnswer() {
		return bool;
	}
	

	public TradeInfoEvent getTie() {
		return tie;
	}
	
	
	public String getString2(){
		return string2;
	}


}
