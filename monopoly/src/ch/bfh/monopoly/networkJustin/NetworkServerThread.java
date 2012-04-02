package ch.bfh.monopoly.networkJustin;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class NetworkServerThread extends Thread {

	private Socket sock;
	int clientID;
	static public boolean connectionOpen = true;
	private ObjectOutputStream oos;
	private OutputStream os;

	public NetworkServerThread(Socket sock, int clientID) {
		this.sock = sock;
		this.clientID = clientID;
		
		createInputStream();
	}

	public void createInputStream(){
		try {
			os = sock.getOutputStream();
			oos = new ObjectOutputStream(os);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void run() {

		if (oos==null) throw new RuntimeException("oos is null");

		while (true) {
			
			try {

				oos.writeObject(new NetMessage(1));
				oos.flush();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("looping");
		}

		// this.interrupt();

	}

	public boolean connectionIsAlive() {
		return sock.isConnected();
	}

	public void closeConnectionEndThead() {
		try {
			oos.close();
			os.close();
			sock.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
