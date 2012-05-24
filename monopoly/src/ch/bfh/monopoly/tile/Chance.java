package ch.bfh.monopoly.tile;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import ch.bfh.monopoly.common.GameClient;
import ch.bfh.monopoly.common.WindowBuilder;
import ch.bfh.monopoly.event.EventManager;

public class Chance extends AbstractTile {

	public Chance(String name, int coordX, int coordY, int tileId,
			GameClient gameClient, EventManager em) {
		super(name, coordX, coordY, tileId, em,gameClient);
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

			}
		};
		actionList.add(al);

		return actionList;
	}




	@Override
	public JPanel getTileEventPanel() {
		// TODO Auto-generated method stub
		return null;
	}

}