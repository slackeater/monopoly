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

	
	/**
	 * Start a server
	 * @param ip the ip to listen on
	 * @param port the port to listen on
	 * @throws IOException
	 */
	public void startServer(String ip, int port, ServerHandler srvHandler) throws IOException{
		IoAcceptor acc = new NioSocketAcceptor();
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
	 * @return IoSession the session that can be used to send message
	 */
	public IoSession startClient(String ip, int port, ClientHandler cliHandler){
		IoConnector ic = new NioSocketConnector();
		ic.getFilterChain().addLast("logger", new LoggingFilter());
		ic.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
		ic.setHandler(cliHandler);
		ConnectFuture cf = ic.connect(new InetSocketAddress(ip, port));

		// TODO what's this?? check the documentation
		cf.awaitUninterruptibly();
		
		IoSession io = cf.getSession();
		return io;
	}
	
}
