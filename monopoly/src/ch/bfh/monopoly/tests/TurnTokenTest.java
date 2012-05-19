package ch.bfh.monopoly.tests;


import java.util.ArrayList;
import java.util.List;

import org.apache.mina.core.session.IoSession;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.bfh.monopoly.common.GameClient;
import ch.bfh.monopoly.common.NetworkController;
import ch.bfh.monopoly.net.Messages;
import ch.bfh.monopoly.net.NetMessage;

//TODO
public class TurnTokenTest {

	private static NetworkController nc;
	private static String srvIp;
	private static int srvPort;
	private static GameClient gc;
	private static List<String> mockUsername;
	private static IoSession c1, c2, c3, c4, c5, c6, c7, c8;

	@BeforeClass
	public static void oneTimeSetUp() throws Exception {
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
	}

	@AfterClass
	public static void tearDown() throws Exception {
		c1.close(true);
		c2.close(true);
		c3.close(true);
		c4.close(true);
		c5.close(true);
		c6.close(true);
		c7.close(true);
		c8.close(true);

		nc.closeServer();
	}

	@Test
	/**
	 * Simulate the sending of the first turn token
	 */
	public void firstTurnToken(){
		try{
			//send the token if we reach 8 sessions
			if(nc.getServerOpenedSession() == 8){
				//TODO note that the user who receives the token must set it to true 
				nc.sendTurnToken();
			}

			Thread.sleep(1500);

		} catch (Exception e){
			System.out.println(e.getMessage());
		}
	}

	@Test
	/**
	 * Simulate a complete turn from player 0 to player 7
	 * At the end the token must return to player 0
	 * !!! DO SOMETHING MORE CONSISTENT !!!
	 */
	public void simulateTurn(){
		try{
			//now c3 clicks end turn
			//!!!! c3 must set the turn token to false 
			NetMessage endTurnBillie = new NetMessage(mockUsername.get(0), Messages.END_TURN);
			c3.write(endTurnBillie);
			
			Thread.sleep(1500);

			NetMessage endTurnMike = new NetMessage(mockUsername.get(1), Messages.END_TURN);
			c1.write(endTurnMike);
			
			Thread.sleep(1500);

			NetMessage endTurnTre = new NetMessage(mockUsername.get(2), Messages.END_TURN);
			c2.write(endTurnTre);

			Thread.sleep(1500);

			NetMessage endTurnGreen = new NetMessage(mockUsername.get(3), Messages.END_TURN);
			c4.write(endTurnGreen);
			
			Thread.sleep(1500);

			NetMessage endTurnBusi = new NetMessage(mockUsername.get(4), Messages.END_TURN);
			c5.write(endTurnBusi);
			
			Thread.sleep(1500);

			NetMessage endTurnJan = new NetMessage(mockUsername.get(5), Messages.END_TURN);
			c6.write(endTurnJan);
			
			Thread.sleep(1500);

			NetMessage endTurnBiber = new NetMessage(mockUsername.get(6), Messages.END_TURN);
			c7.write(endTurnBiber);
			
			Thread.sleep(1500);

			NetMessage endTurnPeter = new NetMessage(mockUsername.get(7), Messages.END_TURN);
			c8.write(endTurnPeter);
			
			
			Thread.sleep(5500);
		} catch (Exception e){
			System.out.println(e.getMessage());
		}
	}



}
