package common;
import java.awt.Color;


public class Token {

	private Color color;
	private double xRatio;
	private double yRatio;
	
	public Token(Color c, double xRatio, double yRatio){
		color = c;
		this.xRatio = xRatio;
		this.yRatio = yRatio;
	}
	
	public Color getColor(){
		return color;
	}
	
	public double getXRatio(){
		return xRatio;
	}

	public double getYRatio(){
		return yRatio;
	}
	
}
