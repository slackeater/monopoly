package ch.bfh.monopoly.tile;

import ch.bfh.monopoly.common.Player;
import ch.bfh.monopoly.event.EventManager;

public class Railroad extends Property {
	private int rent;
	private int[] rrMultiplier = {1,2,4,8};
	

	public Railroad (String name, int price, int rent, String group, int mortgageValue, int coordX, int coordY, int id,EventManager em,Player bank){ 
		super(name,  price,  group,  mortgageValue,  coordX,  coordY, id,em,bank);
		this.rent = rent;
	} 
	
	
	//calculates the fee to charge by checking how many RRs are owned by the owner of this tile
	//the base rent value of 25 is mutilplied by 1,2,4, or 8 depending on the number of RRs owned
	public int feeToCharge(){
		int numberOfRRsOwned = this.owner.numberRailRoadsOwned();
		return rrMultiplier[numberOfRRsOwned-1] * rent;
	}

	@Override
	public final String getEventDescription() {
		return em.getEventDescriptionById(id);
	}

	@Override
	public void performEvent() {
		em.performEventForTileAtId(id);
	}
	
	

	public int getRent() {
		return rent;
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
