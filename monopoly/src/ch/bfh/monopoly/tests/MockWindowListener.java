package ch.bfh.monopoly.tests;

import ch.bfh.monopoly.common.WindowListener;

public class MockWindowListener implements WindowListener{

	@Override
	public void updateWindow(String text) {
		System.out.println("Window updated: " + text);
		
	}

}
