package ch.bfh.monopoly.net;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

public class Network {

	private IoAcceptor acc;
	private IoConnector ic;
	private IoSession clientSession;
	
	
	/**
	 * Start a server
	 * @param ip the ip to listen on
	 * @param port the port to listen on
	 * @throws IOException
	 */
	public void startServer(String ip, int port, ServerHandler srvHandler) throws IOException{
		acc = new NioSocketAcceptor();
		acc.setHandler(srvHandler);
		acc.getFilterChain().addLast("logger", new LoggingFilter());
		acc.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
		acc.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
		acc.bind(new InetSocketAddress(ip, port));
	}
	
	/**
	 * Connect to a server 
	 * @param ip the ip of the server
	 * @param port the port of the server
	 */
	public IoSession startClient(String ip, int port, ClientHandler cliHandler){
		ic = new NioSocketConnector();
		ic.getFilterChain().addLast("logger", new LoggingFilter());
		ic.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
		ic.setHandler(cliHandler);
		ConnectFuture cf = ic.connect(new InetSocketAddress(ip, port));

		// TODO what's this?? check the documentation
		cf.awaitUninterruptibly();
		
		this.clientSession = cf.getSession();	
		return clientSession;
	}
	
	/**
	 * Send a netmessage to the server 
	 * @param nm NetMessage
	 * 			the NetMessage to send to the server
	 */
	public void sendMessage(NetMessage nm) {
		try {
			this.clientSession.write(nm).await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Close the connection on the server side
	 */
	public void stopServer(){
		acc.dispose();
	}
	
}
