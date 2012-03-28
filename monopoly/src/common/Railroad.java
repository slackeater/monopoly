package common;

public class Railroad extends Property {
	private int rent;
	private int[] rrMultiplier = {1,2,4,8};
	
	public Railroad (String name, int price, int rent, int mortgageValue){ 
		this.name = name;
		this.price = price;
		this.rent = rent;
		this.mortgageValue= mortgageValue;
	} 
	
	
	//calculates the fee to charge by checking how many RRs are owned by the owner of this tile
	//the base rent value of 25 is mutilplied by 1,2,4, or 8 depending on the number of RRs owned
	public int feeToCharge(){
		int numberOfRRsOwned = this.owner.numberRailRoadsOwned();
		return rrMultiplier[numberOfRRsOwned-1] * rent;
	}
}
