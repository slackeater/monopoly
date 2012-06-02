package ch.bfh.monopoly.tests;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import ch.bfh.monopoly.common.ClientNetworkController;
import ch.bfh.monopoly.common.GameClient;
import ch.bfh.monopoly.common.ServerNetworkController;


public class PlayerQuitGame {

	static final String ip = "127.0.0.1";
	static final int port = 1234;
	ClientNetworkController cliCtrl;
	ClientNetworkController cliCtrl2;
	ClientNetworkController cliCtrl3;
	ClientNetworkController cliCtrl4;
	

	@Before 
	public void setUp() throws IOException, InterruptedException {
		ServerNetworkController  srvCtrl = new ServerNetworkController();
		srvCtrl.startServer(ip, port);

		cliCtrl = new ClientNetworkController(new GameClient(), "snake", 10);
		cliCtrl.startClient(ip, port);

		Thread.sleep(500);

		cliCtrl2 = new ClientNetworkController(new GameClient(), "jimmy", 12);
		cliCtrl2.startClient(ip, port);

		Thread.sleep(500);

		cliCtrl3 = new ClientNetworkController(new GameClient(), "jacob", 8);
		cliCtrl3.startClient(ip, port);

		Thread.sleep(500);

		cliCtrl4 = new ClientNetworkController(new GameClient(), "mike", 3);
		cliCtrl4.startClient(ip, port);
	}



	@Test 
	public void playerQuit() throws InterruptedException {
		
		Thread.sleep(5000);
		
		cliCtrl2.closeConnection();
	}

}
