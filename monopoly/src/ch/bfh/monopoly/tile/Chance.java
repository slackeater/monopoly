package ch.bfh.monopoly.tile;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import ch.bfh.monopoly.common.GameClient;
import ch.bfh.monopoly.event.EventManager;

public class Chance extends AbstractTile {

	public Chance(String name, int coordX, int coordY, int tileId,
			GameClient gameClient, EventManager em) {
		super(name, coordX, coordY, tileId, em,gameClient);
	}



	@Override
	public JPanel getTileEventPanel() {
		//make the JPanel here
		//the events have a default method in the superclass that will give a button performAction
		//the events that have a special event override the method to return a list with two buttons 
		em.getEventDescriptionChance();
		// TODO Auto-generated method stub
		return null;
	}

}