package ch.bfh.monopoly.event;

import java.util.Random;
import java.util.ResourceBundle;

import ch.bfh.monopoly.common.GameClient;

public class EventManager {

	BoardEvent[] tileEvents = new BoardEvent[40];
	BoardEvent[] chanceEvents = new BoardEvent[16];
	BoardEvent[] commChestEvents = new BoardEvent[16];
	GameClient gameClient;
	ResourceBundle res;

	// set when GUI calls getEventDescription so program then knows which
	// chance / Comm chest card to execute when GUI calls performEVent()
	BoardEvent currentEvent;

	public EventManager(GameClient gameClient) {
		this.gameClient = gameClient;
		res = ResourceBundle.getBundle("events", gameClient.getLoc());
		createTileEvents();
		createChanceEvents();
		createCommChestEvents();
	}

	public String getEventDescriptionById(int tileId) {
		return tileEvents[tileId].getEventDescription();
	}

	public void performEventForTileAtId(int tileId) {
		tileEvents[tileId].performEvent();
	}

	public void performEventChance() {
		currentEvent.performEvent();
	}

	public String getEventDescriptionChance() {
		drawChanceCard();
		return currentEvent.getEventDescription();
	}

	public void performEventCommChest() {
		currentEvent.performEvent();
	}

	public String getEventDescriptionCommChest() {
		drawCommChestCard();
		return currentEvent.getEventDescription();
	}

	public void drawChanceCard() {
		Random r = new Random();
		int rand = r.nextInt(7);
		currentEvent = chanceEvents[rand];
	}

	public void drawCommChestCard() {
		Random r = new Random();
		int rand = r.nextInt(7);
		currentEvent = commChestEvents[rand];
	}

	private void createChanceEvents() {
		// MOVEMENT EVENTS
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
		// REPAIRS EVENTS
		for (int i = 7; i < 9; i++) {
			String name = res.getString("chance" + i + "-name");
			name = name.trim();
			String cardText = res.getString("chance" + i + "-cardText");
			name = name.trim();
			String toParse = res.getString("chance" + i + "-chargePerHouse");
			int chargeHouse = Integer.parseInt(toParse);
			toParse = res.getString("chance" + i + "-chargePerHotel");
			int chargeHotel = Integer.parseInt(toParse);
			BoardEvent te = new RepairsEvent(name, cardText, chargeHouse,
					chargeHotel, gameClient);
			chanceEvents[i] = te;
		}
		//GET JAIL CARD EVENT
		String name = res.getString("chance9-name");
		name = name.trim();
		String cardText = res.getString("chance9-cardText");
		name = name.trim();
		BoardEvent te = new getJailCardEvent(name, cardText, gameClient);
		chanceEvents[9] = te;

		//GET FIXED SUM EVENTS
		for (int i = 10; i < 16; i++) {
			name = res.getString("chance" + i + "-name");
			name = name.trim();
			cardText = res.getString("chance" + i + "-cardText");
			name = name.trim();
			String toParse = res.getString("chance" + i + "-fixedSum");
			int fixedSum = Integer.parseInt(toParse);
			te = new GetFixedSumEvent(name, cardText, fixedSum, gameClient);
			chanceEvents[i] = te;
		}
		
	}


