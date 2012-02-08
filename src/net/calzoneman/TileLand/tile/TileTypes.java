package net.calzoneman.TileLand.tile;

import java.util.HashMap;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.opengl.Texture;

import net.calzoneman.TileLand.TileLand;
import static net.calzoneman.TileLand.tile.TileId.*;

public class TileTypes {
	/** A Dictionary of Tiles keyed by id */
	private static HashMap<Integer, Tile> tileTypes;
	/** A Dictionary of Tile IDs keyed by name */
	private static HashMap<String, Integer> tileNames;
	
	public static void init() {		
		// Initialize Tile type Dictionary
		tileTypes = new HashMap<Integer, Tile>();
		
		// Initialize Tile name Dictionaries
		tileNames = new HashMap<String, Integer>();
		
		Texture tex = TileLand.getTextureManager().getTexture("tiles.png");
		
		// Initialize Tile types
		addTile(new SolidTile(NULL, "null", tex, new Rectangle(0, 0, 32, 32)));
		addTile(new MultidirectionalTile(DIRT, "dirt", tex, new Rectangle(352, 0, 96, 96), false));
		addTile(new MultidataTile(GRASS, "grass", tex, new Rectangle(160, 192, 96, 96)));
		addTile(new MultidirectionalTile(LAKE, "lake", tex, new Rectangle(160, 288, 96, 96), false));
		addTile(new MultidirectionalTile(SAND, "sand", tex, new Rectangle(320, 288, 96, 96), false));
		addTile(new MultidirectionalTile(COBBLESTONE_ROAD, "cobbleroad", tex, new Rectangle(0, 192, 96, 96), false));
		addTile(new MultidirectionalTile(SNOWY_GRASS, "snow", tex, new Rectangle(0, 384, 96, 96), false));
		addTile(new MultidirectionalTile(LAKE_FROZEN, "frozenlake", tex, new Rectangle(0, 288, 96, 96), false));
		
		addTile(new Tile(AIR, "air", null, null, TileProperties.FOREGROUND));
		addTile(new SolidTile(TREE_1, "tree1", tex, new Rectangle(32, 128, 32, 32), TileProperties.FOREGROUND));
		addTile(new SolidTile(TREE_2, "tree2", tex, new Rectangle(64, 128, 32, 32), TileProperties.FOREGROUND));
		addTile(new SolidTile(BUSH, "bush", tex, new Rectangle(96, 128, 32, 32), TileProperties.FOREGROUND));
		addTile(new MultidataTile(SIGN, "sign", tex, new Rectangle(160, 128, 64, 32), TileProperties.FOREGROUND | TileProperties.SOLID));
		addTile(new SolidTile(ROCK_1, "rock1", tex, new Rectangle(0, 160, 32, 32), TileProperties.FOREGROUND));
		addTile(new MultidirectionalTile(MOUNTAIN, "mountain", tex, new Rectangle(320, 192, 96, 96), true, TileProperties.FOREGROUND | TileProperties.SOLID));
	}
	
	/**
	 * Insert a Tile into the appropriate dictionaries
	 * @param bg The Tile to be inserted
	 */
	private static void addTile(Tile tile) {
		tileTypes.put(tile.getId(), tile);
		tileNames.put(tile.getName(), tile.getId());
	}
	
	/**
	 * Retrieves the default background tiletype
	 * @return The default background tiletype
	 */
	public static Tile getDefaultBg() {
		return getTile("dirt");
	}
	
	/**
	 * Retrieves the default foreground tiletype
	 * @return The default foreground tiletype
	 */
	public static Tile getDefaultFg() {
		return getTile("air");
	}
	
	/**
	 * Retrieves the Tile with the given id
	 * @param id The id of the Tile to be retrieved
	 * @return The Tile with the requested id, or null if such a Tile does not exist
	 */
	public static Tile getTile(int id) {
		return tileTypes.get(id);
	}
	
	/**
	 * Retrieves the Tile with the given name
	 * @param name The name of the Tile to be retrieved
	 * @return The Tile with the requested name, or null if such a Tile does not exist
	 */
	public static Tile getTile(String name) {
		Integer id = tileNames.get(name);
		if(id == null)
			return null;
		return tileTypes.get(id);
	}
	
	public static int getTileId(String name) {
		if(tileNames.get(name) != null)
			return tileNames.get(name);
		return -1;
	}
	
	public static boolean playerBreakable(int tt) {
		switch(tt) {
			case NULL:
			case AIR:
			case DIRT:
			case LAKE:
				return false;
			default:
				return true;
		}
	}

}
