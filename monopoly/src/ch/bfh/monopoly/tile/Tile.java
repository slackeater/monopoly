package ch.bfh.monopoly.tile;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import ch.bfh.monopoly.common.WindowBuilder;

public interface Tile{
	String getName();
	int getTileId();
	int getCoordX();
	int getCoordY();
	public String getEventDescription();
	public void performEvent();
	public JPanel getTileEventPanel();
	
	
}
