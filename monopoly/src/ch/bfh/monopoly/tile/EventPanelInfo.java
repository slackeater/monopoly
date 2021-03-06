package ch.bfh.monopoly.tile;

import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JButton;

import ch.bfh.monopoly.common.GameClient;
import ch.bfh.monopoly.common.Sounds;

public class EventPanelInfo {
	ArrayList<PButton> buttonList;
	String text;
	String currentPlayerName;
	GameClient gameClient;

	public class PButton extends JButton {

		private static final long serialVersionUID = 1L;
		private int amount;

		public PButton(String text, int amount, ActionListener al) {
			super();
			setText(text);
			addActionListener(al);
			this.amount = amount;
			addMouseListener(getMouseListener());
		}
		
		public MouseListener getMouseListener(){
			
			MouseListener ml = new MouseListener() {
				
				@Override
				public void mouseReleased(MouseEvent e) {
					gameClient.playSound(Sounds.MOUSE_UP);
					
				}
				
				@Override
				public void mousePressed(MouseEvent e) {
					gameClient.playSound(Sounds.MOUSE_DOWN);
					
				}
				
				@Override
				public void mouseExited(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mouseEntered(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mouseClicked(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
			};
			
			return ml;
		}
		
		

		public int getAmount() {
			return amount;
		}
	}

	public EventPanelInfo(GameClient gameClient) {
		this.buttonList = new ArrayList<PButton>();
		this.gameClient = gameClient;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public PButton getButtonAtIndex(int index) {
		return buttonList.get(index);
	}

	public int getButtonCount() {
		return buttonList.size();
	}

	public void addButton(String text, int amount, ActionListener al) {
		int account = gameClient.getCurrentPlayer().getAccount();
		int jailCards = gameClient.getCurrentPlayer().getJailCard();
		PButton button = new PButton(text, amount, al);
		System.out.println("addButton was called");
		System.out.println("amount: "+amount +" account: "+ account );
		if (amount > account){
			System.out.println("amount: "+amount +" account: "+ account +" SET ENABLE FALSE");
			button.setEnabled(false);}
		else
			button.setEnabled(true);
		// if the amount is -100 this signals the button is for using a jail
		// card
		if (button.getAmount() == -100) {
			if (jailCards < 1)
				button.setEnabled(false);
			else
				button.setEnabled(true);
		}
		buttonList.add(button);
	}
}
