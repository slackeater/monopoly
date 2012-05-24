package ch.bfh.monopoly.tile;

import javax.swing.JPanel;

public interface Tile{
	String getName();
	int getTileId();
	int getCoordX();
	int getCoordY();
	public JPanel getTileEventPanel();
	
	
}
