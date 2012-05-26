package ch.bfh.monopoly.tile;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import ch.bfh.monopoly.common.GameClient;
import ch.bfh.monopoly.event.BoardEvent;
import ch.bfh.monopoly.event.EventManager;

public class NonProperty extends AbstractTile {

	BoardEvent be;

	public NonProperty(String name, int coordX, int coordY, int tileId,
			EventManager em, GameClient gameClient) {
		super(name, coordX, coordY, tileId, em, gameClient);
	}

	/**
	 * set the board event for this tile
	 * 
	 * @param be
	 *            the boardevent to use for this tile
	 */
	public void setEvent(BoardEvent be) {
		this.be = be;
	}

	/**
	 * get the JPanel to show in the GUI for this tile's event
	 */
	public JPanel getTileEventPanel() {

		buttonRight.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				gameClient.payRent(sendNetMessage);
				// buttonRight.setEnabled(false);
				eventInfoLabel.setText("ko");
				buttonRight.removeAll();
				buttonRight.setText("ko");
				buttonRight.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						jpanel.removeAll();

					}
				});
				System.out.println("The owner's bank account balance: ");
				System.out.println("The buyer's bank account balance: "
						+ gameClient.getCurrentPlayer().getAccount());
			}
		});
		buttonRight.setText("ko");
		eventInfoLabel.setText(name );

		jpanel.add(eventInfoLabel);
		jpanel.add(buttonRight);

		return jpanel;
	}

}
