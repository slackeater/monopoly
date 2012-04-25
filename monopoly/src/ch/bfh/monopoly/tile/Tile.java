package ch.bfh.monopoly.tile;

public interface Tile{
	String getName();
	int getId();
	int getCoordX();
	int getCoordY();
	public String getEventDescription();
	public void performEvent();
	
	
}
