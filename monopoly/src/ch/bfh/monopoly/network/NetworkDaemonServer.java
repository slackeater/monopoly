package ch.bfh.monopoly.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

public class NetworkDaemonServer extends Thread{

	private Socket sock;
	int clientID;
	static public boolean connectionOpen = true;
	ObjectInputStream in;
	ObjectOutputStream out;


	public NetworkDaemonServer(Socket sock, int clientID){
		this.sock = sock;
		this.clientID = clientID;
	}


	@Override
	public void run() {
		System.out.println("THREAD FOR THE CLIENT " + clientID);
		System.out.println("FROM PORT: " + sock.getPort());

		try {
			in = new ObjectInputStream(sock.getInputStream());
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		while(connectionOpen){
			try {
				NetMessage n = (NetMessage) in.readObject();
				System.out.println(n.getMessageCode());
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Something went wrong with the connection with the client");
				//e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
