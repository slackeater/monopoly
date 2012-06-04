package ch.bfh.monopoly.tile;

import javax.swing.JPanel;

import ch.bfh.monopoly.common.GameClient;
import ch.bfh.monopoly.event.EventManager;

public class Chance extends AbstractTile {

	EventManager em;
	
	public Chance(String name, int coordX, int coordY, int tileId,
			GameClient gameClient, EventManager em) {
		super(name, coordX, coordY, tileId, em,gameClient);
		this.em = em;
	}



	@Override
	public JPanel getTileEventPanel() {
		
		return em.getTileEventPanelChance();
	}

	
}