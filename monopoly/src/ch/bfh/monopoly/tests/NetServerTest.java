package ch.bfh.monopoly.tests;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ch.bfh.monopoly.common.NetworkController;
import ch.bfh.monopoly.network.Network;

public class NetServerTest {

	NetworkController nc = new NetworkController();
	
	@Before
	public void setup() {
	
	}
	
	@Test
	public void startServer(){
		
		String ip = "192.168.1.2";
		int port = 1234;
		int players = 2;
		
		System.out.println("Starting the server...");

		try {
			nc.startServer(ip,port,players);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@Test
	public void stopServer(){
		
	}
	
	

	@After
	public void cleanUp() {
		System.out.println("Stopping the server...");
		nc.stopServer();
	}

}
