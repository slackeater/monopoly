package ch.bfh.monopoly.tile;

import java.util.Locale;
import java.util.ResourceBundle;

import ch.bfh.monopoly.common.GameClient;

public class TileCreator {

	
	public Tile[] tiles;
	private Locale loc;
	

	public TileCreator(Locale loc, GameClient gameClient) {
		// create tiles, cards, and events
		this.loc = loc;
		tiles = new Tile[40];
		System.out.println(loc.getLanguage());
		createProperties();
		createChanceCommChest(gameClient);
	}
	
	public Tile[] getTilesArray(){
		return tiles;
	}
	
	public void createProperties() {
		createTerrains();
		createRailroads();
		createUtilities();
		createCornersAndIncomeTax();
		//TODO: create GO, FREEPARKING, CHANCE, COMMUNITY CHEST, JAIL, LUXURY TAX
	}
	
	public void createChanceCommChest(GameClient gameClient){
		Chance c = new Chance("Chance",gameClient);
		
		//c.setCoordX(coordX);
		//c.setCoordY(coordY);
		tiles[7] = c;
		tiles[22] = c;
		tiles[36] = c;
		
		CommunityChest cc = new CommunityChest("Community Chest",gameClient);
		tiles[2] = cc;
		tiles[17] = cc;
		tiles[33] = cc;
	}
	
	public void createCornersAndIncomeTax(){
		NonProperty c = new NonProperty("Go");
		tiles[0] = c;
		 c = new NonProperty("Jail");
		tiles[10] = c;
		 c = new NonProperty("Free Parking");
		tiles[20] = c;
		 c = new NonProperty("Go to Jail");
		tiles[30] = c;
		 c = new NonProperty("Income Tax");
		tiles[38] = c;
	}
	
	public void createUtilities(){
		
		int[] utilityPositions = { 12,28};
		for (int i = 0; i < utilityPositions.length; i++) {
			
			int tilePosition = utilityPositions[i];
			
			String tileName = ResourceBundle.getBundle("tile", loc).getString(
					"tile" + tilePosition + "-name");

			String toParse = ResourceBundle.getBundle("tile", loc).getString(
					"utility-price");
			toParse = toParse.trim();
			int tilePrice = Integer.parseInt(toParse);
			
			toParse = ResourceBundle.getBundle("tile", loc).getString(
					"utility-mortgageValue");
			toParse = toParse.trim();
			int mortgageValue = Integer.parseInt(toParse);
			
			
			Utility r = new Utility(tileName, tilePrice, mortgageValue);
			tiles[utilityPositions[i]] = r;
		}
	}

	public void createRailroads(){
		int[] railroadPositions = { 5,15,25,35};
		for (int i = 0; i < railroadPositions.length; i++) {
			
			int railroadPosition = railroadPositions[i];
			
			String tileName = ResourceBundle.getBundle("tile", loc).getString(
					"tile" + railroadPosition + "-name");

			String toParse = ResourceBundle.getBundle("tile", loc).getString(
					"Railroad-price");
			toParse = toParse.trim();
			int tilePrice = Integer.parseInt(toParse);
			
			toParse = ResourceBundle.getBundle("tile", loc).getString(
					"Railroad-rent");
			toParse = toParse.trim();
			int tileRent = Integer.parseInt(toParse);
			
			toParse = ResourceBundle.getBundle("tile", loc).getString(
					"Railroad-mortgageValue");
			toParse = toParse.trim();
			int mortgageValue = Integer.parseInt(toParse);
			
			
			Railroad r = new Railroad(tileName, tilePrice, tileRent, mortgageValue);
			tiles[railroadPositions[i]] = r;
			
		}
	}

	public void createTerrains() {
		
				
		for (int i = 0; i < terrainPositions.length; i++) {
			
			int terrainPosition = terrainPositions[i];
			
			String tileName = ResourceBundle.getBundle("tile", loc).getString(
					"tile" + terrainPosition + "-name");

			String toParse = ResourceBundle.getBundle("tile", loc).getString(
					"tile" + terrainPosition + "-price");
			toParse = toParse.trim();
			int tilePrice = Integer.parseInt(toParse);

			toParse = ResourceBundle.getBundle("tile", loc).getString(
					"tile" + terrainPosition + "-rent");
			toParse = toParse.trim();
			int tileRent = Integer.parseInt(toParse);

			toParse = ResourceBundle.getBundle("tile", loc).getString(
					"tile" + terrainPosition + "-houseCost");
			toParse = toParse.trim();
			int houseCost = Integer.parseInt(toParse);

			toParse = ResourceBundle.getBundle("tile", loc).getString(
					"tile" + terrainPosition + "-hotelCost");
			toParse = toParse.trim();
			int hotelCost = Integer.parseInt(toParse);

			toParse = ResourceBundle.getBundle("tile", loc).getString(
					"tile" + terrainPosition + "-rent1house");
			toParse = toParse.trim();
			int rent1House = Integer.parseInt(toParse);

			toParse = ResourceBundle.getBundle("tile", loc).getString(
					"tile" + terrainPosition + "-rent2house");
			toParse = toParse.trim();
			int rent2House = Integer.parseInt(toParse);

			toParse = ResourceBundle.getBundle("tile", loc).getString(
					"tile" + terrainPosition + "-rent3house");
			toParse = toParse.trim();
			int rent3House = Integer.parseInt(toParse);

			toParse = ResourceBundle.getBundle("tile", loc).getString(
					"tile" + terrainPosition + "-rent4house");
			toParse = toParse.trim();
			int rent4House = Integer.parseInt(toParse);

			toParse = ResourceBundle.getBundle("tile", loc).getString(
					"tile" + terrainPosition + "-renthotel");
			toParse = toParse.trim();
			int rentHotel = Integer.parseInt(toParse);

			toParse = ResourceBundle.getBundle("tile", loc).getString(
					"tile" + terrainPosition + "-mortgageValue");
			toParse = toParse.trim();
			int mortgageValue = Integer.parseInt(toParse);
			
			toParse = ResourceBundle.getBundle("tile", loc).getString(
					"tile" + terrainPosition + "-coordX");
			toParse = toParse.trim();
			int coordX = Integer.parseInt(toParse);
			
			toParse = ResourceBundle.getBundle("tile", loc).getString(
					"tile" + terrainPosition + "-coordY");
			toParse = toParse.trim();
			int coordY = Integer.parseInt(toParse);

			Terrain t = new Terrain(tileName, tilePrice, tileRent, houseCost,
					hotelCost, mortgageValue, rent1House, rent2House,
					rent3House, rent4House, rentHotel, coordX, coordY);
			tiles[terrainPositions[i]] = t;
		}
	}
}
