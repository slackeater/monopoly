package ch.bfh.monopoly.tile;

import java.awt.event.ActionListener;
import java.util.ArrayList;

public class EventPanelInfo {

	String text;
	ArrayList<String> buttonText;
	ArrayList<ActionListener> buttonActions;
	
	public EventPanelInfo() {
		this.buttonText = new ArrayList<String>();
		this.buttonActions = new ArrayList<ActionListener>();
	}

	public void addActionListener(ActionListener al){
		buttonActions.add(al);
	}
	public void addButtonText(String text){
		buttonText.add(text);
	}
	
	public void setText(String text){
		this.text=text;
	}
	
	public String getText() {
		return text;
	}
	public String getButtonTextAtIndex(int index) {
		return buttonText.get(index);
	}
	public ActionListener getButtonActionAtIndex(int index) {
		return buttonActions.get(index);
	}
	public int getButtonCount(){
		return buttonActions.size();
	}
	
}
