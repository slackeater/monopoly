package ch.bfh.monopoly.tile;

import java.util.Locale;
import java.util.ResourceBundle;

import ch.bfh.monopoly.common.GameClient;
import ch.bfh.monopoly.event.EventManager;

public class TileCreator {

	public Tile[] tiles;
	private Locale loc;
	private EventManager em;
	
	public TileCreator(GameClient gameClient) {
		// create tiles, cards, and events
		this.loc = gameClient.getLoc();
		tiles = new Tile[40];
		em = new EventManager(gameClient);
		createTiles(gameClient);
	}

	public Tile[] getTilesArray() {
		return tiles;
	}


	public void createTiles(GameClient gameClient) {
		Tile t =null;
		ResourceBundle res = ResourceBundle.getBundle("tile", loc);
		
		for (int i = 0; i < 40; i++) {

			String name = res.getString(
					"tile" + i + "-name");
			name = name.trim();

			String toParse = res.getString(
					"tile" + i + "-price");
			int price = parseResource(toParse);

			toParse = res.getString(
					"tile" + i + "-houseCost");
			int houseCost = parseResource(toParse);

			toParse = res.getString(
					"tile" + i + "-hotelCost");
			int hotelCost = parseResource(toParse);

			toParse = res.getString(
					"tile" + i + "-rent");
			int rent = parseResource(toParse);

			toParse = res.getString(
					"tile" + i + "-rent1house");
			int rent1house = parseResource(toParse);

			toParse = res.getString(
					"tile" + i + "-rent2house");
			int rent2house = parseResource(toParse);

			toParse = res.getString(
					"tile" + i + "-rent3house");
			int rent3house = parseResource(toParse);

			toParse = res.getString(
					"tile" + i + "-rent4house");
			int rent4house = parseResource(toParse);

			toParse = res.getString(
					"tile" + i + "-renthotel");
			int renthotel = parseResource(toParse);

			String group = res.getString(
					"tile" + i + "-group");
			group = group.trim();

			toParse = res.getString(
					"tile" + i + "-mortgageValue");
			int mortgageValue = parseResource(toParse);

			toParse = res.getString(
					"tile" + i + "-coordX");
			int coordX = parseResource(toParse);

			toParse = res.getString(
					"tile" + i + "-coordY");
			int coordY = parseResource(toParse);
			
			String rgb = res.getString(
					"tile" + i + "-rgb");
			
			rgb = rgb.trim();
			
			if (group.equals("railroad"))
				t = new Railroad(name, price, rent, group, mortgageValue, coordX,coordY,i,em);
			else if (group.equals("utility"))
				t = new Utility(name, price, group, mortgageValue, coordX, coordY, i,em);
			else if (group.equals("Chance"))
				t = new Chance(name,coordX, coordY, i, gameClient,em);
			else if (group.equals("Community Chest"))
				t = new CommunityChest(name, coordX, coordY, i, gameClient,em);
			else if (group.equals("cornersAndTax"))
				t = new NonProperty(name, coordX, coordY,i,em);
			else 
				t = new Terrain(name,  price, houseCost,  hotelCost,   rent,
						 rent1house,  rent2house, rent3house,  rent4house,  renthotel, group,  mortgageValue, coordX,  coordY,  i, rgb,em);
			tiles[i] = t;
		}
	}
	
	public int parseResource(String str) {
		int value;
		str = str.trim();
		if (str.equals("NULL"))
			value = -1;
		else {
			value = Integer.parseInt(str);
		}
		return value;
	}
	
}
