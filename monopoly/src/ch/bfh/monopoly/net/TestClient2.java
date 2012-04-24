package ch.bfh.monopoly.net;

import java.net.InetSocketAddress;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

public class TestClient2 {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		IoConnector ic = new NioSocketConnector();
		ic.getFilterChain().addLast("logger", new LoggingFilter());
		ic.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
		ic.setHandler(new ClientHandler());
		
		ConnectFuture cf = ic.connect(new InetSocketAddress("192.168.2.4", 1234));
		//ConnectFuture cf = ic.connect(new InetSocketAddress("147.87.123.220", 1234));

		IoSession io = null;
		
		//Thread.sleep(7000);
		
		try {
			//cf.awaitUninterruptibly();
			io = cf.getSession();
			NetMessage m = new NetMessage(Messages.ACKNOWLEDGE);
			io.write(m).await();
			
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	
		NetMessage one = new NetMessage(Messages.MORTGAGE);
		NetMessage two = new NetMessage(Messages.UNMORTGAGE);
		NetMessage three = new NetMessage(Messages.KICK_PLAYER);
		NetMessage four = new NetMessage(Messages.KICK_ANSWER);
		NetMessage five = new NetMessage(Messages.QUIT_GAME);
				
//		try {
//			n.startClient("147.87.123.220", 1234);
//		} catch (UnknownHostException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		
//		n.addMsg(one);
//		Thread.sleep(5000);
//		n.addMsg(two);
//		Thread.sleep(5000);
//		n.addMsg(three);
//		Thread.sleep(5000);
//		n.addMsg(four);
//		Thread.sleep(5000);
//		n.addMsg(five);
		
		io.write(one);
		Thread.sleep(2000);
		io.write(two);
		Thread.sleep(2000);
		io.write(three);
		Thread.sleep(2000);
		io.write(four);
		Thread.sleep(2000);
		io.write(five);
		Thread.sleep(2000);
		
		
		System.out.println("I'm sleeping for 30 seconds");
		try {
			Thread.sleep(10000);
			System.out.println("Ehi I wake up!!");
			io.write(one);
			Thread.sleep(2000);
			io.write(two);
			Thread.sleep(2000);
			io.write(three);
			Thread.sleep(2000);
			io.write(four);
			Thread.sleep(2000);
			io.write(five);
			
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
