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

import ch.bfh.monopoly.common.TileEvent;
import ch.bfh.monopoly.common.Token;

public abstract class AbstractTile extends JPanel implements Tile {

	private static final long serialVersionUID = 1L;
	private int id;
	private int coordX;
	private int coordY;
	private String description;
	protected String name;
	protected TileEvent event;

	private int numberOfTokens = 0;
	protected Set<Token> tokens = new HashSet<Token>();

	/**
	 * Remove a token from this tile
	 * @param index the index of the token to be removed
	 */
	public void removeToken(Token t){
		this.tokens.remove(t);
	}

	/**
	 * Add a token to this tile
	 * @param t the token to be added
	 */
	public void addToken(Token t){
		this.tokens.add(t);
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

	/**
	 * Draw the tokens on this tile
	 */
	@Override 
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;

		this.numberOfTokens = tokens.size();

		if(this.numberOfTokens >= 1 && this.numberOfTokens <= 8){
			for(int i = 0 ; i < this.numberOfTokens ; i++){
				Iterator<Token> itr = this.tokens.iterator();

				while(itr.hasNext()){
					Token t = itr.next();
					g2.setColor(t.getColor());
					g2.fillOval((int)(getWidth()*t.getXRatio()), (int)(getHeight()*t.getYRatio()), (int)(getHeight()*0.25), (int)(getHeight()*0.25));
				}
			}
		}

	}

	/**
	 * Set the X coordinate for this tile
	 * it will be used for draw the board
	 * @param coordX the X coordinate
	 */
	public void setCoordX(int coordX) {
		this.coordX = coordX;
	}

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


}
