package common;

import java.awt.Color;
import java.util.Random;

public class TestTile extends AbstractTile {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void setDraw(int numberOfTokens) {
		super.numberOfTokens = numberOfTokens;
		super.tokens = new Token[super.numberOfTokens];


		tokens[0] = new Token(Color.RED,0.1,0.375);
		tokens[1] = new Token(Color.GREEN, 0.3, 0.375);
		tokens[2] = new Token(Color.BLUE, 0.5, 0.375);
		tokens[3] = new Token(Color.YELLOW, 0.7, 0.375);
		tokens[4] = new Token(Color.BLACK, 0.1, 0.700);
		tokens[5] = new Token(Color.CYAN, 0.3, 0.700);
		tokens[6] = new Token(Color.GRAY, 0.5, 0.700);
		tokens[7] = new Token(Color.ORANGE, 0.7, 0.700);
		
	}

}
