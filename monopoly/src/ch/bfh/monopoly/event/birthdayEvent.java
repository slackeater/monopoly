package ch.bfh.monopoly.event;

import ch.bfh.monopoly.common.GameClient;
import ch.bfh.monopoly.common.Player;

public class birthdayEvent extends AbstractTileEvent {

	public birthdayEvent(String name, String eventDescription,
			GameClient gameClient) {
		super(name, eventDescription, gameClient);
	}

	@Override
	public void performEvent() {
		int fee = 20;
		for (Player p : gameClient.getPlayers()) {
			if (p != gameClient.getCurrentPlayer()) {
				p.withdawMoney(fee);
				gameClient.getCurrentPlayer().depositMoney(fee);
			}
		}

	}
}
