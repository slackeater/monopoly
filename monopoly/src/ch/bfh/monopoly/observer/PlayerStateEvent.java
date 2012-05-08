package ch.bfh.monopoly.observer;

public class PlayerStateEvent {

	int position;
	String name;
	boolean isInJail;
	int account;
	boolean turnToken;
	int jailCard;
	boolean[] terrains;
	
	//TODO this class must also carry information Property List... but we don't want to copy the entire list, oder?
	public PlayerStateEvent(int position, String name, boolean isInJail,
			int account, boolean turnToken, int jailCard, boolean[] terrains) {
		super();
		this.position = position;
		this.name = name;
		this.isInJail = isInJail;
		this.account = account;
		this.turnToken = turnToken;
		this.jailCard=jailCard;
		this.terrains=terrains;
	}

	public int getPosition() {
		return position;
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

	public boolean isTurnToken() {
		return turnToken;
	}
	
	public int getJailCard() {
		return jailCard;
	}

	public boolean[] getTerrains() {
		return terrains;
	}
	
	
}
