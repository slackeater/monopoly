package ch.bfh.monopoly.common;

import java.awt.Color;
import java.net.Socket;
import java.util.ArrayList;

import ch.bfh.monopoly.tile.Property;
import ch.bfh.monopoly.tile.Railroad;
import ch.bfh.monopoly.tile.Tile;

public class Player {

	private String name;
	private int account;
	private ArrayList<Tile> properties;
	private int position;
	private boolean isInJail;
	private boolean turnToken;
	private int jailCard;
	
	//start value of money changes with the version of the game played.  US version 5000, Swiss version 200,000
	public Player (String name, int account){
		this.name = name;
		this.account = account;
		position = 0;
		isInJail = false;
		turnToken = false;
		jailCard = 0;
		properties = new ArrayList<Tile>();
	}
	
	public int numberRailRoadsOwned(){
		int rrOwned=0;
		for (Tile t: properties ){
			if (t instanceof Railroad) rrOwned++;
		}
		return rrOwned;
	}
	
	public void addProperty(Tile t){
		//here we must remove the Tile from the list of the previous owner if it was previously owned
		Player prevOwner;
		Property property = (Property)t;
		if (property.owner != null){
			prevOwner = property.owner;
			prevOwner.removeProperty(property);

		}
		property.setOwner(this);
		properties.add(property);
	}

	public boolean removeProperty(Tile t){
		return properties.remove(t);
	}
	
	public boolean ownsProperty(Tile t){
		return properties.contains(t);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public void setProperties(ArrayList<Tile> properties) {
		this.properties = properties;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public boolean isInJail() {
		return isInJail;
	}

	public void setInJail(boolean isInJail) {
		this.isInJail = isInJail;
	}

	public boolean isTurnToken() {
		return turnToken;
	}

	public void setTurnToken(boolean turnToken) {
		this.turnToken = turnToken;
	}

	public int getJailCard() {
		return jailCard;
	}

	public void setJailCard(int jailCard) {
		this.jailCard = jailCard;
	}
	
	
}
