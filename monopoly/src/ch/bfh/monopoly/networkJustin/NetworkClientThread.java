package ch.bfh.monopoly.networkJustin;

import java.io.EOFException;
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
			try {
				Object o;
				int toRead = ois.available();
				for (int i = 0; i < toRead; i++) {
					o = ois.readByte();
				}
			} catch (EOFException e) {
				System.out.println(o);
				System.out.println(((NetMessage) o).getMessageTime());
			}

		}

	}
}
