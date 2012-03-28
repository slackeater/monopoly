package common;

public class Terrain extends Property {

	private int houseCost;
	private int hotelCost;
	private int houseCount;
	private int hotelCount;
	private int mortgageBonus;
	private boolean mortgageActive;
	
	public Terrain (String name, int id, String description, int houseCost, int hotelCost, int mortgageBonus){
		this.name = name;
		this.id = id;
		this.description=description;
		this.houseCost = houseCost;
		this.hotelCost = hotelCost;
		this.mortgageBonus = mortgageBonus;
		
		
		
		mortgageActive = false;
		hotelCount=0;
		houseCount=0;
	}
}
