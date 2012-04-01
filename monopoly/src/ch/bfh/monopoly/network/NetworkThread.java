package ch.bfh.monopoly.network;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import ch.bfh.monopoly.network.NetMessage;

public class NetworkThread extends Thread{

	private Socket sock;
	int clientID;
	static public boolean connectionOpen = true;
	private ObjectOutputStream out;
	private ObjectInputStream in;

	private DataInputStream inTest;

	public NetworkThread(Socket sock, int clientID){
		this.sock = sock;
		this.clientID = clientID;
	}


	@Override
	public void run() {
		System.out.println("Server: the client is connected, its ID is " + this.clientID);
		System.out.println("We can receive data.");
		while(connectionOpen){
			try {

				inTest = new DataInputStream(sock.getInputStream());

				System.out.println(inTest.readUTF());
				//			in = new ObjectInputStream(sock.getInputStream());
				//out = new ObjectOutputStream(sock.getOutputStream());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}



			/*
			NetMessage n = new NetMessage();
			try {
				out.writeObject(n);
				out.flush();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			try {
				sleep(15000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		}
	}

	public void sendNetMessage(NetMessage n){
		try {
			out.writeObject(n);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



}
