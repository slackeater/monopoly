package ch.bfh.monopoly.observer;

public interface WindowSubject {

	public void addListener(WindowListener wl);

	public void removeListener(WindowListener wl);

	public void notifyListeners(String text);
}
