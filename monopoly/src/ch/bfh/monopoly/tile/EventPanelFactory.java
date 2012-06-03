package ch.bfh.monopoly.tile;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class EventPanelFactory {
	JPanel master, buttonPanel;
	JLabel label;
	EventPanelSource eps;
	
	public EventPanelFactory() {
	
	}
	public JPanel getOldJPanel(){
		JPanel jpanel = new JPanel();
		jpanel.setSize(100, 100);
		jpanel.setLayout(new BorderLayout());
		return jpanel;
		
	}

	public EventPanelFactory(EventPanelSource eps) {
		master = createMasterPanel();
		buttonPanel=new JPanel();
		label=new JLabel();
		this.eps=eps;
	}

	public JPanel createMasterPanel(){
		JPanel jpanel = new JPanel();
		jpanel.setSize(100, 100);
		jpanel.setLayout(new BorderLayout());
//		jpanel.setBackground(Color.WHITE);
		return jpanel;
	}
	
	
	public JPanel getJPanel() {
		return master;
	}
	
	public void changePanel(Step step){
		JPanel newButtonPanel = new JPanel();;
		EventPanelInfo epi = eps.getEventPanelInfoForStep(step);
		label.setText(epi.getText());
		
		System.out.println("label.getText(): " +label.getText());

		
		int buttonCount = epi.getButtonCount();
		for (int i=0;i<buttonCount;i++){
			System.out.println("TURN"+i);
			JButton button = new JButton();
			button.setText(epi.getButtonTextAtIndex(i));
			button.addActionListener(epi.getButtonActionAtIndex(i));
			newButtonPanel.add(button);
		}
		master.remove(buttonPanel);
		buttonPanel=newButtonPanel;
		master.add(label,BorderLayout.CENTER);
		master.add(buttonPanel,BorderLayout.SOUTH);
	}
	
	public void disableAfterClick(){
		int buttonCount = buttonPanel.getComponentCount();
		for (int i =0;i<buttonCount;i++){
			buttonPanel.getComponent(i).setEnabled(false);
		}
	}
	
	
	
}
