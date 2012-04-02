package ch.bfh.monopoly.networkJustin;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class NetworkClientThread extends Thread {

	private Socket socket;
	static public boolean connectionOpenClient = true;

	public NetworkClientThread(Socket client) {
		this.socket = client;
	}

	public void run() {

		// if (socket==null) throw new
		// RuntimeException("Socket creation failed");
		InputStream is = null;
		ObjectInputStream ois = null;

		try {

			is = socket.getInputStream();
			ois = new ObjectInputStream(is);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		while (true) {

			Object o;
			try {
				o = ois.readObject();
				System.out.println(o);
				System.out.println(((NetMessage) o).getMessageTime());

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}
		}

	}
}
