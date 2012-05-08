package ch.bfh.monopoly.tests;

import java.util.ArrayList;

import javax.swing.text.PlainView;

import ch.bfh.monopoly.observer.PlayerListener;
import ch.bfh.monopoly.observer.PlayerStateEvent;

public class MockPlayerListener implements PlayerListener{


	@Override
	public void updatePlayer(ArrayList<PlayerStateEvent> playerStates) {
		System.out.println("Player updated: player information for player: "+playerStates.get(1).getName() + "has changed");
		
	}

}
