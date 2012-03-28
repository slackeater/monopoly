package common;

public abstract class Property extends AbstractTile{

	private int price;
	private Player owner;
	
	//how are we keeping track of what group a property belongs to?
	private String group;

	
	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}
	
	
}
