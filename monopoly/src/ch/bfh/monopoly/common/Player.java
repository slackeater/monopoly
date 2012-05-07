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
	private PlayerSubject playerSubject;
	
	
	
	private class ConcreteSubject implements PlayerSubject {

		public ConcreteSubject() {}

		ArrayList<PlayerListener> listeners = new ArrayList<PlayerListener>();

		public void addListener(PlayerListener tl) {
			listeners.add(tl);
		}
		@Override
		public void removeListener(PlayerListener tl) {
			listeners.remove(tl);
		}

		public void notifyListeners() {
			
			PlayerStateEvent pse = new PlayerStateEvent(position, name, isInJail, account, turnToken, jailCard);
			for (PlayerListener pl : listeners) {
				pl.updatePlayer(pse);
			}
		}
	}
	
	//start value of money changes with the version of the game played.  US version 5000, Swiss version 200,000
	public Player (String name, int account){
		this.name = name;
		this.account = account;
		position = 0;
		isInJail = false;
		turnToken = false;
		jailCard = 0;
		properties = new ArrayList<Tile>();
		playerSubject = new ConcreteSubject();
	}
	
	//TODO we don't need this constructor, oder?  Delete it?
//	/**
//	 * Create a Player
//	 * @param name
//	 * 			the name of the player
//	 */
//	public Player(String name){
//		this.name = name;
//		position = 0;
//		isInJail = false;
//		turnToken = false;
//		jailCard = 0;
//		properties = new ArrayList<Tile>();
//		playerSubject = new ConcreteSubject();
//	}
	
	
	public int numberRailRoadsOwned(){
		int rrOwned=0;
		for (Tile t: properties ){
			if (t instanceof Railroad) rrOwned++;
		}
		return rrOwned;
	}
	
	public void addProperty(Tile t){
		properties.add(t);
		playerSubject.notifyListeners();
	}

	public void removeProperty(Tile t){
		properties.remove(t);
		playerSubject.notifyListeners();
	}
	
	public boolean ownsProperty(Tile t){
		return properties.contains(t);
	}
	
	public void depositMoney(int value){
		this.account+=value;
		playerSubject.notifyListeners();
	}
	
	public void withdawMoney(int value){
		if (account<value)
			throw new RuntimeException("The sum cannot be withdrawn from the player's account, because the player has insufficient funds");
		this.account-=value;
		playerSubject.notifyListeners();
	}
	
	public String getName() {
		return name;
	}

	public int getAccount() {
		return account;
	}

	public void setAccount(int account) {
		this.account = account;
	}

	public ArrayList<Tile> getProperties() {
		return properties;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
		playerSubject.notifyListeners();
	}

	public boolean isInJail() {
		return isInJail;
	}

	public void setInJail(boolean isInJail) {
		this.isInJail = isInJail;
		playerSubject.notifyListeners();
	}

	public boolean isTurnToken() {
		return turnToken;
	}

	public void setTurnToken(boolean turnToken) {
		this.turnToken = turnToken;
		playerSubject.notifyListeners();
	}

	public int getJailCard() {
		return jailCard;
	}

	public void setJailCard(int jailCard) {
		this.jailCard = jailCard;
		playerSubject.notifyListeners();
	}
	
	public void setToken(Token t){
		this.t = t;
	}
	
	public Token getToken(){
		return this.t;
	}
	
	/**
	 * Method used by PlayerListeners in the GUI in an observer pattern
	 * if something changes in the Player data, it is through the PlayerSubject that the 
	 * GUI is notified of the changes
	 * @return  the playerSubject for this player
	 */
	public PlayerSubject getPlayerSubject(){
		return playerSubject;
	}
	
}
