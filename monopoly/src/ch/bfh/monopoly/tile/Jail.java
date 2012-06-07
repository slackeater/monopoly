package ch.bfh.monopoly.tile;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.JPanel;

import ch.bfh.monopoly.common.GameClient;
import ch.bfh.monopoly.event.BoardEvent;
import ch.bfh.monopoly.event.EventManager;
import ch.bfh.monopoly.gui.EventPanelFactory;

public class Jail extends AbstractTile implements EventPanelSource {

	BoardEvent be;
	ResourceBundle rb = ResourceBundle.getBundle(
			"ch.bfh.monopoly.resources.tile", gameClient.getLoc());;
	String description, description2;
	EventPanelFactory epf;

	public Jail(String name, int coordX, int coordY, int tileId,
			EventManager em, GameClient gameClient) {
		super(name, coordX, coordY, tileId, em, gameClient);
		this.description = rb.getString("jail-cardText");
		this.description2 = rb.getString("jail-cardText2");
	}

	public EventPanelInfo getEventPanelInfoForStep(Step step) {
		String labelText;
		String buttonText;
		ActionListener al;
		EventPanelInfo epi;

		switch (step) {
		case GET_EVENT:
			epi = new EventPanelInfo(gameClient);
			buttonText = "ok";
			al = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {

					if (gameClient.isDoublesRoll()) {
						epf.setEventPanelSource(gameClient.getDice());
						epf.changePanel(Step.DOUBLES_TRANSITION);
					} else {

						gameClient.sendTransactionSuccesToGUI(sendNetMessage);

						System.out.println("The current player is at tile :"
								+ gameClient.getCurrentPlayer().getPosition());
						System.out
								.println("The current player jail status is :"
										+ gameClient.getCurrentPlayer()
												.isInJail());
					}
				}
			};
			if (gameClient.getCurrentPlayer().isInJail()){
				System.err.println("currentPlayer jail status true");
				description=rb.getString("wentToJailArrivalMessage");
				}
			epi.setText(description);
			epi.addButton(buttonText, 0, al);
			break;

		default:
			epi = gameClient.getEventPanelInfoFromDice(step);
			break;
		}
		return epi;
	}

	@Override
	public JPanel getTileEventPanel() {
		epf = new EventPanelFactory(this, gameClient.getSubjectForPlayer());
		epf.changePanel(Step.GET_EVENT);
		return epf.getJPanel();
	}
}
