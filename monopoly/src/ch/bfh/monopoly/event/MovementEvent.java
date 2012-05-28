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
			gameClient.advancePlayerToTile(newPosition, false);
		}
		else if (newPosition == 0 ){
			gameClient.goToJail(false);
			}
		else {
			gameClient.advancePlayerToTile(newPosition, sendNetMessage);
		}
		
	}
}
