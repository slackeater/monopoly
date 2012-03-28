package common;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Tile extends JPanel{
	int id;
	String description;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int tokenNumber = 0;
	private final int PADDING_X = (int) (getWidth()*0.8);
	private final int PADDING_Y = (int) (getHeight()*0.8);
	private Token tokens[] = new Token[8];
	private BufferedImage img;
	

	public Tile(){
		tokens[0] = new Token(Color.RED, 5, 20);
		tokens[1] = new Token(Color.BLACK, 10, 30);
		tokens[2] = new Token(Color.YELLOW, 15, 15);
		tokens[3] = new Token(Color.BLUE, 25, 40);
		tokens[4] = new Token(Color.GRAY, 50, 50);
		tokens[5] = new Token(Color.GREEN, 40, 25);
		tokens[6] = new Token(Color.WHITE, 15, 50);
		tokens[7] = new Token(Color.CYAN, 15, 40);
		
		try {
			img = ImageIO.read(new File("/home/snake/controller.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void setDraw(int tokenNumber){
		this.tokenNumber = tokenNumber;
	}

	@Override
	public void paintComponent(Graphics g){
		if(tokenNumber >= 1 && tokenNumber <= 8){
			for(int j = 0 ; j < tokenNumber ; j++){
				Graphics2D g2 = (Graphics2D) g;
				g2.drawImage(img, PADDING_X +tokens[j].getXratio(), PADDING_Y+tokens[j].getYratio(), null);
				//g2.setColor(tokens[j].getColor());
				//g2.fillOval(PADDING_X +tokens[j].getXratio() , PADDING_Y+tokens[j].getYratio() , 17, 17);
			}
		}

	}

}
