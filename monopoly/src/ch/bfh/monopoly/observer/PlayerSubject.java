package ch.bfh.monopoly.observer;

public interface PlayerSubject {
	public void addListener(PlayerListener pl);
	public void removeListener(PlayerListener pl);
	public void notifyListeners();
}