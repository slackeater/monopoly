package common;

public class Terrain extends Property {

	private int houseCost;
	private int hotelCost;
	private int houseCount;
	private int hotelCount;

	
	private int[] rentRates = new int[5];	
	private int rent1house;
	private int rent2house;
	private int rent3house;
	private int rent4house;
	private int renthotel;

	public Terrain (String name, int price, int rent, int houseCost, int hotelCost, int mortgageValue,
			int rent1house, int rent2house,int rent3house, int rent4house, int renthotel){
		this.name = name;
		//this.id = id;
		this.rentRates[0] =rent;
		this.rentRates[1] =rent1house;
		this.rentRates[2] =rent2house;
		this.rentRates[3] =rent2house;
		this.rentRates[4] =rent4house;
		this.renthotel=renthotel;
		this.price = price;
		//this.description=description;
		this.houseCost = houseCost;
		this.hotelCost = hotelCost;
		this.mortgageValue = mortgageValue;
		
		
		
		mortgageActive = false;
		hotelCount=0;
		houseCount=0;
	}

	public int feeToCharge(){
		return rentRates[houseCount] + renthotel*hotelCount;
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
				"\nrent: " + rentRates[0] +
				"\nrent w/Houses" + rentRates[1]+", "+rentRates[2]+", "+rentRates[3]+", "+rentRates[4]+
				"\nrent w/Hotel" + renthotel +
				"\nowner: " + owner;
	}
}
