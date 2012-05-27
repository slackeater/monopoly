package ch.bfh.monopoly.event;

import javax.swing.JPanel;

public interface BoardEvent {
	public String getEventDescription();
	public void performEvent();
	public JPanel getTileEventPanel();
}
