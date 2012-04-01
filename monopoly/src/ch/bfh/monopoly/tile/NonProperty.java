package ch.bfh.monopoly.tile;


public class NonProperty extends AbstractTile {

	public NonProperty(String name, int coordX, int coordY, int id){
		super(name, coordX, coordY,id);
		this.name = name;
	}

	@Override
	public int getID() {
		return super.getId();
	}
}
