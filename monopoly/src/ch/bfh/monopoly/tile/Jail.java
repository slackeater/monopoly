package ch.bfh.monopoly.tile;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.JPanel;

import ch.bfh.monopoly.common.GameClient;
import ch.bfh.monopoly.event.BoardEvent;
import ch.bfh.monopoly.event.EventManager;

public class Jail extends AbstractTile implements EventPanelSource {

	BoardEvent be;
	ResourceBundle rb = ResourceBundle.getBundle(
			"ch.bfh.monopoly.resources.tile", gameClient.getLoc());;
	String description;
	EventPanelFactory epf;

	public Jail(String name, int coordX, int coordY, int tileId,
			EventManager em, GameClient gameClient) {
		super(name, coordX, coordY, tileId, em, gameClient);
		this.description = rb.getString("jail-cardText");
		epf = new EventPanelFactory(this);
	}



	public EventPanelInfo getEventPanelInfoForStep(Step step) {
		String labelText;
		String buttonText;
		ActionListener al;
		EventPanelInfo epi;

		switch (step) {
		case GET_EVENT:
			epi = new EventPanelInfo();
			al = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					gameClient.sendTransactionSuccesToGUI(sendNetMessage);

					System.out.println("The current player is at tile :"
							+ gameClient.getCurrentPlayer().getPosition());
					System.out.println("The current player jail status is :"
							+ gameClient.getCurrentPlayer().isInJail());

				}
			};
			epi.setText(description);
			epi.addButtonText("ok");
			epi.addActionListener(al);
			break;

		default:
			epi = new EventPanelInfo();
			labelText = "No case defined";
			buttonText = "ok";
			al = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					gameClient.sendTransactionSuccesToGUI(sendNetMessage);
				}
			};
			epi.setText(labelText);
			epi.addActionListener(al);
			epi.addButtonText(buttonText);
			break;
		}
		return epi;
	}

	@Override
	public JPanel getTileEventPanel() {
		epf.changePanel(Step.GET_EVENT);
		return epf.getJPanel();
	}
}
