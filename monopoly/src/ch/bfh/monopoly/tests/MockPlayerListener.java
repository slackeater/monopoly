package ch.bfh.monopoly.tests;

import javax.swing.text.PlainView;

import ch.bfh.monopoly.observer.PlayerListener;
import ch.bfh.monopoly.observer.PlayerStateEvent;

public class MockPlayerListener implements PlayerListener{

	@Override
	public void updatePlayer(PlayerStateEvent pse) {
		System.out.println("Player updated: player information for player: "+pse.getName() + "has changed");
		
	}

}
