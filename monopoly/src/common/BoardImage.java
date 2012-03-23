package common;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class BoardImage extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1215618660838287680L;
	private BufferedImage img;
	
	public BoardImage(){
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			InputStream input = classLoader.getResourceAsStream("resources/monopoly.png");

			img = ImageIO.read(input);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override 
	public void paintComponent(Graphics g){
		g.drawImage(img, 0, 0, 735, 734, null);
	}
}
