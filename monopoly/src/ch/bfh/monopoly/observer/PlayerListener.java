package ch.bfh.monopoly.observer;

import java.util.ArrayList;

public interface PlayerListener {
	public void updatePlayer(ArrayList<PlayerStateEvent> playerStates);
}
