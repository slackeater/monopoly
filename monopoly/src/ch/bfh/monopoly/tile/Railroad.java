package ch.bfh.monopoly.tile;

public class Railroad extends Property {
	private int rent;
	private int[] rrMultiplier = {1,2,4,8};
	

	public Railroad (String name, int price, int rent, String group, int mortgageValue, int coordX, int coordY, int id){ 
		super(name,  price,  group,  mortgageValue,  coordX,  coordY, id);
		this.rent = rent;
	} 
	
	
	//calculates the fee to charge by checking how many RRs are owned by the owner of this tile
	//the base rent value of 25 is mutilplied by 1,2,4, or 8 depending on the number of RRs owned
	public int feeToCharge(){
		int numberOfRRsOwned = this.owner.numberRailRoadsOwned();
		return rrMultiplier[numberOfRRsOwned-1] * rent;
	}


	public int getRent() {
		return rent;
	}
	
	@Override
	public int getID(){
		return super.getId();
	}

	public void setRent(int rent) {
		this.rent = rent;
	}
	
	public String toString(){
		return super.toString() + "\nname: " + name +
				"\nprice: " + this.price +
				"\nrent: " + this.rent +
				"\nmortgageValue: " + mortgageValue +
				"\nowner: " + owner;
	}
}
