package ch.bfh.monopoly.common;

public interface Subject {
	public void addListener(TileListener tl);
	public void removeListener(TileListener tl);
	public void notifyListeners();
}