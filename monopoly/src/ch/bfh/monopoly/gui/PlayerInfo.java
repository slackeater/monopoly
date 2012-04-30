package ch.bfh.monopoly.gui;

import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ch.bfh.monopoly.common.Player;


/**
 * This class represent the JPanel with the information of the user
 * like balance, color and terrains, and jail card
 * @author snake
 *
 */
public class PlayerInfo extends JPanel{

	private Color c;
	private static final long serialVersionUID = 1312492036490100077L;
	private JLabel playerInfo;
	private Player p;
	JPanel terrainUp = new JPanel();
	JPanel terrainDown = new JPanel();
	
	/**
	 * Construct a PlayerInfo
	 * @param c the color of this player
	 */
	public PlayerInfo(Color c, Player p){
		this.c = c;
		this.p = p;
		
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		playerInfo.setText(p.getName() + "    " + p.getAccount());
		//TODO
	}

}
