package ch.bfh.monopoly.net;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

/**
 * This class is used to handle the operations of the server
 * over the network
 * @author snake
 *
 */
public class ServerHandler implements IoHandler{

	/**
	 * This inner class is used to wrap the username 
	 * and the roll value for order into one single 
	 * object
	 * @author snake
	 *
	 */
	class PlayerWrapper {

		private String username;
		private int rollValue;

		public PlayerWrapper(String username, int rollValue){
			this.username = username;
			this.rollValue = rollValue;
		}

		
		public void setUsername(String s){
			this.username = s;
		}
		
		public String getUsername() {
			return username;
		}

		public int getRollValue() {
			return rollValue;
		}

	}

	private List<IoSession> sessions = new ArrayList<IoSession>(); 
	private List<String> usernames = new ArrayList<String>();
	private List<PlayerWrapper> plWrap = new ArrayList<PlayerWrapper>();
	private int userindex = 1;

	/**
	 * Get the number of opened sessions on this server
	 * @return an int representing the number of sessions opened
	 */
	public int getOpenedSessions(){
		return this.sessions.size();
	}

	/**
	 * Get the usernames of the players 
	 * @return a List that contains the player's names
	 */
	public List<String> getUsernames(){
		return Collections.unmodifiableList(usernames);
	}

	@Override
	public void exceptionCaught(IoSession arg0, Throwable arg1)
	throws Exception {
		System.out.println(arg1.getMessage());
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

	/**
	 * Build the list of user, by ordering the list looking at the
	 * roll order value and check if there are equals username
	 * @param n
	 */
	private void buildUserList(NetMessage n){
		String checkedName = n.getText();
		
		//if the username sended is equal to one in the wrap list, add a number to it
		for(int j = 0 ; j < plWrap.size() ; j++){
			if(n.getText().equals(plWrap.get(j).getUsername())){
				checkedName = n.getText().concat(Integer.toString(userindex));
				this.userindex++;
			}
		}
		
		this.plWrap.add(new PlayerWrapper(checkedName, n.getInt()));
		

		//bubble sort for sorting players
		int x = plWrap.size()-1;
		PlayerWrapper tmp;

		while(x > 0){
			for (int i = 0; i < x; i++) {                                       
				if (plWrap.get(i).getRollValue() < plWrap.get(i+1).getRollValue()) {                          
					tmp = plWrap.get(i);

					plWrap.set(i, plWrap.get(i+1));

					plWrap.set(i+1, tmp);
				}
			} 

			x--;
		}

		this.usernames.clear();
		
		for(int q = 0 ; q < plWrap.size() ; q++){
			//TODO remove sysout
			this.usernames.add(plWrap.get(q).getUsername());
			System.out.println("USER " + plWrap.get(q).getUsername() + " " + plWrap.get(q).getRollValue());
		}
		
		System.out.println("========");
	}
	
	@Override
	public void messageReceived(IoSession arg0, Object arg1) throws Exception {
		NetMessage n = (NetMessage)arg1;

		System.out.println("Received a message: " + n.getMessageCode());

		
		if(n.getMessageType() == Messages.SEND_USERNAME){
			buildUserList(n);
			
		}
		else
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
