package ch.bfh.monopoly.tile;

import ch.bfh.monopoly.common.Player;
import ch.bfh.monopoly.event.EventManager;

public abstract class Property extends AbstractTile{

	protected int price;
	protected Player owner;
	protected int mortgageValue;
	protected boolean mortgageActive;
	private String group;

	public Property(String name, int price, String group, int mortgageValue, int coordX, int coordY,int id,EventManager em){
		super(name, coordX, coordY,id,em);
		this.price=price;
		this.group=group;
		this.mortgageValue=mortgageValue;
	}
	
	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}
	
	public int getMortgageValue() {
		return mortgageValue;
	}

	public void setMortgageValue(int mortgageValue) {
		this.mortgageValue = mortgageValue;
	}
	
	
}
