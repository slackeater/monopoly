package ch.bfh.monopoly.tile;

public class TileInfo {

	String name;
	String owner;
	int price;
	int houseCost;
	int hotelCost;
	int rent;
	int rent1house;
	int rent2house;
	int rent3house;
	int rent4house;
	int renthotel;
	String group;
	int mortgageValue;
	int coordX;
	int coordY;
	int id;
	String rgb;

	public TileInfo() {
		name = null;
		owner = null;
		price = -1;
		houseCost = -1;
		hotelCost = -1;
		rent = -1;
		rent1house = -1;
		rent2house = -1;
		rent3house = -1;
		rent4house = -1;
		renthotel = -1;
		group = null;
		mortgageValue = -1;
		coordX = -1;
		coordY = -1;
		id = -1;
		rgb = null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
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

	public int getRent() {
		return rent;
	}

	public void setRent(int rent) {
		this.rent = rent;
	}

	public int getRent1house() {
		return rent1house;
	}

	public void setRent1house(int rent1house) {
		this.rent1house = rent1house;
	}

	public int getRent2house() {
		return rent2house;
	}

	public void setRent2house(int rent2house) {
		this.rent2house = rent2house;
	}

	public int getRent3house() {
		return rent3house;
	}

	public void setRent3house(int rent3house) {
		this.rent3house = rent3house;
	}

	public int getRent4house() {
		return rent4house;
	}

	public void setRent4house(int rent4house) {
		this.rent4house = rent4house;
	}

	public int getRenthotel() {
		return renthotel;
	}

	public void setRenthotel(int renthotel) {
		this.renthotel = renthotel;
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

	public int getCoordX() {
		return coordX;
	}

	public void setCoordX(int coordX) {
		this.coordX = coordX;
	}

	public int getCoordY() {
		return coordY;
	}

	public void setCoordY(int coordY) {
		this.coordY = coordY;
	}
	
	public String getRGB(){
		return this.rgb;
	}
	
	public void setRGB(String rgb){
		this.rgb = rgb;
	}
	
	public int getId(){
		return this.id;
	}
	
	public void setId(int id){
		this.id = id;
	}

}
