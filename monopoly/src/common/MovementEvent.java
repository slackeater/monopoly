package common;

public class MovementEvent extends AbstractTileEvent {

	int newPosition;

	public MovementEvent(String name, String cardText, int newPosition,
			GameClient gameClient) {
		super(gameClient);
		this.newPosition = newPosition;
		this.name = name;
		this.cardText = cardText;
	}

	public void run() {
		// How do you send text to GUI???
		
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

	public int getNewPosition() {
		return newPosition;
	}

	public void setNewPosition(int newPosition) {
		this.newPosition = newPosition;
	}
}
