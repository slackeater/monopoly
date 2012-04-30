package ch.bfh.monopoly.observer;

public interface TileSubject {
	public void addListener(TileListener tl);
	public void removeListener(TileListener tl);
	public void notifyListeners();
}