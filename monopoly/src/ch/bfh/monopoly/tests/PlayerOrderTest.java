package ch.bfh.monopoly.tests;


import static org.junit.Assert.*;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.mina.core.session.IoSession;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ch.bfh.monopoly.common.GameClient;
import ch.bfh.monopoly.common.NetworkController;

public class PlayerOrderTest {

	private NetworkController nc;
	private String srvIp;
	private int srvPort;
	private GameClient gc;
	private List<String> mockUsername;


	@Before
	public void setUp() throws Exception {
		nc = new NetworkController();
		gc = new GameClient();
		srvIp = "127.0.0.1";
		srvPort = 1234;

		mockUsername = new ArrayList<String>();
		mockUsername.add("billiejoe");
		mockUsername.add("mike");
		mockUsername.add("trecool");
		mockUsername.add("green");
		mockUsername.add("busi");
		mockUsername.add("jan");
		mockUsername.add("biber");
		mockUsername.add("peter");
	}

	@Test
	public void playerOrderTwo(){
		try{
			Thread.sleep(5000);
			
			int playerNum = 2;
			List<String> serverUsername;
			IoSession c1, c2;

			nc.startServer(srvIp, srvPort);

			c2 = nc.startClient(srvIp, srvPort, gc, "mike", 4);
			Thread.sleep(1000);
			c1 = nc.startClient(srvIp, srvPort, gc, "billiejoe", 5);
			Thread.sleep(1000);
	
			while(true){
				if(nc.getServerOpenedSession() == playerNum){
					serverUsername = nc.getServerUsernames();
					break;
				}
			}

			for(int j = 0 ; j < playerNum ; j++){
				assertEquals(mockUsername.get(j), serverUsername.get(j));
			}


			Thread.sleep(1500);


			c1.close(true);
			c2.close(true);

			Thread.sleep(1500);

			nc.closeServer();
		} catch (Exception e){
			System.out.println(e.getMessage());
		}
	}

	@Test
	public void playerOrderThree(){
		try{
			Thread.sleep(5000);
			
			int playerNum = 3;
			List<String> serverUsername;
			IoSession c1, c2, c3;

			nc.startServer(srvIp, srvPort);

			c1 = nc.startClient(srvIp, srvPort, gc, "mike", 9);
			Thread.sleep(1000);
			c2 = nc.startClient(srvIp, srvPort, gc, "trecool", 7);
			Thread.sleep(1000);
			c3 = nc.startClient(srvIp, srvPort, gc, "billiejoe", 12);
			Thread.sleep(1000);

			while(true){
				if(nc.getServerOpenedSession() == playerNum){
					serverUsername = nc.getServerUsernames();
					break;
				}
			}

			for(int j = 0 ; j < playerNum ; j++){
				assertEquals(mockUsername.get(j), serverUsername.get(j));
			}

			Thread.sleep(1500);

			c1.close(true);
			c2.close(true);
			c3.close(true);

			Thread.sleep(1500);

			nc.closeServer();
		} catch (Exception e){
			System.out.println(e.getMessage());
		}
	}

	@Test
	public void playerOrderFour(){
		try{
			Thread.sleep(5000);
			
			int playerNum = 4;
			List<String> serverUsername;
			IoSession c1, c2, c3, c4;

			nc.startServer(srvIp, srvPort);

			c2 = nc.startClient(srvIp, srvPort, gc, "trecool", 7);
			Thread.sleep(1000);
			c1 = nc.startClient(srvIp, srvPort, gc, "mike", 9);
			Thread.sleep(1000);
			c3 = nc.startClient(srvIp, srvPort, gc, "billiejoe", 12);
			Thread.sleep(1000);
			c4 = nc.startClient(srvIp, srvPort, gc, "green", 3);
			Thread.sleep(1000);

			while(true){
				if(nc.getServerOpenedSession() == playerNum){
					serverUsername = nc.getServerUsernames();
					break;
				}
			}

			for(int j = 0 ; j < playerNum ; j++){
				assertEquals(mockUsername.get(j), serverUsername.get(j));
			}

			Thread.sleep(1500);

			c1.close(true);
			c2.close(true);
			c3.close(true);
			c4.close(true);

			Thread.sleep(1500);

			nc.closeServer();
		}catch (Exception e){
			System.out.println(e.getMessage());
		}
	}

	@Test
	public void playerOrderFive(){
		try{
			Thread.sleep(5000);
			
			int playerNum = 5;
			List<String> serverUsername;
			IoSession c1, c2, c3, c4, c5;

			nc.startServer(srvIp, srvPort);

			c2 = nc.startClient(srvIp, srvPort, gc, "trecool", 7);
			Thread.sleep(1000);
			c1 = nc.startClient(srvIp, srvPort, gc, "mike", 11);
			Thread.sleep(1000);
			c5 = nc.startClient(srvIp, srvPort, gc, "busi", 2);
			Thread.sleep(1000);
			c3 = nc.startClient(srvIp, srvPort, gc, "billiejoe", 12);
			Thread.sleep(1000);
			c4 = nc.startClient(srvIp, srvPort, gc, "green", 3);
			Thread.sleep(1000);
		
			while(true){
				if(nc.getServerOpenedSession() == playerNum){
					serverUsername = nc.getServerUsernames();
					break;
				}
			}

			for(int j = 0 ; j < playerNum ; j++){
				assertEquals(mockUsername.get(j), serverUsername.get(j));
			}

			Thread.sleep(1500);

			c1.close(true);
			c2.close(true);
			c3.close(true);
			c4.close(true);
			c5.close(true);

			Thread.sleep(1500);

			nc.closeServer();
		}catch (Exception e){
			System.out.println(e.getMessage());
		}
	}

