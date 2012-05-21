package ch.bfh.monopoly.common;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class WindowBuilder {
	
	String name;
	String description;
	List<ActionListener> actionList;
	
	
	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public List<ActionListener> getActionList() {
		return actionList;
	}


	public WindowBuilder(String name, String description, List<ActionListener> actionList){
		this.name = name;
		this.description = description;
		this.actionList =actionList;
	}
}
