package ch.bfh.monopoly.event;

import ch.bfh.monopoly.common.GameClient;

public class GetVariableSumEvent extends AbstractTileEvent {

	int eventId;

	public GetVariableSumEvent(String name, String eventDescription,
			int eventId, GameClient gameClient) {
		super(name, eventDescription, gameClient);
		this.eventId = eventId;
	}

	@Override
	public void performEvent() {
		// gets position of current player to figure out which course of action
		// to take in the switch statement
		eventId = gameClient.getCurrentPlayer().getPosition();
		int fee;
		switch (eventId) {
		case 20: // FREE PARKING
			int amount = gameClient.getFreeParking();
			gameClient.getCurrentPlayer().depositMoney(amount);
			break;
		case 4: // INCOME TAX
			fee = gameClient.getCurrentPlayer().getAccount();
			fee /= 10;
			gameClient.getCurrentPlayer().withdawMoney(fee);
			break;
		default:
			break;
		}

	}

}
