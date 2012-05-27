package ch.bfh.monopoly.tile;


import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ch.bfh.monopoly.common.GameClient;
import ch.bfh.monopoly.event.EventManager;
import ch.bfh.monopoly.event.BoardEvent;

public abstract class AbstractTile implements Tile {

	protected int tileId;
	private int coordX;
	private int coordY;

	protected String name;
	protected BoardEvent event;
	protected EventManager em;
	protected boolean sendNetMessage = true;
	protected GameClient gameClient;
	protected JPanel jpanel = new JPanel();
	protected JButton buttonRight = new JButton();
	protected JButton buttonLeft = new JButton();
	protected JLabel eventInfoLabel = new JLabel();
	
	public AbstractTile(String name, int coordX, int coordY, int tileId,
			EventManager em, GameClient gameClient) {
		this.name = name;
		this.coordX = coordX;
		this.coordY = coordY;
		this.tileId = tileId;
		this.em = em;
		this.gameClient = gameClient;
	}

	/**
	 * Get the id of this tile
	 * 
	 * @return an int that correspond to the id
	 */
	public int getTileId() {
		return tileId;
	}

	/**
	 * Set the id for this tile
	 * 
	 * @param id
	 *            the id of this tile
	 */
	public void setId(int tileId) {
		this.tileId = tileId;
	}


	/**
	 * Get the name of this tile
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name of this tile
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * Get the X coordinate of this tile
	 * 
	 * @return an int representing the x coordindate
	 */
	public int getCoordX() {
		return coordX;
	}


	/**
	 * Get the Y coordinate of this tile
	 * 
	 * @return an int representing the y coordindate
	 */
	public int getCoordY() {
		return coordY;
	}

	// TODO Needed to test events, delete or comment out for final product
	public EventManager getEventManager() {
		return this.em;
	}

	
	/**
	 * used for testing. If it is set to false, no net messages will be sent
	 * 
	 * @param sendNetMessage the truth value to set to
	 */
	public void setSendNetMessage(boolean sendNetMessage) {
		this.sendNetMessage = sendNetMessage;
	}

}
