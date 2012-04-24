package ch.bfh.monopoly.tile;

public interface Tile{
	String getName();
	int getID();
	int getCoordX();
	int getCoordY();
	public String getEventDescription();
	public void performEvent();
	
	
}
