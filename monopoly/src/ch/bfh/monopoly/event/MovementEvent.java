package ch.bfh.monopoly.event;

import ch.bfh.monopoly.common.GameClient;
import ch.bfh.monopoly.common.Player;

public class MovementEvent extends AbstractTileEvent {

	int newPosition;
	
	public MovementEvent(String name, String eventDescription, int newPosition,
			GameClient gameClient) {
		super(name, eventDescription, gameClient);
		this.newPosition = newPosition;

	}

	@Override
	public String getEventDescription() {
		return eventDescription;
	}

	@Override
	public void performEvent() {
		Player currentPlayer = gameClient.getCurrentPlayer();
		if (newPosition > 0) {
			//set player's position to the new position
			currentPlayer.setPosition(newPosition);
		}
		else if (newPosition == 0 ){
			//go to jail
			currentPlayer.setPosition(currentPlayer.getPosition()-3);
			}
		else {
			//set player's position back three spaces
			currentPlayer.setPosition(currentPlayer.getPosition()-3);
		}
		
	}
}
