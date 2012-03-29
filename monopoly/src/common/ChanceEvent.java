package common;

import java.util.Locale;
import java.util.ResourceBundle;

public class ChanceEvent extends AbstractTileEvent{
	
	int fixedSum;
	Locale loc;
	
	public ChanceEvent(String name, int fixedSum, StateManager sm){
		super(sm);
		createChanceCards();
		this.fixedSum=fixedSum;
		this.name=name;
		loc = sm.getLoc();
		//constructor should create the chance events, all 20
	}
	
	public void run(){
		//select event from this events eventList
		//assure that all events in list are used before reusing an event
	}
	
	public void createChanceCards(){
		
		
		for (int i = 0; i < utilityPositions.length; i++) {
			
			int tilePosition = utilityPositions[i];
			
			String tileName = ResourceBundle.getBundle("tile", loc).getString(
					"tile" + tilePosition + "-name");

			String toParse = ResourceBundle.getBundle("tile", loc).getString(
					"utility-price");
			toParse = toParse.trim();
			int tilePrice = Integer.parseInt(toParse);
			
			toParse = ResourceBundle.getBundle("tile", loc).getString(
					"utility-mortgageValue");
			toParse = toParse.trim();
			int mortgageValue = Integer.parseInt(toParse);
			
			
			Utility r = new Utility(tileName, tilePrice, mortgageValue);
			tiles[utilityPositions[i]] = r;
		}
	}
	
/**
 * LIST HANDLING
 * create array of fixed length which is master list of all events
 * 
 * create an ArrayList populated with all events from MasterList, but 
 * events should be added in a random fashion
 * 
 * when this objects event class is called, pop an event from this list
 * when there are no more events, repopulate the list in the same fashion as before
 * 
 * */
}