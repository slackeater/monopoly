package ch.bfh.monopoly.tile;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.JPanel;

import ch.bfh.monopoly.common.GameClient;
import ch.bfh.monopoly.event.BoardEvent;
import ch.bfh.monopoly.event.EventManager;
import ch.bfh.monopoly.gui.EventPanelFactory;

public class LuxuryTax extends AbstractTile implements EventPanelSource {

	BoardEvent be;
	ResourceBundle rb = ResourceBundle.getBundle(
			"ch.bfh.monopoly.resources.tile", gameClient.getLoc());;
	String description;
	int fee;
	EventPanelFactory epf;

	public LuxuryTax(String name, int fee, int coordX, int coordY, int tileId,
			EventManager em, GameClient gameClient) {
		super(name, coordX, coordY, tileId, em, gameClient);
		this.description = rb.getString("luxuryTax-cardText");
		this.fee = Integer.parseInt(rb.getString("tile38-price"));
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
					gameClient.payFee(fee, sendNetMessage);
					if (gameClient.isDoublesRoll()) {
						epf.disableAfterClick();
						epf.setEventPanelSource(gameClient.getDice());
						epf.changePanel(Step.DOUBLES_TRANSITION);
					} else {
						epf.disableAfterClick();
						gameClient.sendTransactionSuccesToGUI(sendNetMessage);
					}
				}
			};
			epi.setText(description);
			epi.addButton(buttonText, fee, al);
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
