package ch.bfh.monopoly.event;

import ch.bfh.monopoly.common.GameClient;

public class ChanceEvent extends AbstractTileEvent {

	public ChanceEvent(String name, String eventDescription, GameClient gameClient) {
		super(
				name,
				eventDescription,
				gameClient);
	}

	@Override
	public void performEvent() {
		gameClient.payRent(sendNetMessage);
		gameClient.sendTransactionSuccesToGUI(true);
	}

	@Override
	public String getEventDescription() {
		int tileId = gameClient.getCurrentPlayer().getPosition();
		int rent = gameClient.getFeeForTileAtId(tileId);
		String eventDescription = super.getEventDescription() + rent;
		if (!(gameClient.hasSufficientFunds(rent)))
			eventDescription = super.getEventDescription() + rent
					+ "\nBut you do not have sufficient fund to pay!";
		return eventDescription;
	}

}
