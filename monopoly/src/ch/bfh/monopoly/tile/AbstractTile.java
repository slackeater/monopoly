package ch.bfh.monopoly.tile;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import ch.bfh.monopoly.common.Token;
import ch.bfh.monopoly.event.EventManager;
import ch.bfh.monopoly.event.BoardEvent;

public abstract class AbstractTile implements Tile {

	private static final long serialVersionUID = 1L;
	protected int id;
	private int coordX;
	private int coordY;
	private String description;
	protected String name;
	protected BoardEvent event;
	protected EventManager em;
	
	public AbstractTile(String name, int coordX, int coordY, int id,EventManager em){
		this.name= name;
		this.coordX=coordX;
		this.coordY=coordY;
		this.id=id;
		this.em=em;
	}

	/**
	 * Get the id of this tile
	 * @return an int that correspond to the id 
	 */
	public int getId() {
		return id;
	}

	/**
	 * Set the id for this tile
	 * @param id the id of this tile
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * MAYBE TO DELETE
	 * @return
	 */
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Get the name of this tile
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name of this tile
	 */
	public void setName(String name) {
		this.name = name;
	}

//	/**
//	 * Set the X coordinate for this tile
//	 * it will be used for draw the board
//	 * @param coordX the X coordinate
//	 */
//	public void setCoordX(int coordX) {
//		this.coordX = coordX;
//	}

	/**
	 * Get the X coordinate of this tile
	 * @return an int representing the x coordindate
	 */
	public int getCoordX() {
		return coordX;
	}

	/**
	 * Set the Y coordinate of this tile
	 * @param coordY the Y coordinate
	 */
	public void setCoordY(int coordY) {
		this.coordY = coordY;
	}

	/**
	 * Get the Y coordinate of this tile
	 * @return an int representing the y coordindate
	 */
	public int getCoordY() {
		return coordY;
	}
	
	//TODO Needed to test events, delete or comment out for final product
	public EventManager getEventManager(){
		return this.em;
	}


}
