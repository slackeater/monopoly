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
		createTiles(gameClient);
	}

	public Tile[] getTilesArray() {
		return tiles;
	}


	public void createTiles(GameClient gameClient) {
		Tile t =null;

		for (int i = 0; i < 40; i++) {

			String name = ResourceBundle.getBundle("tile", loc).getString(
					"tile" + i + "-name");
			name = name.trim();

			String toParse = ResourceBundle.getBundle("tile", loc).getString(
					"tile" + i + "-price");
			int price = parseResource(toParse);

			toParse = ResourceBundle.getBundle("tile", loc).getString(
					"tile" + i + "-houseCost");
			int houseCost = parseResource(toParse);

			toParse = ResourceBundle.getBundle("tile", loc).getString(
					"tile" + i + "-hotelCost");
			int hotelCost = parseResource(toParse);

			toParse = ResourceBundle.getBundle("tile", loc).getString(
					"tile" + i + "-rent");
			int rent = parseResource(toParse);

			toParse = ResourceBundle.getBundle("tile", loc).getString(
					"tile" + i + "-rent1house");
			int rent1house = parseResource(toParse);

			toParse = ResourceBundle.getBundle("tile", loc).getString(
					"tile" + i + "-rent2house");
			int rent2house = parseResource(toParse);

			toParse = ResourceBundle.getBundle("tile", loc).getString(
					"tile" + i + "-rent3house");
			int rent3house = parseResource(toParse);

			toParse = ResourceBundle.getBundle("tile", loc).getString(
					"tile" + i + "-rent4house");
			int rent4house = parseResource(toParse);

			toParse = ResourceBundle.getBundle("tile", loc).getString(
					"tile" + i + "-renthotel");
			int renthotel = parseResource(toParse);

			String group = ResourceBundle.getBundle("tile", loc).getString(
					"tile" + i + "-group");
			group = group.trim();

			toParse = ResourceBundle.getBundle("tile", loc).getString(
					"tile" + i + "-mortgageValue");
			int mortgageValue = parseResource(toParse);

			toParse = ResourceBundle.getBundle("tile", loc).getString(
					"tile" + i + "-coordX");
			int coordX = parseResource(toParse);

			toParse = ResourceBundle.getBundle("tile", loc).getString(
					"tile" + i + "-coordY");
			int coordY = parseResource(toParse);


			if (group.equals("railroad"))
				t = new Railroad(name, price, rent, group, mortgageValue, coordX,coordY,i);
			else if (group.equals("utility"))
				t = new Utility(name, price, group, mortgageValue, coordX, coordY, i);
			else if (group.equals("Chance"))
				t = new Chance(name,coordX, coordY, i, gameClient);
			else if (group.equals("Community Chest"))
				t = new CommunityChest(name, coordX, coordY, i, gameClient);
			else if (group.equals("cornersAndTax"))
				t = new NonProperty(name, coordX, coordY,i);
			else 
				t = new Terrain(name,  price, houseCost,  hotelCost,   rent,
						 rent1house,  rent2house, rent3house,  rent4house,  renthotel, group,  mortgageValue, coordX,  coordY,  i);
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
