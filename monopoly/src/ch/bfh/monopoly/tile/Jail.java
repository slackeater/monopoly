package ch.bfh.monopoly.tile;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.JPanel;

import ch.bfh.monopoly.common.GameClient;
import ch.bfh.monopoly.event.BoardEvent;
import ch.bfh.monopoly.event.EventManager;

public class Jail extends AbstractTile {

	BoardEvent be;
	ResourceBundle rb = ResourceBundle.getBundle(
			"ch.bfh.monopoly.resources.events", gameClient.getLoc());;
	String description;

	public Jail(String name, int coordX, int coordY, int tileId,
			EventManager em, GameClient gameClient) {
		super(name, coordX, coordY, tileId, em, gameClient);
		this.description = rb.getString("jail-cardText");
	}

	/**
	 * get the JPanel to show in the GUI for this tile's event
	 */
	public JPanel getTileEventPanel() {

		buttonRight.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				gameClient.sendTransactionSuccesToGUI(sendNetMessage);
				
				System.out.println("The current player is at tile :"
						+ gameClient.getCurrentPlayer().getPosition());
				System.out.println("The current player jail status is :"
						+ gameClient.getCurrentPlayer().isInJail());

			}
		});
		buttonRight.setText("ok");
		eventInfoLabel.setText(description);

		jpanel.add(eventInfoLabel);
		jpanel.add(buttonRight);

		return jpanel;
	}
}
