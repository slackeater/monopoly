package ch.bfh.monopoly.event;

public interface BoardEvent {
	public String getEventDescription();
	public void performEvent();
}
