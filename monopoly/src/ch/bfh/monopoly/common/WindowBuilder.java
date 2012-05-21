package ch.bfh.monopoly.common;

import java.awt.event.ActionListener;
import java.util.ArrayList;

public class WindowBuilder {
	
	String name;
	String description;
	ArrayList<ActionListener> actionList;
	
	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public ArrayList<ActionListener> getActionList() {
		return actionList;
	}


	public WindowBuilder(String name, String description, ArrayList<ActionListener> actionList){
		this.name = name;
		this.description = description;
		this.actionList =actionList;
	}
}
