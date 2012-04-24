package ch.bfh.monopoly.event;

import ch.bfh.monopoly.common.GameClient;

public class EventManager {
	
	TileEvent[] tileEvents = new TileEvent[40];
	
	public EventManager(GameClient gameClient) {
		// TODO Auto-generated constructor stub
	}

	TileEvent[] masterList;

	public String getEventDescriptionById(int tileId) {
		return tileEvents[tileId].getEventDescription();
	}

	public void performEventForTileAtId(int tileId) {
		tileEvents[tileId].performEvent();
		
	}
	
}
   