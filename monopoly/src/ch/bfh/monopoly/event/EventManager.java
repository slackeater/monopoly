package ch.bfh.monopoly.event;

import java.util.Random;
import java.util.ResourceBundle;

import ch.bfh.monopoly.common.GameClient;

public class EventManager {

	BoardEvent[] tileEvents = new BoardEvent[40];
	BoardEvent[] chanceEvents = new BoardEvent[16];
	GameClient gameClient;
	ResourceBundle res;
	BoardEvent[] masterList;

	public EventManager(GameClient gameClient) {
		this.gameClient = gameClient;
		res = ResourceBundle.getBundle("events", gameClient.getLoc());
		createTileEvents();
		createChanceMovementEvents();
		createFreeParkingAndGoEvents();
		// TODO create 1 singled out event for testing... will remove later
		createGoToJailEvent();
	}

	public String getEventDescriptionById(int tileId) {
		return tileEvents[tileId].getEventDescription();
	}

	public void performEventForTileAtId(int tileId) {
		tileEvents[tileId].performEvent();
	}
	
	public void performChanceEvent(){
		Random r = new Random();
		int rand = r.nextInt(7);
		System.out.println(chanceEvents[rand].getEventDescription()); 
		chanceEvents[rand].performEvent();
	}
	
	public void performCommChestEvent(){
		//TODO CHOOSE COMM CHEST CARD AT RANDOM
	}

	public void createChanceMovementEvents() {
		for (int i = 0; i < 7; i++) {
			String name = res.getString("chance" + i + "-name");
			name = name.trim();

			String cardText = res.getString("chance" + i + "-cardText");
			name = name.trim();

			String toParse = res.getString("chance" + i + "-newPosition");
			int newPosition = Integer.parseInt(toParse);

			BoardEvent te = new MovementEvent(name, cardText, newPosition,
					gameClient);
			chanceEvents[i] = te;

		}
	}

	public void createGoToJailEvent() {
		String name = res.getString("goToJail-name");
		name = name.trim();
		String cardText = res.getString("goToJail-cardText");
		name = name.trim();
		String toParse = res.getString("goToJail-newPosition");
		int newPosition = Integer.parseInt(toParse);
		BoardEvent te = new GoToJailEvent(name, cardText, newPosition,
				gameClient);
		tileEvents[30] = te;
	}

	public void createFreeParkingAndGoEvents() {
		// CREATE GO
		String name = res.getString("go-name");
		name = name.trim();
		String cardText = res.getString("go-cardText");
		cardText = cardText.trim();
		String toParse = res.getString("go-amount");
		int amount = Integer.parseInt(toParse);
		BoardEvent te = new GetFixedSumEvent(name, cardText, amount, gameClient);
		tileEvents[0] = te;

		//CREATE FREE PARKING
		name = res.getString("freeParking-name");
		name = name.trim();
		cardText = res.getString("freeParking-cardText");
		cardText = cardText.trim();
		//20 is the variable sum code, used to tell the program to perform the FREE PARKING EVENT
		te = new GetVariableSumEvent(name, cardText, 0,gameClient);
		tileEvents[20] = te;
		
		//CREATE INCOME TAX
		name = res.getString("incomeTax-name");
		name = name.trim();
		cardText = res.getString("incomeTax-cardText");
		cardText = cardText.trim();
		//4 is the variable sum code, used to tell the program to perform the FREE PARKING EVENT
		te = new GetVariableSumEvent(name, cardText, 4,gameClient);
		tileEvents[4] = te;
		
		//CREATE LUXURY TAX
		name = res.getString("luxuryTax-name");
		name = name.trim();
		cardText = res.getString("luxuryTax-cardText");
		cardText = cardText.trim();
		toParse = res.getString("luxuryTax-amount");
		toParse = toParse.trim();
		amount = Integer.parseInt(toParse);
		te = new GetFixedSumEvent(name, cardText, amount, gameClient);
		tileEvents[38] = te;
		
	}

	public void createTileEvents() {
		int[] terrainAndRRSpaces = { 1, 3, 5, 6, 8, 9, 11, 13, 14, 15, 16, 18,
				19, 21, 23, 24, 25, 26, 27, 29, 31, 32, 34, 35, 37, 39 };
		SimpleFeeEvent sfe = new SimpleFeeEvent("Pay Rent", gameClient);
		for (int i = 0; i < terrainAndRRSpaces.length; i++) {
			int boardPosition = terrainAndRRSpaces[i];
			tileEvents[boardPosition] = sfe;
		}
	}
}
