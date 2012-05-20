package ch.bfh.monopoly.net;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;


public class ServerNetwork {
	private IoAcceptor acc;
	private ServerHandler srvHandler;
	
	public ServerNetwork(ServerHandler srvHandler){
		this.srvHandler = srvHandler;
	}
	
	/**
	 * Start a server
	 * @param ip the ip to listen on
	 * @param port the port to listen on
	 * @throws IOException
	 */
	public void startServer(String ip, int port) throws IOException{
		acc = new NioSocketAcceptor();
		acc.setHandler(srvHandler);
		acc.getFilterChain().addLast("logger", new LoggingFilter());
		acc.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
		acc.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
		acc.bind(new InetSocketAddress(ip, port));
	}
	
	/**
	 * Stop the server
	 */
	public void stopServer(){
		acc.dispose();
	}
	
	
}
