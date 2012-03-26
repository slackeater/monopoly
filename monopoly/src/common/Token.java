package common;
import java.awt.Color;


public class Token {

	private Color color;
	private int x_ratio;
	private int y_ratio;
	
	public Token(Color c, int x, int y){
		color = c;
		x_ratio = x;
		y_ratio = y;
	}
	
	public Color getColor(){
		return color;
	}
	
	public int getXratio(){
		return x_ratio;
	}

	public int getYratio(){
		return y_ratio;
	}
	
}
