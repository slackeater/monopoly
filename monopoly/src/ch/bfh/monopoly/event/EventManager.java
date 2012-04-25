package ch.bfh.monopoly.event;

import java.util.ResourceBundle;

import ch.bfh.monopoly.common.GameClient;

public class EventManager {
	
	BoardEvent[] tileEvents = new BoardEvent[40];
	BoardEvent[] chanceEvents = new BoardEvent[16];
	GameClient gameClient;
	
	public EventManager(GameClient gameClient) {
		this.gameClient = gameClient;
		createTileEvents();
		createChanceMovementEvents();
		//TODO create 1 singled out event for testing... will remove later
		createGoToJailEvent();
	}

	BoardEvent[] masterList;

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
					"chance"  +i+ "-name");
			name = name.trim();
			
			String cardText = res.getString(
					"chance"  +i+ "-cardText");
			name = name.trim();
			
			String toParse = res.getString(
					"chance"  +i+ "-newPosition");
			int newPosition = Integer.parseInt(toParse);
			
			BoardEvent te = new MovementEvent(name, cardText, newPosition, gameClient);
			chanceEvents[i]=te;
			
		}
	}
	
	public void createGoToJailEvent(){
		BoardEvent te = new GoToJailEvent("Go To Jail", "You go directly to jail, do not pass go, do not collect 200 dollars", 10, gameClient);
		tileEvents[30]=te;
	}
	
	public void createTileEvents(){
		int[] terrainAndRRSpaces = {1,3,5,6,8,9,11,13,14,15,16,18,19,21,23,24,25,26,27,29,31,32,34,35,37,39};
		SimpleFeeEvent sfe = new SimpleFeeEvent("Pay Rent", gameClient);
		for (int i=0;i<terrainAndRRSpaces.length;i++){
			int boardPosition = terrainAndRRSpaces[i];
			tileEvents[boardPosition]=sfe;
		}
	}
}


   