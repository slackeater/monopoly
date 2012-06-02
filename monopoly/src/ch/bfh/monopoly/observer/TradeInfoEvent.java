package ch.bfh.monopoly.observer;

import java.util.List;

public class TradeInfoEvent {

	int moneyDemand;
	int moneyOffer;
	int jailcardDemand;
	int jailcardOffer;
	List<String> propertiesDemand;
	List<String> propertiesOffer;
	
	
	public TradeInfoEvent(int moneyDemand, int moneyOffer, int jailcardDemand,
			int jailcardOffer, List<String> propertiesDemand,
			List<String> propertiesOffer) {
		super();
		this.moneyDemand = moneyDemand;
		this.moneyOffer = moneyOffer;
		this.jailcardDemand = jailcardDemand;
		this.jailcardOffer = jailcardOffer;
		this.propertiesDemand = propertiesDemand;
		this.propertiesOffer = propertiesOffer;
	}
	
	public int getMoneyDemand() {
		return moneyDemand;
	}
	public int getMoneyOffer() {
		return moneyOffer;
	}
	public int getJailcardDemand() {
		return jailcardDemand;
	}
	public int getJailcardOffer() {
		return jailcardOffer;
	}
	public List<String> getPropertiesDemand() {
		return propertiesDemand;
	}
	public List<String> getPropertiesOffer() {
		return propertiesOffer;
	}


	
	
}
