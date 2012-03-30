package common;

import java.awt.Color;
import java.util.Random;

public class TestTile extends AbstractTile {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TestTile(){
		super();
	}
	
	@Override
	public void setDraw(int numberOfTokens) {
		super.numberOfTokens = numberOfTokens;
		super.tokens = new Token[super.numberOfTokens];

		for(int i = 0 ; i < numberOfTokens ; i++){
			tokens[i] = super.initTokens[i];
		}
	}

}
