package ch.bfh.monopoly.common;

import java.awt.Color;
import java.net.Socket;
import java.util.ArrayList;

import ch.bfh.monopoly.observer.PlayerListener;
import ch.bfh.monopoly.observer.PlayerStateEvent;
import ch.bfh.monopoly.observer.PlayerSubject;
import ch.bfh.monopoly.tile.Property;
import ch.bfh.monopoly.tile.Railroad;
import ch.bfh.monopoly.tile.Terrain;
import ch.bfh.monopoly.tile.Tile;

public class Player {

	private String name;
	private int account;
	private ArrayList<Tile> properties;
	private int position;
	private boolean isInJail;
	private boolean turnToken;
	private int jailCard;
	private Token t;
	
	
	public Player(String name, int account) {
		this.name = name;
		this.account = account;
		position = 0;
		isInJail = false;
		turnToken = false;
		jailCard = 0;
		properties = new ArrayList<Tile>();
	}

	/**
	 * find out how many railroad tiles this player owns. This is used by the
	 * simpleFeeEvent to calculate the rent that another player must pay if he
	 * lands on a railroad owned by this player
	 * 
	 * @return the int value representing the number of railroad that this
	 *         player owns.
	 */
	public int numberRailRoadsOwned() {
		int rrOwned = 0;
		for (Tile t : properties) {
			if (t instanceof Railroad)
				rrOwned++;
		}
		return rrOwned;
	}

	/**
	 * Add a property to the list of properties that the player owns
	 * 
	 * @param the
	 *            tile object to add to the list
	 */
	public void addProperty(Tile t) {
		properties.add(t);
		
	}

	/**
	 * remove a property from the list of properties that the player owns
	 * 
	 * @param the
	 *            tile object to remove from the list
	 */
	public void removeProperty(Tile t) {
		properties.remove(t);
	
	}

	/**
	 * check if a given property, t, belongs the this player
	 * 
	 * @param the
	 *            tile object for which ownership will be checked
	 * @return true if this player owns the given tile t
	 */
	public boolean ownsProperty(Tile t) {
		return properties.contains(t);
	}

	/**
	 * deposit money into a player's account
	 * 
	 * @param value
	 *            is amount of money to add to this player's account
	 */
	public void depositMoney(int value) {
		this.account += value;
		
	}

	/**
	 * withdraw money from this player's account
	 * 
	 * @param value
	 *            is the amount of money that should be withdrawn
	 */
	public void withdawMoney(int value) {
		if (account < value)
			throw new RuntimeException(
					"The sum cannot be withdrawn from the player's account, because the player has insufficient funds");
		this.account -= value;
		
	}

	/**
	 * get the name of this player
	 * 
	 * @return the name of the player as a string
	 */
	public String getName() {
		return name;
	}

	/**
	 * get the balance of the account of this player
	 * 
	 * @return the int value balance of this player's account
	 */
	public int getAccount() {
		return account;
	}

	/**
	 * set the balance of this player's account
	 * 
	 * @param account
	 *            the new balance that the account should be
	 */
	public void setAccount(int account) {
		this.account = account;
	}

	/**
	 * get the list of properties that belong to this player
	 * 
	 * @return the list of properties that belong to this player
	 */
	public ArrayList<Tile> getProperties() {
		return properties;
	}

	/**
	 * get the current position on the baord for this player
	 * 
	 * @return the int value which represents the tile the player is on. 0 is
	 *         Go, 39 is Boardwalk (english edition)
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * set the current position on the board for this player
	 * 
	 * @param position
	 *            is the new position that the player should have
	 */
	public void setPosition(int position) {
		this.position = position;
	}

	/**
	 * check if this player is in jail
	 * 
	 * @return true if the player is in jail
	 */
	public boolean isInJail() {
		return isInJail;
	}

	/**
	 * change the state of the player between inJail and not inJail
	 * 
	 * @param isInJail
	 *            boolean value to set the player's jail state to
	 */
	public void setInJail(boolean isInJail) {
		this.isInJail = isInJail;
	}

	/**
	 * check if this player is the player who is allowed to perform actions in
	 * the game. i.e. is it his turn?
	 * 
	 * @return the boolean value true if it is this player's turn
	 */
	public boolean isTurnToken() {
		return turnToken;
	}

	/**
	 * sets the turnToken value to the provided boolean value
	 * 
	 * @param turnToken
	 *            true if it is this player's turn, false if another player is
	 *            allowed to make actions
	 */
	public void setTurnToken(boolean turnToken) {
		this.turnToken = turnToken;
	}

	/**
	 * check how many jail cards this player has (if any)
	 * 
	 * @return the int value representing the amount of 'get out of jail free
	 *         cards' that the player has
	 */
	public int getJailCard() {
		return jailCard;
	}

	/**
	 * changes the amount of jail cards that the player has
	 * 
	 * @param jailCard
	 *            is the int value to set the jailCard variable to
	 */
	public void setJailCard(int jailCard) {
		this.jailCard = jailCard;
	}

	/**
	 * sets the token object for this player. The token object contains
	 * information which represents the player graphically on the board
	 * 
	 * @param t
	 *            the token object to set for this player's token variable
	 */
	public void setToken(Token t) {
		this.t = t;
	}

	/**
	 * gets the token object for this player. The token object contains
	 * information which represents the player graphically on the board
	 * 
	 * @return the token object that this player has
	 */
	public Token getToken() {
		return this.t;
	}



}
