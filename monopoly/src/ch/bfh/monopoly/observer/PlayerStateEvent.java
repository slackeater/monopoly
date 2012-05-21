package ch.bfh.monopoly.observer;

import ch.bfh.monopoly.common.Token;

public class PlayerStateEvent {

	int position;
	String name;
	boolean isInJail;
	int account;
	boolean turnToken;
	int jailCard;
	boolean[] terrains;
	private Token t;
	int rollValue;
	int previousPosition;


	//TODO this class must also carry information Property List... but we don't want to copy the entire list, oder?
	public PlayerStateEvent(int position, int previousPosition, int rollValue, String name, boolean isInJail,
			int account, boolean turnToken, int jailCard, boolean[] terrains, Token t) {
		super();
		this.position = position;
		this.previousPosition = previousPosition;
		this.rollValue= rollValue;
		this.name = name;
		this.isInJail = isInJail;
		this.account = account;
		this.turnToken = turnToken;
		this.jailCard=jailCard;
		this.terrains=terrains;
		this.t = t;
	}

	public int getPosition() {
		return position;
	}

	public int getPreviousPosition() {
		return previousPosition;
	}
	
	public int getRollValue() {
		return rollValue;
	}

	public String getName() {
		return name;
	}

	public boolean isInJail() {
		return isInJail;
	}

	public int getAccount() {
		return account;
	}

	public boolean hasTurnToken() {
		return turnToken;
	}
	
	public int getJailCard() {
		return jailCard;
	}

	public boolean[] getTerrains() {
		return terrains;
	}
	
	public Token getT() {
		return t;
	}
	
}
