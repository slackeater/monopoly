package ch.bfh.monopoly.common;

public interface WindowSubject {

	public void addListener(WindowListener wl);

	public void removeListener(WindowListener wl);

	public void notifyListeners(String text);
}
