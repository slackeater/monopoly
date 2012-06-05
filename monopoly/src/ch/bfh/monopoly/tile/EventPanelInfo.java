package ch.bfh.monopoly.tile;

import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;

public class EventPanelInfo {
	ArrayList<PButton> buttonList; 
	String text;
	String currentPlayerName;
	
	public class PButton extends JButton{

		private static final long serialVersionUID = 1L;
		private int amount;
		
		public PButton(String text, int amount, ActionListener al) {
			super();
			setText(text);
			addActionListener(al);
			this.amount = amount;
		}
		public int getAmount() {
			return amount;
		}
	}
	public EventPanelInfo(String currentPlayerName) {
		this.buttonList = new ArrayList<PButton>();
		this.currentPlayerName=currentPlayerName;
	}

	public void setText(String text){
		this.text=text;
	}
	
	public String getText() {
		return text;
	}

	public PButton getButtonAtIndex(int index) {
		return buttonList.get(index);
	}
	public int getButtonCount(){
		return buttonList.size();
	}
	
	public void addButton(String text, int amount, ActionListener al){
		PButton button = new PButton(text, amount, al);
		buttonList.add(button);
	}
}
