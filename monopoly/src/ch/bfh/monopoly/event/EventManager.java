package ch.bfh.monopoly.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import javax.swing.JPanel;
import ch.bfh.monopoly.common.GameClient;

public class EventManager {

	public GameClient gameClient;
	ResourceBundle res;
	// length of 16 for each deck of chance-type cards
	List<BoardEvent> chanceEvents = new ArrayList<BoardEvent>();
	List<BoardEvent> commChestEvents = new ArrayList<BoardEvent>();

	// these lists have numbers from 0-15 shuffled up, used to index into real
	// card lists
	int[] chanceEventsShuffled = new int[16];
	int[] commChestEventsShuffled = new int[16];
	ArrayList<Integer> integersTo16;

	int chanceDrawIndex=16;
	int commChestDrawIndex=16;

	private boolean testing;

	// set when GUI calls getEventDescription so program then knows which
	// chance / Comm chest card to execute when GUI calls performEVent()
	BoardEvent currentEvent;

	public EventManager(GameClient gameClient,boolean testOff) {
		this.testing = testOff;
		integersTo16 = makeIntegerList();
		this.gameClient = gameClient;
		res = ResourceBundle.getBundle("ch.bfh.monopoly.resources.events",
				gameClient.getLoc());
		createChanceEvents();
		createCommChestEvents();
	}

	private void shuffleChanceCards() {
		chanceEventsShuffled = shuffleDeck();
		//set a number here to test that event in particular
		chanceDrawIndex = 5;
		if (testing)
			gameClient.updateChanceDrawOrder(chanceEventsShuffled, testing);
	}

	private void shuffleCommChestCards() {
		commChestEventsShuffled = shuffleDeck();
		//set a number here to test that event in particular
		commChestDrawIndex = 0;
		if (testing)
			gameClient.updateCommChestDrawOrder(commChestEventsShuffled, testing);
	}

