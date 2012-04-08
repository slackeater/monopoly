package ch.bfh.monopoly.network;

import java.io.Serializable;

public class NetMessage implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Messages m;
	

	public NetMessage(Messages m){
		this.m = m;
	}

	//only for test
	public int getMessageCode(){
		return m.getInt();
	}
	
	public Messages getMessageType(){
		return m;
	}
	
}
