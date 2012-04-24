package ch.bfh.monopoly.tile;

import ch.bfh.monopoly.event.EventManager;


public class NonProperty extends AbstractTile {

	public NonProperty(String name, int coordX, int coordY, int id, EventManager em){
		super(name, coordX, coordY,id,em);
		this.name = name;
	}
	
	@Override
	public final String getEventDescription() {
		return em.getEventDescriptionById(id);
	}

	@Override
	public void performEvent() {
		em.performEventForTileAtId(id);
	}

}
