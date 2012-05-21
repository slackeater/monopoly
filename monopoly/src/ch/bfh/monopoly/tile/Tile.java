package ch.bfh.monopoly.tile;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public interface Tile{
	String getName();
	int getId();
	int getCoordX();
	int getCoordY();
	public String getEventDescription();
	public void performEvent();
	public List<ActionListener> getActionListenerList();
	
	
}
