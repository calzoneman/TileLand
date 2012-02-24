package net.calzoneman.TileLand.net;

import java.nio.ByteBuffer;

public class PacketFactory {
	// Server -> Client packet types
	public static final byte OPCODE_LEVEL_BEGIN = 0x10;
	public static final byte OPCODE_LEVEL_PARTIAL = 0x11;
	public static final byte OPCODE_LEVEL_END = 0x12;
	public static final byte OPCODE_FULL_INVENTORY = 0x13;
	public static final byte OPCODE_PLAYER_SPAWN = 0x20;
	public static final byte OPCODE_PLAYER_DESPAWN = 0x22;
	public static final byte OPCODE_TILE_DATA_UPDATE = 0x31;
	public static final byte OPCODE_KICK = (byte) 0xFF;
		
	public static final byte OPCODE_LOGIN = 0x01;
	public static final byte OPCODE_INVENTORY_UPDATE = 0x14;
	public static final byte OPCODE_MOVE = 0x21;
	public static final byte OPCODE_TILE_CHANGE = 0x30;
	public static final byte OPCODE_CHAT_MESSAGE = 0x40;
	
	public static byte[] makeHandshake(int version, String name, String mppass) {
		byte[] bName = new byte[0];
		byte[] bMppass = new byte[0];
		
		try {
			bName = name.getBytes("UTF-8");
			bMppass = mppass.getBytes("UTF-8");
		}
		// Should never happen
		catch(Exception e) {
			e.printStackTrace();
		}
		
		ByteBuffer buf = ByteBuffer.allocate(6 + bName.length + bMppass.length);
		buf.put((byte) version);
		buf.putShort((short) bName.length);
		buf.put(bName);
		buf.putShort((short) bMppass.length);
		buf.put(bMppass);
		buf.put((byte) -1);
		
		return buf.array();
	}
}
