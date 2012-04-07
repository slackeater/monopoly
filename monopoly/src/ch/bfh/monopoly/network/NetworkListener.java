package ch.bfh.monopoly.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.channels.SocketChannel;

public class NetworkListener extends Thread{

	private Socket sock;
	int clientID;
	static public boolean connectionOpen = true;
	ObjectInputStream in;
	ObjectOutputStream out;

	public NetworkListener(Socket sock, int clientID){
		this.sock = sock;
		this.clientID = clientID;
	}


	@Override
	public void run() {
		System.out.println("THREAD FOR THE CLIENT " + clientID);
		

		while(connectionOpen && sock.isConnected()){
			try {
				out = new ObjectOutputStream(sock.getOutputStream());
				
				NetMessage m = new NetMessage();
				
				out.writeObject(m);
				out.flush();
				
				/*in = new ObjectInputStream(sock.getInputStream());
				
				NetMessage n = (NetMessage) in.readObject();
				System.out.println(n.toString());*/
				//in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
}
