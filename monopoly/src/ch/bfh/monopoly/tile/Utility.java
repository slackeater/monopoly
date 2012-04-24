package ch.bfh.monopoly.tile;

import ch.bfh.monopoly.event.EventManager;

public class Utility extends Property{

	public Utility (String name, int price,String group, int mortgageValue, int coordX, int coordY, int id,EventManager em){ 
		super(name,  price,  group,  mortgageValue,  coordX,  coordY, id,em);
		this.name = name;
		this.price = price;
		this.mortgageValue= mortgageValue;
	} 
	
	public int feeToCharge(){
		//check if owner of this utility owns the other utility
		//generate a random number to simulate ROLL
		//if owner owns other utility  return randNumber * 10
		//else return randNumber * 4
		return 0;
	}
	
	public String toString(){
		return super.toString() + "\nname: " + name +
				"\nprice: " + this.price +
				"\nmortgageValue: " + mortgageValue +
				"\nowner: " + owner;
	}
	
}
