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
	//this array of token is used to initialize the tokens
	//then, only the necessary token will be copied to tokens
	protected Token[] initTokens = new Token[8];
	
	protected Token[] tokens;
	
	public AbstractTile(){
		initTokens[0] = new Token(Color.RED,0.1,0.375);
		initTokens[1] = new Token(Color.GREEN, 0.3, 0.375);
		initTokens[2] = new Token(Color.BLUE, 0.5, 0.375);
		initTokens[3] = new Token(Color.YELLOW, 0.7, 0.375);
		initTokens[4] = new Token(Color.BLACK, 0.1, 0.700);
		initTokens[5] = new Token(Color.CYAN, 0.3, 0.700);
		initTokens[6] = new Token(Color.GRAY, 0.5, 0.700);
		initTokens[7] = new Token(Color.ORANGE, 0.7, 0.700);
	}
	
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
				g2.setColor(this.tokens[i].getColor());
				g2.fillOval((int)(getWidth()*this.tokens[i].getXRatio()), (int)(getHeight()*this.tokens[i].getYRatio()), (int)(getHeight()*0.25), (int)(getHeight()*0.25));
			}
		}
	}


}
