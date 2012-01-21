package net.calzoneman.TileLand.tile;

import java.util.HashMap;

import org.newdawn.slick.geom.Rectangle;

import net.calzoneman.TileLand.player.Player;

public class TileTypes {
	/** A Dictionary of Background Tiles keyed by id */
	private static HashMap<Integer, Tile> bgTileTypes;
	/** A Dictionary of Foreground Tiles keyed by id */
	private static HashMap<Integer, Tile> fgTileTypes;
	/** A Dictionary of Background Tile IDs keyed by name */
	private static HashMap<String, Integer> bgTileNames;
	/** A Dictionary of Foreground Tile IDs keyed by name */
	private static HashMap<String, Integer> fgTileNames;
	
	public static void init() {
		// Initialize Tile type Dictionaries
		bgTileTypes = new HashMap<Integer, Tile>();
		fgTileTypes = new HashMap<Integer, Tile>();
		
		// Initialize Tile name Dictionaries
		fgTileNames = new HashMap<String, Integer>();
		bgTileNames = new HashMap<String, Integer>();
		
		// Initialize Tile types
		addBgTile(new Tile(-1, "null", new Rectangle(0, 0, 32, 32), TileProperties.SOLID));
		addBgTile(new Tile(0, "grass1", new Rectangle(160, 192, 32, 32)));
		addBgTile(new Tile(1, "grass2", new Rectangle(192, 192, 32,32)));
		addBgTile(new Tile(2, "grass3", new Rectangle(224, 192, 32, 32)));		
		
		addFgTile(new Tile(-1, "air", new Rectangle(128, 448, 32, 32)));
		addFgTile(new Tile(0, "tree1", new Rectangle(32, 128, 32, 32), TileProperties.SOLID));
		addFgTile(new Tile(1, "tree2", new Rectangle(64, 128, 32, 32), TileProperties.SOLID));
		addFgTile(new Tile(2, "bush1", new Rectangle(96, 128, 32, 32), TileProperties.SOLID));
		addFgTile(new Tile(3, "sign1", new Rectangle(128, 128, 32, 32), TileProperties.SOLID));
		addFgTile(new Tile(4, "sign2", new Rectangle(160, 128, 32, 32), TileProperties.SOLID));
		addFgTile(new Tile(5, "sign3", new Rectangle(192, 128, 32, 32), TileProperties.SOLID));
	}
	
	/**
	 * Insert a background Tile into the appropriate dictionaries
	 * @param bg The Tile to be inserted
	 */
	private static void addBgTile(Tile bg) {
		bgTileTypes.put(bg.getId(), bg);
		bgTileNames.put(bg.getName(), bg.getId());
	}
	
	/**
	 * Insert a foreground Tile into the appropriate dictionaries
	 * @param fg The Tile to be inserted
	 */
	private static void addFgTile(Tile fg) {
		fgTileTypes.put(fg.getId(), fg);
		fgTileNames.put(fg.getName(), fg.getId());
	}
	
	/**
	 * Retrieves the default background tiletype
	 * @return The default background tiletype
	 */
	public static Tile getDefaultBg() {
		return bgTileTypes.get(0);
	}
	
	/**
	 * Retrieves the default foreground tiletype
	 * @return The default foreground tiletype
	 */
	public static Tile getDefaultFg() {
		return fgTileTypes.get(-1);
	}
	
	/**
	 * Retrieves the background Tile with the given id
	 * @param id The id of the Tile to be retrieved
	 * @return The background Tile with the requested id, or null if such a Tile does not exist
	 */
	public static Tile getBgTile(int id) {
		return bgTileTypes.get(id);
	}
	
	/**
	 * Retrieves the background Tile with the given name
	 * @param name The name of the Tile to be retrieved
	 * @return The background Tile with the requested name, or null if such a Tile does not exist
	 */
	public static Tile getBgTile(String name) {
		Integer id = bgTileNames.get(name);
		if(id == null)
			return null;
		return bgTileTypes.get(id);
	}
	
	/**
	 * Retrieves the foreground Tile with the given id
	 * @param id The id of the Tile to be retrieved
	 * @return The foreground Tile with the requested id, or null if such a Tile does not exist
	 */
	public static Tile getFgTile(int id) {
		return fgTileTypes.get(id);
	}
	
	/**
	 * Retrieves the foreground Tile with the given name
	 * @param name The name of the Tile to be retrieved
	 * @return The foreground Tile with the requested name, or null if such a Tile does not exist
	 */
	public static Tile getFgTile(String name) {
		Integer id = fgTileNames.get(name);
		if(id == null)
			return null;
		return fgTileTypes.get(id);
	}
	
	/**
	 * Determines whether a player can place the given background Tile
	 * @param ply The player for which the permission is requested
	 * @param bg The id of the Tile the permission is wanted for
	 * @return true if the player is allowed to place the Tile, false otherwise
	 */
	public static boolean playerCanPlaceBg(Player ply, int bg) {
		switch(bg) {
			default: return true;
		}
	}
	
	/**
	 * Determines whether a player can place the given foreground Tile
	 * @param ply The player for which the permission is requested
	 * @param fg The id of the Tile the permission is wanted for
	 * @return true if the player is allowed to place the Tile false otherwise
	 */
	public static boolean playerCanPlaceFg(Player ply, int fg) {
		switch(fg) {
			default: return true;
		}
	}
	
	/**
	 * Convenience method accepting a Tile object instead of an id
	 * @see playerCanPlaceBg(Player ply, int bg)
	 */
	public static boolean playerCanPlaceBg(Player ply, Tile bg) {
		return playerCanPlaceBg(ply, bg.getId());
	}
	
	/**
	 * Convenience method accepting a Tile object instead of an id
	 * @see playerCanPlaceFg(Player ply, int fg)
	 */
	public static boolean playerCanPlaceFg(Player ply, Tile fg) {
		return playerCanPlaceFg(ply, fg.getId());
	}
}
