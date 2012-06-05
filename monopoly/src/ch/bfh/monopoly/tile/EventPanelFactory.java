package ch.bfh.monopoly.tile;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import ch.bfh.monopoly.common.Monopoly;
import ch.bfh.monopoly.gui.MonopolyGUI;
import ch.bfh.monopoly.observer.PlayerListener;
import ch.bfh.monopoly.observer.PlayerStateEvent;
import ch.bfh.monopoly.observer.PlayerSubject;

public class EventPanelFactory  implements PlayerListener{
	JPanel master, buttonPanel;
	JTextPane label;
	EventPanelSource eps;
	EventPanelInfo epi;

	

//	public EventPanelFactory(EventPanelSource eps) {
//		master = createMasterPanel();
//		buttonPanel = new JPanel();
//		label = new JTextPane();
//
//		this.eps = eps;
//	}
	
	public EventPanelFactory(EventPanelSource eps, PlayerSubject ps) {
		master = createMasterPanel();
		buttonPanel = new JPanel();
		label = new JTextPane();
		// label.setWrapStyleWord(true);
		// label.setLineWrap(true);
		// label.setHorizontalAlignment( SwingConstants.CENTER );
		ps.addListener(this);
		this.eps = eps;
	}

	public JPanel createMasterPanel() {
		JPanel jpanel = new JPanel();
		jpanel.setSize(100, 100);
		jpanel.setLayout(new BorderLayout());
		// jpanel.setBackground(Color.WHITE);
		return jpanel;
	}

	public JPanel getJPanel() {
		return master;
	}

	public void changePanel(Step step) {
		JPanel newButtonPanel = new JPanel();

		epi = eps.getEventPanelInfoForStep(step);
		label.setText("\n\n"+epi.getText());

		// format the text
		SimpleAttributeSet bSet = new SimpleAttributeSet();
		StyleConstants.setAlignment(bSet, StyleConstants.ALIGN_CENTER);
		StyleConstants.setFontFamily(bSet, "lucida typewriter bold");
		StyleConstants.setFontSize(bSet, 24);
		StyledDocument doc = label.getStyledDocument();
		doc.setParagraphAttributes(0, doc.getLength(), bSet, false);

		System.out.println("label.getText(): " + label.getText());

		int buttonCount = epi.getButtonCount();
		for (int i = 0; i < buttonCount; i++) {
			JButton button =epi.getButtonAtIndex(i);
			newButtonPanel.add(button);
		}
		master.remove(buttonPanel);
		buttonPanel = newButtonPanel;
		master.add(label, BorderLayout.CENTER);
		master.add(buttonPanel, BorderLayout.SOUTH);
	}

	public void disableAfterClick() {
		int buttonCount = buttonPanel.getComponentCount();
		for (int i = 0; i < buttonCount; i++) {
			buttonPanel.getComponent(i).setEnabled(false);
		}
	}
	
	private JPanel imageLogo(String name) {
		JPanel img = new JPanel();
		java.net.URL urlImg = Monopoly.class
				.getResource("/ch/bfh/monopoly/resources/" + name);
		ImageIcon logo = new ImageIcon(urlImg);
		JLabel imgLab = new JLabel(logo);
		img.add(imgLab);
		return img;
	}

	
	@Override
	public void updatePlayer(ArrayList<PlayerStateEvent> playerStates) {
		System.out.println("EVENT PANEL FACTORY GOT OBSERVER SIGNAL");
		int currentPlayerAccount=0;
		int jailCardCount=0;
		for (PlayerStateEvent pse : playerStates){
			if (pse.hasTurnToken()){
				currentPlayerAccount = pse.getAccount();
				jailCardCount=pse.getJailCard();
			}
		}
		int buttonCount = epi.getButtonCount();
		for (int i = 0; i < buttonCount; i++) {
			if (epi.getButtonAtIndex(i).getAmount() > currentPlayerAccount)
				epi.getButtonAtIndex(i).setEnabled(false);
			else
				epi.getButtonAtIndex(i).setEnabled(true);
			//if the amount is -100 this signals the button is for using a jail card
			if (epi.getButtonAtIndex(i).getAmount()==-100){
				if (jailCardCount <1 )
					epi.getButtonAtIndex(i).setEnabled(false);
				else
					epi.getButtonAtIndex(i).setEnabled(true);
			}
				
		}
	}

}
