package ch.bfh.monopoly.net;

import java.util.ArrayList;
import java.util.List;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import ch.bfh.monopoly.common.Monopoly;

/**
 * This class is used to handle the operations of the server
 * over the network
 * @author snake
 *
 */
public class ServerHandler implements IoHandler{

	private List<IoSession> sessions = new ArrayList<IoSession>(); 

	/**
	 * Get the number of opened sessions on this server
	 * @return an int representing the number of sessions opened
	 */
	public int getOpenedSessions(){
		return this.sessions.size();
	}

	@Override
	public void exceptionCaught(IoSession arg0, Throwable arg1)
	throws Exception {
		// TODO Auto-generated method stub

	}

	/**
	 * Send a broadcast message to the session of this server
	 * @param n the NetMessage to broadcast
	 */
	public void sendBroadcast(NetMessage n, IoSession notBroadcast){
		for(int j = 0 ; j < sessions.size() ; j++){
			if(notBroadcast != sessions.get(j))
			sessions.get(j).write(n);
		}
	}
	
	@Override
	public void messageReceived(IoSession arg0, Object arg1) throws Exception {
		NetMessage n = (NetMessage)arg1;

		System.out.println("Received a message: " + n.getMessageCode());

		System.out.println("BROADCASTING...");

		//when we receive a message, we must broadcast it
		sendBroadcast(n, arg0);
		
	}

	@Override
	public void messageSent(IoSession arg0, Object arg1) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("message sent");

	}

	@Override
	public void sessionClosed(IoSession arg0) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("A player went down. Taking his properties and money.");

	}

	@Override
	public void sessionCreated(IoSession arg0) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("session created");
		sessions.add(arg0);
	}

	@Override
	public void sessionIdle(IoSession arg0, IdleStatus arg1) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void sessionOpened(IoSession arg0) throws Exception {
		
	}

}
