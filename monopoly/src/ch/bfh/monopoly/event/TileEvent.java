package ch.bfh.monopoly.event;

public interface TileEvent {
	public String getEventDescription();
	public void performEvent();
}
