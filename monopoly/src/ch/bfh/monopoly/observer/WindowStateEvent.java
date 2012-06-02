package ch.bfh.monopoly.observer;

public class WindowStateEvent {


	String eventDescription;
	int amount;
	private WindowMessage type;
	TradeInfoEvent tei;
	
	public WindowStateEvent(WindowMessage type, String eventDescription, int amount){
		this.type = type;
		this.eventDescription=eventDescription;
		this.amount=amount;
	}
	
	public WindowStateEvent(WindowMessage type, TradeInfoEvent tei){
		this.type = type;
		this.tei=tei;
	}
	
	public WindowStateEvent(WindowMessage type, boolean answer){
		this.type = type;
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
