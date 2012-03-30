package ch.bfh.monopoly.tests;

import static org.junit.Assert.*;


import org.junit.Test;

import ch.bfh.monopoly.network.NetworkClient;

public class NetClientTest {

//	@Test
//	public void testConnection() {
//		NetworkClient connection = new NetworkClient("192.168.1.8", 1234);
//		assertTrue(connection.openConnection());
//
//	}
	
	@Test
	public void testConnection2() {
		NetworkClient connection = new NetworkClient("192.168.1.8", 1234);
		connection.openConnection();
		connection.start();
		//
		// }
	}

	// @Test
	// public void clientConnects() {
	// GameClient gc = new GameClient();
	// String ip = "192.168.1.8";
	// int port = 1234;
	// assertTrue(gc.joinGame(ip, port));
	//
	//
	// try {
	// System.out.println(InetAddress.getLocalHost().toString());
	// } catch (UnknownHostException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	//
	// }

}