	@Test
	public void playerOrderSix(){
		try{
			Thread.sleep(5000);
			
			int playerNum = 6;
			List<String> serverUsername;
			IoSession c1, c2, c3, c4, c5, c6;

			nc.startServer(srvIp, srvPort);

			c1 = nc.startClient(srvIp, srvPort, gc, "mike", 11);
			Thread.sleep(1000);
			c6 = nc.startClient(srvIp, srvPort, gc, "jan", 2);
			Thread.sleep(1000);
			c3 = nc.startClient(srvIp, srvPort, gc, "billiejoe", 12);
			Thread.sleep(1000);
			c4 = nc.startClient(srvIp, srvPort, gc, "green", 5);
			Thread.sleep(1000);
			c5 = nc.startClient(srvIp, srvPort, gc, "busi", 4);
			Thread.sleep(1000);
			c2 = nc.startClient(srvIp, srvPort, gc, "trecool", 9);
			Thread.sleep(1000);


			while(true){
				if(nc.getServerOpenedSession() == playerNum){
					serverUsername = nc.getServerUsernames();
					break;
				}
			}

			for(int j = 0 ; j < playerNum ; j++){
				assertEquals(mockUsername.get(j), serverUsername.get(j));
			}

			Thread.sleep(1500);

			c1.close(true);
			c2.close(true);
			c3.close(true);
			c4.close(true);
			c5.close(true);
			c6.close(true);

			Thread.sleep(1500);

			nc.closeServer();
		}catch (Exception e){
			System.out.println(e.getMessage());
		}
	}

	@Test
	public void playerOrderSeven(){
		try{
			Thread.sleep(5000);
			
			int playerNum = 7;
			List<String> serverUsername;
			IoSession c1, c2, c3, c4, c5, c6, c7;

			nc.startServer(srvIp, srvPort);

			c6 = nc.startClient(srvIp, srvPort, gc, "jan", 5);
			Thread.sleep(1000);
			c1 = nc.startClient(srvIp, srvPort, gc, "mike", 11);
			Thread.sleep(1000);
			c2 = nc.startClient(srvIp, srvPort, gc, "trecool", 9);
			Thread.sleep(1000);
			c7 = nc.startClient(srvIp, srvPort, gc, "biber", 4);
			Thread.sleep(1000);
			c5 = nc.startClient(srvIp, srvPort, gc, "busi", 6);
			Thread.sleep(1000);
			c3 = nc.startClient(srvIp, srvPort, gc, "billiejoe", 12);
			Thread.sleep(1000);
			c4 = nc.startClient(srvIp, srvPort, gc, "green", 8);
			Thread.sleep(1000);
			
			while(true){
				if(nc.getServerOpenedSession() == playerNum){
					serverUsername = nc.getServerUsernames();
					break;
				}
			}

			for(int j = 0 ; j < playerNum ; j++){
				assertEquals(mockUsername.get(j), serverUsername.get(j));
			}

			Thread.sleep(1500);

			c1.close(true);
			c2.close(true);
			c3.close(true);
			c4.close(true);
			c5.close(true);
			c6.close(true);
			c7.close(true);

			Thread.sleep(1500);

			nc.closeServer();
		}catch (Exception e){
			System.out.println(e.getMessage());
		}
	}

	@Test
	public void playerOrderEight(){
		try{
			
			Thread.sleep(5000);
			
			int playerNum = 8;
			List<String> serverUsername;
			IoSession c1, c2, c3, c4, c5, c6, c7, c8;

			nc.startServer(srvIp, srvPort);

			c8 = nc.startClient(srvIp, srvPort, gc, "peter", 2);
			Thread.sleep(1000);
			c1 = nc.startClient(srvIp, srvPort, gc, "mike", 11);
			Thread.sleep(1000);
			c6 = nc.startClient(srvIp, srvPort, gc, "jan", 5);
			Thread.sleep(1000);
			c2 = nc.startClient(srvIp, srvPort, gc, "trecool", 9);
			Thread.sleep(1000);
			c7 = nc.startClient(srvIp, srvPort, gc, "biber", 4);
			Thread.sleep(1000);
			c4 = nc.startClient(srvIp, srvPort, gc, "green", 8);
			Thread.sleep(1000);
			c5 = nc.startClient(srvIp, srvPort, gc, "busi", 6);
			Thread.sleep(1000);
			c3 = nc.startClient(srvIp, srvPort, gc, "billiejoe", 12);
			Thread.sleep(1000);
			
			while(true){
				if(nc.getServerOpenedSession() == playerNum){
					serverUsername = nc.getServerUsernames();
					break;
				}
			}

			for(int j = 0 ; j < playerNum ; j++){
				assertEquals(mockUsername.get(j), serverUsername.get(j));
			}

			Thread.sleep(1500);

			c1.close(true);
			c2.close(true);
			c3.close(true);
			c4.close(true);
			c5.close(true);
			c6.close(true);
			c7.close(true);
			c8.close(true);

			Thread.sleep(1500);

			nc.closeServer();
		}catch (Exception e){
			System.out.println(e.getMessage());
		}
	}

	@After
	public void tearDown(){
		nc.closeServer();
	}

}
