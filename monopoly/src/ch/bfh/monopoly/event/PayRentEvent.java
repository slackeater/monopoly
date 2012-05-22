package ch.bfh.monopoly.event;

import ch.bfh.monopoly.common.GameClient;
import ch.bfh.monopoly.exception.TransactionException;

public class PayRentEvent extends AbstractTileEvent {

	public PayRentEvent(String name, GameClient gameClient) {
		super(
				name,
				"If you are not the owner of this tile, you must pay rent.  The rent for this tile is ",
				gameClient);
	}

	@Override
	public void performEvent() {
		
		gameClient.payRent(true);
		
		TransactionException te = new TransactionException(
				"Player has completed the event successfully");
		gameClient.sendTransactionSuccesToGUI(te, true);

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
