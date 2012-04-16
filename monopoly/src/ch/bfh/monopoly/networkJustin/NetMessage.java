package ch.bfh.monopoly.networkJustin;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class NetMessage implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	int methodCode;
	String someMessage;
	String msgTime;
	
	public NetMessage(int methodCode){
		this.methodCode = methodCode;
		setMessageTime();
	}
	
	public void setMessageTime(){
		DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dfm.setTimeZone(TimeZone.getTimeZone("Europe/Zurich"));
		msgTime = dfm.format(new Date());
	}

	public String getMessageTime(){
		return msgTime;
	}
	public String toString(){
		return "I am a NetMessage, sent from above";

	}
}
