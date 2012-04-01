package ch.bfh.monopoly.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;


public class NetworkClient extends Thread {

	
	private Socket socket;
	final int QUEUE_LENGTH = 20;
	final String SERVER_ADDR;
	final int PORT;
	private int msgCount;
	NetMessage[] messageQueue;
	String errorMessage;
	static public boolean connectionOpenClient = true;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	

	public NetworkClient(String ip, int port){
		this.SERVER_ADDR = ip;
		this.PORT = port;
		messageQueue = new NetMessage[5];
	}
	
	public boolean hasMessages(){
		return (msgCount > 0);
	}
	

	public void openConnection(){
		
		try {
			socket = new Socket(SERVER_ADDR, PORT);
		} catch (UnknownHostException e) {
			errorMessage= e.getMessage();
			System.out.println(errorMessage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			errorMessage= e.getMessage();
			System.out.println(errorMessage);
			
		}
		
	}
	
	public void run(){
		try {
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while (connectionOpenClient) {
			checkMessages();
		}

	}
	

	
	public void checkMessages(){
		try {
			Object o;
			if ( (o= in.readObject()) != null){
				((NetMessage)o).toString();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	public void sendMessage(NetMessage n){
		try {
			out.writeObject(n);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}


	
	
	
