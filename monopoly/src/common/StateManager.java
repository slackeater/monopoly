package common;

import java.awt.Color;
import java.net.Socket;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JMenu;

public class StateManager {
	Player[] players;
	public Tile[] tiles;
	private Locale loc;
	private ResourceBundle rb;


	public StateManager() {
		//create tiles, cards, and events
		loc = new Locale("EN");
		tiles = new Tile[40];
		createTiles();	

	}
	
	public void createTiles(){

	//	int[] terrainPositions = {1,3,6,8,9,11,13,14,16,18,19,21,23,24,26,27,29,31,32,34,37,38,39};
		
		int[] terrainPositions = {1,3,6,8,9,11,13,14};
		
		for (int i =0 ; i < terrainPositions.length; i++){
			
			String tileName = ResourceBundle.getBundle("tile", loc).getString("tile"+i+"-name");
			String strTilePrice = ResourceBundle.getBundle("tile", loc).getString("tile"+i+"-price");
			int tilePrice = Integer.parseInt(strTilePrice);
			
			String strHouseCost = ResourceBundle.getBundle("tile", loc).getString("tile"+i+"-houseCost");
			int houseCost = Integer.parseInt(strHouseCost);
			
			String strHotelCost = ResourceBundle.getBundle("tile", loc).getString("tile"+i+"-hotelCost");
			int hotelCost = Integer.parseInt(strHotelCost);
			
			String strMortgageValue = ResourceBundle.getBundle("tile", loc).getString("tile"+i+"-mortgageValue");
			int mortgageValue = Integer.parseInt(strMortgageValue);
			
			Terrain t=  new Terrain(tileName, tilePrice, houseCost, hotelCost, mortgageValue);
			tiles[terrainPositions[i]]=t;
		}
	}
	
	

}
