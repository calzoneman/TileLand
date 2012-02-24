package net.calzoneman.TileLand;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

import org.lwjgl.Sys;

import net.calzoneman.TileLand.event.EventHandler;
import net.calzoneman.TileLand.gui.MenuManager;
import net.calzoneman.TileLand.net.PacketFactory;
import net.calzoneman.TileLand.net.PacketReader;
import net.calzoneman.TileLand.player.Player;
import net.calzoneman.TileLand.screen.ServerConnectScreen;

public class MultiplayerGame extends Game implements Runnable {
	
	public static final int PROTOCOL_VERSION = 1;
	
	static final int MAX_READ_PER_TICK = 10;
	static final int MAX_SEND_PER_TICK = 10;
	
	protected boolean ready = false;
	protected boolean connected = true;
	
	protected SocketChannel channel;
	protected SocketChannel serverChannel;
	protected Selector selector;
	protected Queue<Packet> packetQueue;
	
	protected ServerConnectScreen serverConnectScreen;

	public MultiplayerGame(Player player, String ip, int port) {
		super(player);
		packetQueue = new LinkedBlockingQueue<Packet>();
		serverConnectScreen = new ServerConnectScreen(this);
		serverConnectScreen.setActive(false); // So handleInput doesn't give it input control
		try {
			channel = SocketChannel.open();
			channel.configureBlocking(false);
			channel.connect(new InetSocketAddress(ip, port));
			Selector selector = Selector.open();
			channel.register(selector, SelectionKey.OP_CONNECT);
			queue(new Packet(PacketFactory.makeHandshake(PROTOCOL_VERSION, player.getName(), "")));
			//ready = true;
		}
		catch(IOException ex) {
			Sys.alert("TileLand", "Unable to reach server at " + ip + ":" + port);
			MenuManager.getMenuManager().goBack();
			return;
		}
	}
	
	public void read() {
		int n = 0;
		while(n < MAX_READ_PER_TICK) {
			try {
				ByteBuffer op = ByteBuffer.allocate(1);
				int read = serverChannel.read(op);
				if(read == 0)
					continue;
				switch(op.array()[0]) {
					case PacketFactory.OPCODE_LOGIN:
						handleLogin();
						break;
					case PacketFactory.OPCODE_LEVEL_BEGIN:
						handleNewLevel();
						break;
					case PacketFactory.OPCODE_LEVEL_PARTIAL:
						handlePartialLevel();
						break;
					case PacketFactory.OPCODE_LEVEL_END:
						handleEndLevel();
						break;
					case PacketFactory.OPCODE_FULL_INVENTORY:
						handleFullInventory();
						break;
					case PacketFactory.OPCODE_PLAYER_SPAWN:
						handleNewPlayer();
						break;
					case PacketFactory.OPCODE_MOVE:
						handleMove();
						break;
					case PacketFactory.OPCODE_PLAYER_DESPAWN:
						handlePlayerDie();
						break;
					case PacketFactory.OPCODE_TILE_CHANGE:
						handleBlockchange();
						break;
					case PacketFactory.OPCODE_TILE_DATA_UPDATE:
						handleBlockDataUpdate();
						break;
					case PacketFactory.OPCODE_CHAT_MESSAGE:
						handleChatMessage();
						break;
					case PacketFactory.OPCODE_KICK:
						handleKick();
						break;
					default:
						Sys.alert("TileLand", "Unknown packet received from server");
						break;
				}
			}
			catch(IOException e) {
				disconnect();
			}
			n++;
		}
	}
	
	public void write() {
		int n = 0;
		while( n < MAX_SEND_PER_TICK && !packetQueue.isEmpty() ) {
			try {
				serverChannel.write(ByteBuffer.wrap(packetQueue.remove().raw));
			} 
			catch (IOException e) {
				disconnect();
			}
		}
	}
	
	@Override
	public void tick() {
		if(!ready)
			return;
		
		super.tick();	
	}
	
	@Override
	public void render() {
		if(!ready)
			return;
		if(serverConnectScreen == null)
			super.render();
		serverConnectScreen.render();
	}
	
	private void queue(Packet packet) {
		packetQueue.add(packet);
	}

	private void handleLogin() throws IOException {
		int version = PacketReader.readByte(serverChannel);
		// The server should catch this, but just in case...
		if( version != PROTOCOL_VERSION ) {
			serverConnectScreen.setState(ServerConnectScreen.STATE_ERROR);
			serverConnectScreen.setMessage("Unrecognized protocol version: " + version);
		}
		
		String name = PacketReader.readString(serverChannel);
		String motd = PacketReader.readString(serverChannel);
		
		PacketReader.readByte(serverChannel);
		
		serverConnectScreen.setState(ServerConnectScreen.STATE_CONNECTED);
		serverConnectScreen.setMessage(name);
		serverConnectScreen.setMessageDetail(motd);
		ready = true;
	}
	
	private void handleNewLevel() {
		// TODO Auto-generated method stub
		
	}
	
	private void handlePartialLevel() {
		// TODO Auto-generated method stub
		
	}
	
	private void handleEndLevel() {
		// TODO Auto-generated method stub
		
	}
	
	private void handleFullInventory() {
		// TODO Auto-generated method stub
		
	}
	
	private void handleNewPlayer() {
		// TODO Auto-generated method stub
		
	}
	
	private void handleMove() {
		// TODO Auto-generated method stub
		
	}

	private void handlePlayerDie() {
		// TODO Auto-generated method stub
		
	}

	private void handleBlockchange() {
		// TODO Auto-generated method stub
		
	}

	private void handleBlockDataUpdate() {
		// TODO Auto-generated method stub
		
	}

	private void handleChatMessage() {
		// TODO Auto-generated method stub
		
	}

	private void handleKick() {
		// TODO Auto-generated method stub
		
	}

	private void disconnect() {
		serverConnectScreen.setState(ServerConnectScreen.STATE_ERROR);
		serverConnectScreen.setMessage("Lost connection");
	}

	@Override
	public boolean isMultiplayer() {
		return true;
	}
	
	@Override
	public void run() {
		while(connected) {
			try {
				selector.select();
			}
			catch(IOException ex) {
				disconnect();
				return;
			}
			Set<SelectionKey> keys = selector.keys();
			Iterator<SelectionKey> itr = keys.iterator();
			while(itr.hasNext()) {
				SelectionKey key = itr.next();
				if(key.isConnectable()) {
					serverChannel = (SocketChannel) key.channel();
					return;
				}
				
				if(key.isReadable())
					read();
				if(key.isWritable())
					write();
			}
		}
	}
	
	protected class MultiplayerEventHandler extends EventHandler {

	}
	
	// Wrapper for Packet data
	private class Packet {
		public byte[] raw;
		public Packet(byte[] data) {
			raw = data;
		}
	}
	
	static class PacketUtil {
		// TODO test this for endian problems
		public static short getShort(byte[] b) {
			if(b.length != 2)
				return -1;
			return (short) ( (b[0] << 8) | (b[1]) );
		}
	}

}