	private void createCommChestEvents(){
		String name;
		String cardText;
		BoardEvent te;
		String toParse;
		
		// MOVEMENT EVENTS
		for (int i = 0; i < 3; i++) {
			name = res.getString("commchest" + i + "-name");
			name = name.trim();
			cardText = res.getString("commchest" + i + "-cardText");
			name = name.trim();
			toParse = res.getString("commchest" + i + "-newPosition");
			int newPosition = Integer.parseInt(toParse);
			te = new MovementEvent(name, cardText, newPosition,
					gameClient);
			commChestEvents[i] = te;
		}
		
//		#CASH TRANSFER CARDS
//		commchest3-name=It is your birthday 
//		commchest3-cardText=Collect $20 from each player
//		commchest3-transferAmount=20
		name = res.getString("commchest3-name");
		name = name.trim();
		cardText = res.getString("commchest3-cardText");
		name = name.trim();
		toParse = res.getString("commchest3-fixedSum");
		te = new birthdayEvent(name, cardText, gameClient);
		commChestEvents[3] = te;
	
		//GET JAIL CARD EVENT
		 name = res.getString("commchest4-name");
		name = name.trim();
		cardText = res.getString("commchest4-cardText");
		name = name.trim();
		te = new getJailCardEvent(name, cardText, gameClient);
		commChestEvents[4] = te;

		//GET FIXED SUM EVENTS
		for (int i = 5; i < 16; i++) {
			name = res.getString("commchest" + i + "-name");
			name = name.trim();
			cardText = res.getString("commchest" + i + "-cardText");
			name = name.trim();
			toParse = res.getString("commchest" + i + "-fixedSum");
			int fixedSum = Integer.parseInt(toParse);
			te = new GetFixedSumEvent(name, cardText, fixedSum, gameClient);
			commChestEvents[i] = te;
		}
		
	}
	private void createTileEvents() {
		// CREATE TERRAIN AND RAILROAD EVENTS
		int[] terrainAndRRSpaces = { 1, 3, 5, 6, 8, 9, 11, 13, 14, 15, 16, 18,
				19, 21, 23, 24, 25, 26, 27, 29, 31, 32, 34, 35, 37, 39 };
		SimpleFeeEvent sfe = new SimpleFeeEvent("Pay Rent", gameClient);
		for (int i = 0; i < terrainAndRRSpaces.length; i++) {
			int boardPosition = terrainAndRRSpaces[i];
			tileEvents[boardPosition] = sfe;
		}

		// CREATE GO EVENT
		String name = res.getString("go-name");
		name = name.trim();
		String cardText = res.getString("go-cardText");
		cardText = cardText.trim();
		String toParse = res.getString("go-amount");
		int amount = Integer.parseInt(toParse);
		BoardEvent te = new GetFixedSumEvent(name, cardText, amount, gameClient);
		tileEvents[0] = te;

		// CREATE COMMUNITY CHEST DEFAULT EVENT --> default event just sends
		// performEvent() method to get a random card here

		// CREATE INCOME TAX EVENT
		name = res.getString("incomeTax-name");
		name = name.trim();
		cardText = res.getString("incomeTax-cardText");
		cardText = cardText.trim();
		// 4 is the variable sum code, used to tell the program to perform the
		// FREE PARKING EVENT
		te = new GetVariableSumEvent(name, cardText, 4, gameClient);
		tileEvents[4] = te;

		// CREATE FREE PARKING EVENT
		name = res.getString("freeParking-name");
		name = name.trim();
		cardText = res.getString("freeParking-cardText");
		cardText = cardText.trim();
		// 20 is the variable sum code, used to tell the program to perform the
		// FREE PARKING EVENT
		te = new GetVariableSumEvent(name, cardText, 0, gameClient);
		tileEvents[20] = te;

		// GO TO JAIL TILE EVENT
		name = res.getString("goToJail-name");
		name = name.trim();
		cardText = res.getString("goToJail-cardText");
		name = name.trim();
		toParse = res.getString("goToJail-newPosition");
		int newPosition = Integer.parseInt(toParse);
		te = new GoToJailEvent(name, cardText, newPosition, gameClient);
		tileEvents[30] = te;

		// CREATE LUXURY TAX EVENT
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

	// TODO Methods Needed to Test a Specific Event : COMMENT OUT FOR FINAL
	// PRODUCT
	public BoardEvent getCurrentEvent() {
		return currentEvent;
	}

	public void setCurrentEvent(BoardEvent currentEvent) {
		this.currentEvent = currentEvent;
	}

	public BoardEvent[] getTileEvents() {
		return tileEvents;
	}

	public BoardEvent[] getChanceEvents() {
		return chanceEvents;
	}

	public BoardEvent[] getCommChestEvents() {
		return commChestEvents;
	}

}
