package ch.bfh.monopoly.tests;

import ch.bfh.monopoly.observer.WindowListener;

public class MockWindowListener implements WindowListener{

	@Override
	public void updateWindow(String text) {
		System.out.println("Window updated: " + text);
		
	}

}
