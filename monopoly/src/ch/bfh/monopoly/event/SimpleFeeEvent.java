package ch.bfh.monopoly.event;

import ch.bfh.monopoly.common.GameClient;
import ch.bfh.monopoly.exception.TransactionException;

public class SimpleFeeEvent extends AbstractTileEvent {

	public SimpleFeeEvent(String name, GameClient gameClient) {
		super(
				name,
				"If you are not the owner of this tile, you must pay rent.  The rent for this tile is ",
				gameClient);
	}

	@Override
	public void performEvent() {
		String currentPlayer = gameClient.getCurrentPlayer().getName();
		int currentPos = gameClient.getCurrentPlayer().getPosition();
		if (!(gameClient.playerIsOwnerOfTile(currentPlayer, currentPos))) {
			int fee = gameClient.getFeeForTileAtId(currentPos);
			// TODO make a distinction between paying a player and playing a fee
			// into FREE PARKING
			try {
				gameClient.getCurrentPlayer().withdawMoney(fee);
			} catch (TransactionException e) {
				gameClient.sendTransactionErrorToGUI(e, true);
			}
		}
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
