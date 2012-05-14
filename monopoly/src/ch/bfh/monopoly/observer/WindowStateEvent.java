package ch.bfh.monopoly.observer;

public class WindowStateEvent {


	String eventDescription;
	int amount;
	private WindowMessage type;
	
	
	public WindowStateEvent(WindowMessage type, String eventDescription, int amount){
		this.type = type;
		this.eventDescription=eventDescription;
		this.amount=amount;
	}


	public String getEventDescription() {
		return eventDescription;
	}


	public int getAmount() {
		return amount;
	}


	public WindowMessage getType() {
		return type;
	}
}
