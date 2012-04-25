package ch.bfh.monopoly.event;

import java.util.ResourceBundle;

import ch.bfh.monopoly.common.GameClient;

public class EventManager {
	
	TileEvent[] tileEvents = new TileEvent[40];
	TileEvent[] chanceEvents = new TileEvent[16];
	GameClient gameClient;
	
	public EventManager(GameClient gameClient) {
		this.gameClient = gameClient;
		createChanceMovementEvents();
		//TODO create 1 singled out event for testing... will remove later
		createGoToJailEvent();
	}

	TileEvent[] masterList;

	public String getEventDescriptionById(int tileId) {
		return tileEvents[tileId].getEventDescription();
	}

	public void performEventForTileAtId(int tileId) {
		tileEvents[tileId].performEvent();
	}
	
	
	public void createChanceMovementEvents(){
		ResourceBundle res = ResourceBundle.getBundle("chance", gameClient.getLoc());
		
		for(int i=0;i<7;i++){
			String name = res.getString(
					"card"  +i+ "-name");
			name = name.trim();
			
			String cardText = res.getString(
					"card"  +i+ "-cardText");
			name = name.trim();
			
			String toParse = res.getString(
					"card"  +i+ "-newPosition");
			int newPosition = Integer.parseInt(toParse);
			
			TileEvent te = new MovementEvent(name, cardText, newPosition, gameClient);
			chanceEvents[i]=te;
			
		}
	}
	
	public void createGoToJailEvent(){
		TileEvent te = new GoToJailEvent("Go To Jail", "You go directly to jail, do not pass go, do not collect 200 dollars", 10, gameClient);
		tileEvents[30]=te;
	}
}


   