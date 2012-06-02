package ch.bfh.monopoly.tile;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JPanel;

public class EventPanelFactory {

	public JPanel getJPanel(){
		JPanel jpanel = new JPanel();
		jpanel.setSize(100, 100);
		jpanel.setLayout(new GridLayout(2,1));
		jpanel.setBackground(Color.WHITE);
		return jpanel;
	}
}
