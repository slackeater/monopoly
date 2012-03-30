package ch.bfh.monopoly.tile;

public class Utility extends Property{

	public Utility (String name, int price, int mortgageValue){ 
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
