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
	protected int numberOfTokens = 0;
	protected Token[] tokens;

	public abstract void setDraw(int numberOfTokens);

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
		if(this.numberOfTokens >= 2 && this.numberOfTokens <= 8){
			Graphics2D g2 = (Graphics2D) g;

			for(int i = 0 ; i < this.numberOfTokens ; i++){
				System.out.println("inside number");
				g2.setColor(this.tokens[i].getColor());
				g2.fillOval((int)(getWidth()*this.tokens[i].getXRatio()), (int)(getHeight()*this.tokens[i].getYRatio()), 17, 17);
			}
		}
	}


}
