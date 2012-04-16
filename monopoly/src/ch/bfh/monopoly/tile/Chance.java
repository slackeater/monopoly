package ch.bfh.monopoly.tile;

import java.util.Locale;
import java.util.ResourceBundle;

import ch.bfh.monopoly.common.GameClient;
import ch.bfh.monopoly.common.MovementEvent;
import ch.bfh.monopoly.common.TileEvent;

public class Chance extends AbstractTile {

	int fixedSum;
	Locale loc;
	public TileEvent[] chanceCardDeck;
	//Board board;
	private GameClient gameClient; 


	public Chance(String name, int coordX, int coordY, int id, GameClient gameClient) {
		super(name, coordX, coordY,id);
		loc = gameClient.getLoc();
		chanceCardDeck = new TileEvent[16];
		createChanceCards();
		
		// constructor should create the chance events, all 20
	}

	public void run() {
		// select event from this events eventList
		// assure that all events in list are used before reusing an event
	}

	public void createChanceCards() {

		//create movement cards
		for (int i = 0; i < 7; i++) {

			String cardName = ResourceBundle.getBundle("chance", loc).getString(
					"card" + i + "-name");
			
			String cardText = ResourceBundle.getBundle("chance", loc).getString(
					"card" + i + "-cardText");

			String toParse = ResourceBundle.getBundle("chance", loc).getString(
					"card" + i + "-newPosition");
			toParse = toParse.trim();
			int newPosition = Integer.parseInt(toParse);

			
			MovementEvent evt = new MovementEvent(cardName, cardText, newPosition, gameClient);
			chanceCardDeck[i] = evt;
		}
	}

	/**
	 * LIST HANDLING create array of fixed length which is master list of all
	 * events
	 * 
	 * create an ArrayList populated with all events from MasterList, but events
	 * should be added in a random fashion
	 * 
	 * when this objects event class is called, pop an event from this list when
	 * there are no more events, repopulate the list in the same fashion as
	 * before
	 * 
	 * */
}