package ch.bfh.monopoly.event;

import ch.bfh.monopoly.common.GameClient;

public class GetFixedSumEvent extends AbstractTileEvent {

	int fixedSum;
	public GetFixedSumEvent(String name, String eventDescription, int fixedSum,
			GameClient gameClient) {
		super(name, eventDescription, gameClient);
		this.fixedSum=fixedSum;
		
	}

	@Override
	public void performEvent() {
		gameClient.getCurrentPlayer().depositMoney(fixedSum);
		
	}

}
