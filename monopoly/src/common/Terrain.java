package common;

public class Terrain extends Property {

	private int houseCost;
	private int hotelCost;
	private int houseCount;
	private int hotelCount;
	private int mortgageValue;
	private boolean mortgageActive;
	
	private int rent;	
	private int rent1house;
	private int rent2house;
	private int rent3house;
	private int rent4house;
	private int rent1hotel;

	public Terrain (String name, int price, int rent, int houseCost, int hotelCost, int mortgageValue){
		this.name = name;
		this.id = id;
		this.rent = rent;
		this.price = price;
		this.description=description;
		this.houseCost = houseCost;
		this.hotelCost = hotelCost;
		this.mortgageValue = mortgageValue;
		
		
		
		mortgageActive = false;
		hotelCount=0;
		houseCount=0;
	}

	
	public int getHouseCost() {
		return houseCost;
	}

	public void setHouseCost(int houseCost) {
		this.houseCost = houseCost;
	}

	public int getHotelCost() {
		return hotelCost;
	}

	public void setHotelCost(int hotelCost) {
		this.hotelCost = hotelCost;
	}

	public int getHouseCount() {
		return houseCount;
	}

	public void setHouseCount(int houseCount) {
		this.houseCount = houseCount;
	}

	public int getHotelCount() {
		return hotelCount;
	}

	public void setHotelCount(int hotelCount) {
		this.hotelCount = hotelCount;
	}

	public int getMortgageValue() {
		return mortgageValue;
	}

	public void setMortgageValue(int mortgageValue) {
		this.mortgageValue = mortgageValue;
	}

	public boolean isMortgageActive() {
		return mortgageActive;
	}

	public void setMortgageActive(boolean mortgageActive) {
		this.mortgageActive = mortgageActive;
	}
	
	public String toString(){
		return super.toString() + "\nname: " + name +
				"\nprice: " + this.price +
				"\nhouseCost: " + houseCost +
				"\nhotelCost: " + hotelCost +
				"\nmortgageValue: " + mortgageValue +
				"\nrent: " + rent;
	}
}
