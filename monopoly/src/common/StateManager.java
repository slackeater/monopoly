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
				
		for (int i =0 ; i < terrainPositions.length; i++){
			int terrainPosition = terrainPositions[i];
			String tileName = ResourceBundle.getBundle("tile", loc).getString("tile"+terrainPosition+"-name");

			String toParse = ResourceBundle.getBundle("tile", loc).getString("tile"+terrainPosition+"-price");
			toParse = toParse.trim();
			int tilePrice = Integer.parseInt(toParse);
			
			toParse = ResourceBundle.getBundle("tile", loc).getString("tile"+terrainPosition+"-rent");
			toParse = toParse.trim();
			int tileRent = Integer.parseInt(toParse);
			
			toParse = ResourceBundle.getBundle("tile", loc).getString("tile"+terrainPosition+"-houseCost");
			toParse = toParse.trim();
			int houseCost = Integer.parseInt(toParse);
			
			toParse = ResourceBundle.getBundle("tile", loc).getString("tile"+terrainPosition+"-hotelCost");
			toParse = toParse.trim();
			int hotelCost = Integer.parseInt(toParse);
			
			toParse = ResourceBundle.getBundle("tile", loc).getString("tile"+terrainPosition+"-rent1house");
			toParse = toParse.trim();
			int rent1House = Integer.parseInt(toParse);
			
			toParse = ResourceBundle.getBundle("tile", loc).getString("tile"+terrainPosition+"-rent2house");
			toParse = toParse.trim();
			int rent2House = Integer.parseInt(toParse);
			
			toParse = ResourceBundle.getBundle("tile", loc).getString("tile"+terrainPosition+"-rent3house");
			toParse = toParse.trim();
			int rent3House = Integer.parseInt(toParse);
			
			toParse = ResourceBundle.getBundle("tile", loc).getString("tile"+terrainPosition+"-rent4house");
			toParse = toParse.trim();
			int rent4House = Integer.parseInt(toParse);
			
			toParse = ResourceBundle.getBundle("tile", loc).getString("tile"+terrainPosition+"-renthotel");
			toParse = toParse.trim();
			int rentHotel = Integer.parseInt(toParse);
			
			toParse = ResourceBundle.getBundle("tile", loc).getString("tile"+terrainPosition+"-mortgageValue");
			toParse = toParse.trim();
			int mortgageValue = Integer.parseInt(toParse);
			
			
			Terrain t=  new Terrain(tileName, tilePrice, tileRent, houseCost, hotelCost, mortgageValue, rent1House, rent2House, rent3House, rent4House, rentHotel);
			tiles[terrainPositions[i]]=t;
		}
	}
	
	

}
