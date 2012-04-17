package ch.bfh.monopoly.common;

public class TileStateEvent {
	private final int houseCount;
	private final int hotelsCount;
	private final String owner;
	private final boolean mortgageActive;
	
	public TileStateEvent(int houseCount, int hotelCount, String owner, boolean mortgageActive){
		this.houseCount=houseCount;
		this.hotelsCount=hotelCount;
		this.owner=owner;
		this.mortgageActive=mortgageActive;
	}

	public int getHouseCount() {
		return houseCount;
	}

	public int getHotelsCount() {
		return hotelsCount;
	}

	public String getOwner() {
		return owner;
	}

	public boolean isMortgageActive() {
		return mortgageActive;
	}
}
