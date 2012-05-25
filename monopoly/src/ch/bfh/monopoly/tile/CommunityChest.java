package ch.bfh.monopoly.tile;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import ch.bfh.monopoly.common.GameClient;
import ch.bfh.monopoly.event.EventManager;

public class CommunityChest extends AbstractTile {

	public CommunityChest(String name, int coordX, int coordY, int tileId, GameClient gameClient, EventManager em) {
		super(name, coordX, coordY, tileId,em,gameClient);
	}


	@Override
	public JPanel getTileEventPanel() {
		// TODO Auto-generated method stub

		List<ActionListener> actionList = new ArrayList<ActionListener>();
		ActionListener al = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			}
		};
		actionList.add(al);
		return null;
	}

}