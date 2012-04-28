package ch.bfh.monopoly.tile;

import ch.bfh.monopoly.common.GameClient;
import ch.bfh.monopoly.event.EventManager;


public class Chance extends AbstractTile {


	public Chance(String name, int coordX, int coordY, int id, GameClient gameClient, EventManager em) {
		super(name, coordX, coordY,id,em);
	}


	@Override
	public final String getEventDescription() {
		return em.getEventDescriptionById(id);
	}

	@Override
	public void performEvent() {
		em.performChanceEvent();
	}

}