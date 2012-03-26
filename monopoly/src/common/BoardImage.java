package common;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class BoardImage extends JPanel{

	int x;
	int y;
	Graphics g2;
	/**
	 * 
	 */
	private static final long serialVersionUID = -1215618660838287680L;
	
	public BoardImage(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public void notG(){
		g2.dispose();
	}
	
	@Override 
	public void paintComponent(Graphics g){
		g2 = (Graphics2D)g;
		g2.setColor(Color.RED);
		g2.fill3DRect(x, y, 25, 25, true);
	}
}
