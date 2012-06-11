package ch.bfh.monopoly.observer;

public class WindowStateEvent {


	String eventDescription;
	int amount;
	private WindowMessage type;
	TradeInfoEvent tei;
	boolean answer;
	String player;
	String playerToKick;
	
	public WindowStateEvent(WindowMessage type, String eventDescription, int amount){
		this.type = type;
		this.eventDescription=eventDescription;
		this.amount=amount;
	}
	
	public WindowStateEvent(WindowMessage type, TradeInfoEvent tei){
		this.type = type;
		this.tei=tei;
	}
	
	public WindowStateEvent(WindowMessage type, String player, String playerToKick){
		this.type = type;
		this.player=player;
		this.playerToKick=playerToKick;
	}
	
	public WindowStateEvent(WindowMessage type, String player, boolean answer){
		this.type = type;
		this.player=player;
		this.answer = answer;
	}
	
	public WindowStateEvent(WindowMessage type, boolean answer){
		this.type = type;
		this.answer = answer;
	}
	
	public boolean getAnswer(){
		return  answer;
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

	public TradeInfoEvent getTei() {
		return tei;
	}
	

	
}
