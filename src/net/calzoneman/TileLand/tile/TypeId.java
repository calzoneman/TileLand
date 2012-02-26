package net.calzoneman.TileLand.tile;

public class TypeId {
	// Special tiles
	public static final short NULL = -1;
	public static final short AIR = 0;
	
	// Background tiles [ 0x0001 to 0x0FFF ]
	public static final short DIRT = 1;
	public static final short GRASS = 2;
	public static final short LAKE = 3;
	public static final short SAND = 4;
	public static final short COBBLESTONE_ROAD = 5;
	public static final short SNOWY_GRASS = 6;
	public static final short LAKE_FROZEN = 7;
	
	// Foreground tiles [ 0x1000 to 0x1FFF ]
	public static final short TREE = 4096;
	public static final short BUSH = 4097;
	public static final short SIGN = 4098;
	public static final short ROCK = 4099;
	public static final short MOUNTAIN = 4100;
}
