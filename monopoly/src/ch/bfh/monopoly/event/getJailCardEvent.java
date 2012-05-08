package ch.bfh.monopoly.event;

import ch.bfh.monopoly.common.GameClient;

public class getJailCardEvent extends AbstractTileEvent{

	public getJailCardEvent(String name, String eventDescription,
			GameClient gameClient) {
		super(name, eventDescription, gameClient);
	}

	@Override
	public void performEvent() {
		int jailCardCount = gameClient.getCurrentPlayer().getJailCard();
		gameClient.getCurrentPlayer().setJailCard(jailCardCount++);
		
	}

}
