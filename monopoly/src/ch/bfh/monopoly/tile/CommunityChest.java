package ch.bfh.monopoly.tile;


import javax.swing.JPanel;

import ch.bfh.monopoly.common.GameClient;
import ch.bfh.monopoly.event.EventManager;

public class CommunityChest extends AbstractTile {

	public CommunityChest(String name, int coordX, int coordY, int tileId, GameClient gameClient, EventManager em) {
		super(name, coordX, coordY, tileId,em,gameClient);
	}


	@Override
	public JPanel getTileEventPanel() {
		
		return em.getTileEventPanelCommChest();
	}

}