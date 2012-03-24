package common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class NetworkThread extends Thread{

	private Socket sock;
	int clientID;
	static public boolean connectionOpen = true;
	private ObjectOutputStream out;
	private ObjectInputStream in;

	public NetworkThread(Socket sock, int clientID){
		this.sock = sock;
		this.clientID = clientID;
	}


	@Override
	public void run() {
		System.out.println("Server: the client is connected, its ID is " + this.clientID);
		System.out.println("We can receive data.");

		try {
			in = new ObjectInputStream(sock.getInputStream());
			out = new ObjectOutputStream(sock.getOutputStream());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		while(connectionOpen){

			try {
				sleep(15000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}



}
