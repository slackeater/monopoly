package ch.bfh.monopoly.tests;

import static org.junit.Assert.assertTrue;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ch.bfh.monopoly.network.Network;

public class NetServerTest {

	Network communicate ;
	
	@Before
	public void setup() {
		communicate = new Network();
		//communicate.stopServer();
	}
	
	@Test
	public void server(){
	

		System.out.println("Starting the server...");

		try {
			communicate.startServer("192.168.1.2", 1234, 2);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}



	@After
	public void cleanUp() {
		System.out.println("Stopping the server...");
		communicate.stopServer();

	}

}
