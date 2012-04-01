package ch.bfh.monopoly.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ch.bfh.monopoly.common.Token;
import ch.bfh.monopoly.tile.TileInfo;

public class BoardTile extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3335141445010622095L;
	private int numberOfTokens = 0;
	private Set<Token> tokens = new HashSet<Token>();
	private TileInfo ti;
	
	/**
	 * Construct a new BoardTile
	 * @param ti the TileInfo used to passed the information
	 */
	public BoardTile(TileInfo ti){
		this.ti = ti;
		setBorder(BorderFactory.createEtchedBorder());
		setLayout(new GridLayout(2,1));
		add(new JLabel(ti.getGroup()));
		add(new JLabel(ti.getName()));
	}
	
	/**
	 * Get the X coordinate of this tile
	 * @return an int that correspond to the X coordinate
	 */
	public int getTileInfoX(){
		return ti.getCoordX();
	}
	
	/**
	 * Get the Y coordinate of this tile
	 * @return an int that correspond to the Y coordinate
	 */
	public int getTileInfoY(){
		return ti.getCoordY();
	}
	
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
	
}
