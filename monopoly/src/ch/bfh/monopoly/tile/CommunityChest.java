package ch.bfh.monopoly.tile;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import ch.bfh.monopoly.common.GameClient;
import ch.bfh.monopoly.common.WindowBuilder;
import ch.bfh.monopoly.event.EventManager;

public class CommunityChest extends AbstractTile {

	public CommunityChest(String name, int coordX, int coordY, int tileId, GameClient gameClient, EventManager em) {
		super(name, coordX, coordY, tileId,em,gameClient);
	}

	

	/**
	 * perform the action that this tile causes if a player lands on it
	 */
	@Override
	public void performEvent() {
		em.performEventCommChest();
	}

	/**
	 * get the text that should be displayed when a play lands on this tile
	 */
	@Override
	public String getEventDescription() {
		return em.getEventDescriptionCommChest();
	}

	@Override
	public JPanel getTileEventPanel() {
		// TODO Auto-generated method stub

		List<ActionListener> actionList = new ArrayList<ActionListener>();
		ActionListener al = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				performEvent();
			}
		};
		actionList.add(al);
		return null;
	}

}