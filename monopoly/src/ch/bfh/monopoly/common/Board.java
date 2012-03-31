package ch.bfh.monopoly.common;

import java.awt.Color;
import java.net.Socket;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JMenu;

import ch.bfh.monopoly.tile.Tile;
import ch.bfh.monopoly.tile.TileCreator;

public class Board {
	Player[] players;
	public Tile[] tiles;
	private Locale loc;

	private Player me;
	private Player currentPlayer;

	public Board(Locale loc, GameClient gameClient) {
		// create tiles, cards, and events
		this.loc = loc;

		TileCreator tc = new TileCreator(loc, gameClient);
		tiles = tc.getTilesArray();
	}
}
