package common;

import java.awt.Color;
import java.net.Socket;
import java.util.ArrayList;

public class Player {

	private String name;
	private int account;
	private ArrayList<Tile> properties;
	//private Color color;
	private int position;
	private Socket socket;
	private boolean isInJail;
	private boolean turnToken;
	private int jailCard;
	
	//start value of money changes with the version of the game played.  US version 5000, Swiss version 200,000
	public Player (String name, Color color, int account, Socket socket){
		//assumed that 'Go' is position 0
		this.name = name;
		//this.color = color;
		this.account = account;
		position = 0;
		this.socket = socket;
		isInJail = false;
		turnToken = false;
		jailCard = 0;
		
		
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