	private int[] shuffleDeck() {
		//uncomment if you wish to have a fixed order of events
		if (!testing) {
			int[] notShuffled = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13,
					14, 15 };
			return notShuffled;
		}
		int[] newOrder = new int[16];
		for (int i = 0; i < newOrder.length; i++) {
			Random r = new Random();
			int randomIndex = r.nextInt(integersTo16.size());
			newOrder[i] = integersTo16.get(randomIndex).intValue();
			// remove the number from the list, so it can't be used again
			integersTo16.remove(randomIndex);
		}
		integersTo16 = makeIntegerList();
		return newOrder;
	}

	public JPanel getTileEventPanelChance() {
		drawNextChanceCard();
		return currentEvent.getTileEventPanel();
	}

	public JPanel getTileEventPanelCommChest() {
		drawNextCommChestCard();
		return currentEvent.getTileEventPanel();
	}

	public void drawNextChanceCard() {;
		// if there are "no more cards left" in the deck, then reshuffle
	System.out.println("START: chanceDrawIndex: "+ chanceDrawIndex);
	
		if (chanceDrawIndex > 15)
			shuffleChanceCards();
		int cardToDraw = chanceEventsShuffled[chanceDrawIndex];
		System.out.println("cardToDraw: "+ cardToDraw);
		currentEvent = chanceEvents.get(cardToDraw);
		chanceDrawIndex++;
		System.out.println("END: chanceDrawIndex: "+ chanceDrawIndex);
	}

	public void drawNextCommChestCard() {
		// if there are no more cards left in the deck, then reshuffle
		if (commChestDrawIndex > 15)
			shuffleCommChestCards();
		int cardToDraw = commChestEventsShuffled[commChestDrawIndex];
		currentEvent = commChestEvents.get(cardToDraw);
		
		commChestDrawIndex++;
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
			chanceEvents.add(te);
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
			chanceEvents.add(te);
		}
		// GET JAIL CARD EVENT
		String name = res.getString("chance9-name");
		name = name.trim();
		String cardText = res.getString("chance9-cardText");
		name = name.trim();
		BoardEvent te = new GetJailCardEvent(name, cardText, gameClient);
		chanceEvents.add(te);

		// GET FIXED SUM EVENTS
		for (int i = 10; i < 16; i++) {
			name = res.getString("chance" + i + "-name");
			name = name.trim();
			cardText = res.getString("chance" + i + "-cardText");
			name = name.trim();
			String toParse = res.getString("chance" + i + "-fixedSum");
			int fixedSum = Integer.parseInt(toParse);
			te = new GetFixedSumEvent(name, cardText, fixedSum, gameClient);
			chanceEvents.add(te);
		}

	}

	private void createCommChestEvents() {
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
			te = new MovementEvent(name, cardText, newPosition, gameClient);
			commChestEvents.add(te);
		}

		// #CASH TRANSFER CARDS
		// commchest3-name=It is your birthday
		// commchest3-cardText=Collect $20 from each player
		// commchest3-transferAmount=20
		name = res.getString("commchest3-name");
		name = name.trim();
		cardText = res.getString("commchest3-cardText");
		name = name.trim();
		toParse = res.getString("commchest3-fixedSum");
		te = new BirthdayEvent(name, cardText, gameClient);
		commChestEvents.add(te);

		// GET JAIL CARD EVENT
		name = res.getString("commchest4-name");
		name = name.trim();
		cardText = res.getString("commchest4-cardText");
		name = name.trim();
		te = new GetJailCardEvent(name, cardText, gameClient);
		commChestEvents.add(te);

		// GET FIXED SUM EVENTS
		for (int i = 5; i < 16; i++) {
			name = res.getString("commchest" + i + "-name");
			name = name.trim();
			cardText = res.getString("commchest" + i + "-cardText");
			name = name.trim();
			toParse = res.getString("commchest" + i + "-fixedSum");
			int fixedSum = Integer.parseInt(toParse);
			te = new GetFixedSumEvent(name, cardText, fixedSum, gameClient);
			commChestEvents.add(te);
		}

	}

	// TODO Methods Needed to Test a Specific Event : COMMENT OUT FOR FINAL
	// PRODUCT
	public BoardEvent getCurrentEvent() {
		return currentEvent;
	}

	public void setCurrentEvent(BoardEvent currentEvent) {
		this.currentEvent = currentEvent;
	}

	public List<BoardEvent> getChanceEvents() {
		return chanceEvents;
	}

	public List<BoardEvent> getCommChestEvents() {
		return commChestEvents;
	}

	/**
	 * in the case of a reshuffle the next shuffled deck must be sent to all
	 * clients they are working from the same list
	 * 
	 * @param deck
	 */
	public void setChanceOrder(int[] shuffleOrder) {
		chanceEventsShuffled = shuffleOrder;
	}

	public void setCommChestOrder(int[] shuffleOrder) {
		commChestEventsShuffled = shuffleOrder;
	}

	private ArrayList<Integer> makeIntegerList() {
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < 16; i++) {
			list.add(new Integer(i));
		}
		return list;
	}

	// USED FOR TESTS
	public int getChanceEventsShuffledSize() {
		return chanceEventsShuffled.length;
	}

	public int getCommChestEventsShuffledSize() {
		return commChestEventsShuffled.length;
	}

	/**
	 * used for testing. If it is set to false, no net messages will be sent
	 * 
	 * 
	 * @param testing
	 *            the truth value to set to
	 */
	public void setupForTesting() {
		this.testing = false;

		for (BoardEvent be : chanceEvents) {
			AbstractTileEvent ate = (AbstractTileEvent) be;
			ate.setSendNetMessage(false);
		}
		for (BoardEvent be : commChestEvents) {
			AbstractTileEvent ate = (AbstractTileEvent) be;
			ate.setSendNetMessage(false);
		}
	}
}
