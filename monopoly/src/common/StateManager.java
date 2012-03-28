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

		int[] terrainPositions = {1,3,6,8,9,11,13,14,16,18,19,21,23,24,26,27,29,31,32,34,37,39};
		//int[] terrainPositions = {1,3,6,8,9,11,13,14,16,18,19,21,23,24,26,27};
				
		for (int i =0 ; i < terrainPositions.length; i++){
			int terrainPosition = terrainPositions[i];
			String tileName = ResourceBundle.getBundle("tile", loc).getString("tile"+terrainPosition+"-name");

			String strTilePrice = ResourceBundle.getBundle("tile", loc).getString("tile"+terrainPosition+"-price");
			strTilePrice = strTilePrice.trim();
			int tilePrice = Integer.parseInt(strTilePrice);
			
			String strTileRent = ResourceBundle.getBundle("tile", loc).getString("tile"+terrainPosition+"-price");
			strTileRent = strTileRent.trim();
			int tileRent = Integer.parseInt(strTileRent);
			
			String strHouseCost = ResourceBundle.getBundle("tile", loc).getString("tile"+terrainPosition+"-houseCost");
			strHouseCost = strHouseCost.trim();
			int houseCost = Integer.parseInt(strHouseCost);
			
			String strHotelCost = ResourceBundle.getBundle("tile", loc).getString("tile"+terrainPosition+"-hotelCost");
			strHotelCost = strHotelCost.trim();
			int hotelCost = Integer.parseInt(strHotelCost);
			
			String strMortgageValue = ResourceBundle.getBundle("tile", loc).getString("tile"+terrainPosition+"-mortgageValue");
			strMortgageValue = strMortgageValue.trim();
			int mortgageValue = Integer.parseInt(strMortgageValue);
			
			Terrain t=  new Terrain(tileName, tilePrice, tileRent, houseCost, hotelCost, mortgageValue);
			tiles[terrainPositions[i]]=t;
		}
	}
	
	

}
