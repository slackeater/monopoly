package ch.bfh.monopoly.tests;

import javax.swing.text.PlainView;

import ch.bfh.monopoly.common.PlayerListener;
import ch.bfh.monopoly.common.PlayerStateEvent;

public class MockPlayerListener implements PlayerListener{

	@Override
	public void updatePlayer(PlayerStateEvent pse) {
		System.out.println("Player updated: player information for player: "+pse.getName() + "has changed");
		
	}

}
