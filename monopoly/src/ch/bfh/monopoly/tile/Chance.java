package ch.bfh.monopoly.tile;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import ch.bfh.monopoly.common.GameClient;
import ch.bfh.monopoly.common.WindowBuilder;
import ch.bfh.monopoly.event.EventManager;

public class Chance extends AbstractTile {

	public Chance(String name, int coordX, int coordY, int tileId,
			GameClient gameClient, EventManager em) {
		super(name, coordX, coordY, tileId, em);
	}

	/**
	 * get the window builder object needed for the GUI to display a window in
	 * response to landing on a tile
	 * 
	 * @param sendNetMessage
	 *            true if a net message should be sent to the server
	 */
	@Override
	public WindowBuilder getWindowBuilder() {
		return new WindowBuilder(name, getEventDescription(),
				getActionListenerList());

	}

	/**
	 * creates the actionListeners that the GUI should display in response to a
	 * player landing on this tile
	 * 
	 * @return a list of actionListeners for the GUI to add to buttons
	 */
	public List<ActionListener> getActionListenerList() {
		List<ActionListener> actionList = new ArrayList<ActionListener>();
		ActionListener al = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				performEvent();
			}
		};
		actionList.add(al);

		return actionList;
	}

	/**
	 * perform the action that this tile causes if a player lands on it
	 */
	@Override
	public void performEvent() {
		em.performEventChance();
	}

	/**
	 * get the text that should be displayed when a play lands on this tile
	 */
	@Override
	public String getEventDescription() {
		return em.getEventDescriptionChance();
	}

}