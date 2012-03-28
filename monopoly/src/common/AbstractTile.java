package common;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public abstract class AbstractTile extends JPanel implements Tile {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String description;
	private String name;
	private int numberOfTokens;
	private Token[] tokens;
	private int X_PADDING = 40;
	private int Y_PADDING = 40;

	public void setDraw(int numberOfTokens){
		this.numberOfTokens = numberOfTokens;
		this.tokens = new Token[this.numberOfTokens];

		//TEST
		for(int j = 0 ; j < this.numberOfTokens ; j++){
			tokens[j] = new Token(Color.RED, 20, 20);
		}
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Override 
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;

		for(int i = 0 ; i < this.numberOfTokens ; i++){
			g2.drawOval(this.X_PADDING + this.tokens[0].getXratio(), this.Y_PADDING + this.tokens[0].getYratio(), 17, 17);
		}
	}


}
