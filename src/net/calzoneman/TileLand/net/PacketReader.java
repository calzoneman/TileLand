package net.calzoneman.TileLand.net;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class PacketReader {
	public static byte readByte(SocketChannel channel) throws IOException {
		ByteBuffer buf = ByteBuffer.allocate(1);
		channel.read(buf);
		return buf.array()[0];
	}
	
	public static short readShort(SocketChannel channel) throws IOException {
		ByteBuffer buf = ByteBuffer.allocate(2);
		channel.read(buf);
		return getShort(buf.array());
	}
	
	public static String readString(SocketChannel channel) throws IOException {
		short len = readShort(channel);
		ByteBuffer str = ByteBuffer.allocate(len);
		channel.read(str);
		return new String(str.array(), "UTF-8");
	}
	
	public static short getShort(byte[] b) {
		if(b.length != 2)
			return -1;
		return (short) ( (b[0] << 8) | (b[1]) );
	}
}
