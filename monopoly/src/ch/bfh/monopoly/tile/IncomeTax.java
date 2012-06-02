package ch.bfh.monopoly.tile;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.JPanel;

import ch.bfh.monopoly.common.GameClient;
import ch.bfh.monopoly.event.BoardEvent;
import ch.bfh.monopoly.event.EventManager;

public class IncomeTax extends AbstractTile {

	BoardEvent be;
	ResourceBundle rb = ResourceBundle.getBundle(
			"ch.bfh.monopoly.resources.tile", gameClient.getLoc());;
	String description;
	int fee;
	public IncomeTax(String name, int fee, int coordX, int coordY, int tileId,
			EventManager em, GameClient gameClient) {
		super(name, coordX, coordY, tileId, em, gameClient);
		this.description = rb.getString("incomeTax-cardText");
		this.fee=fee;
	}

	/**
	 * get the JPanel to show in the GUI for this tile's event
	 */
	public JPanel getTileEventPanel() {
		final int amount = Integer.parseInt(rb.getString("tile4-rent"));
		buttonLeft.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				gameClient.payIncome10Percent(sendNetMessage);
				buttonLeft.setEnabled(false);
				gameClient.sendTransactionSuccesToGUI(sendNetMessage);
			}
		});

		buttonRight.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				gameClient.payFee(amount, sendNetMessage);
				buttonRight.setEnabled(false);
				gameClient.sendTransactionSuccesToGUI(sendNetMessage);
			}
		});
		buttonLeft.setText("10%");
		buttonRight.setText(String.valueOf(amount));
		
		eventInfoLabel.setText(description);

		jpanel.add(eventInfoLabel);
		jpanel.add(buttonLeft);
		jpanel.add(buttonRight);

		return jpanel;
	}
}
