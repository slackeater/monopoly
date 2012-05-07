package ch.bfh.monopoly.tests;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Set;

import org.apache.mina.core.filterchain.IoFilterChain;
import org.apache.mina.core.future.CloseFuture;
import org.apache.mina.core.future.ReadFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.service.IoService;
import org.apache.mina.core.service.TransportMetadata;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.session.IoSessionConfig;
import org.apache.mina.core.write.WriteRequest;
import org.apache.mina.core.write.WriteRequestQueue;

import ch.bfh.monopoly.common.Board;
import ch.bfh.monopoly.common.BoardController;
import ch.bfh.monopoly.common.GameClient;
import ch.bfh.monopoly.common.GameController;

public class TestInstanceGenerator {

	GameClient gameClient;
	GameController gc;
	Board board;
	BoardController bc;

	public TestInstanceGenerator() {
		gameClient = new GameClient(new Locale("EN"));
		gc = new GameController(gameClient);
		board = gameClient.getBoard();
		bc = new BoardController(board);
		
		String myName = "Justin";
		ArrayList<String> playerNames = new ArrayList<String>();
		playerNames.add(myName);
		playerNames.add("Giuseppe");
		playerNames.add("Damien");
		playerNames.add("Cyril");
		playerNames.add("Elie");

		gameClient.setUsersList(playerNames, myName);
		IoSession s = new IoSession() {

			@Override
			public WriteFuture write(Object arg0, SocketAddress arg1) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public WriteFuture write(Object arg0) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void updateThroughput(long arg0, boolean arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void suspendWrite() {
				// TODO Auto-generated method stub

			}

			@Override
			public void suspendRead() {
				// TODO Auto-generated method stub

			}

			@Override
			public void setCurrentWriteRequest(WriteRequest arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public Object setAttributeIfAbsent(Object arg0, Object arg1) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Object setAttributeIfAbsent(Object arg0) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Object setAttribute(Object arg0, Object arg1) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Object setAttribute(Object arg0) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Object setAttachment(Object arg0) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void resumeWrite() {
				// TODO Auto-generated method stub

			}

			@Override
			public void resumeRead() {
				// TODO Auto-generated method stub

			}

			@Override
			public boolean replaceAttribute(Object arg0, Object arg1,
					Object arg2) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean removeAttribute(Object arg0, Object arg1) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public Object removeAttribute(Object arg0) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public ReadFuture read() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public boolean isWriterIdle() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isWriteSuspended() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isReaderIdle() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isReadSuspended() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isIdle(IdleStatus arg0) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isConnected() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isClosing() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isBothIdle() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public double getWrittenMessagesThroughput() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public long getWrittenMessages() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public double getWrittenBytesThroughput() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public long getWrittenBytes() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public int getWriterIdleCount() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public WriteRequestQueue getWriteRequestQueue() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public TransportMetadata getTransportMetadata() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public SocketAddress getServiceAddress() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public IoService getService() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public int getScheduledWriteMessages() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public long getScheduledWriteBytes() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public SocketAddress getRemoteAddress() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public int getReaderIdleCount() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public double getReadMessagesThroughput() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public long getReadMessages() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public double getReadBytesThroughput() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public long getReadBytes() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public SocketAddress getLocalAddress() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public long getLastWriterIdleTime() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public long getLastWriteTime() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public long getLastReaderIdleTime() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public long getLastReadTime() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public long getLastIoTime() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public long getLastIdleTime(IdleStatus arg0) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public long getLastBothIdleTime() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public int getIdleCount(IdleStatus arg0) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public long getId() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public IoHandler getHandler() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public IoFilterChain getFilterChain() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public WriteRequest getCurrentWriteRequest() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Object getCurrentWriteMessage() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public long getCreationTime() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public IoSessionConfig getConfig() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public CloseFuture getCloseFuture() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public int getBothIdleCount() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public Set<Object> getAttributeKeys() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Object getAttribute(Object arg0, Object arg1) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Object getAttribute(Object arg0) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Object getAttachment() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public boolean containsAttribute(Object arg0) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public CloseFuture close(boolean arg0) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public CloseFuture close() {
				// TODO Auto-generated method stub
				return null;
			}
		};
		gameClient.setIoSession(s);
	}

	public GameClient getGameClient() {
		return gameClient;
	}

	public GameController getGc() {
		return gc;
	}

	public Board getBoard() {
		return board;
	}
	
	public BoardController getBc(){
		return bc;
	}

}
