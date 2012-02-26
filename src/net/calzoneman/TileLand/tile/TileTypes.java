package net.calzoneman.TileLand.tile;

import java.io.IOException;
import java.util.HashMap;

import org.lwjgl.Sys;

import net.calzoneman.TileLand.gfx.SpriteSheet;
import net.calzoneman.TileLand.gui.MenuManager;
import static net.calzoneman.TileLand.tile.TypeId.*;

public class TileTypes {
	/** A Dictionary of Tiles keyed by id */
	private static HashMap<Integer, Tile> tileTypes;
	/** A Dictionary of Tile IDs keyed by name */
	private static HashMap<String, Integer> tileNames;

	public static void init(SpriteSheet sheet) {
		// Initialize Tile type Dictionary
		tileTypes = new HashMap<Integer, Tile>();

		// Initialize Tile name Dictionaries
		tileNames = new HashMap<String, Integer>();

		// Initialize Tile types
		try {
			addTile(new Tile(NULL, "null", sheet.getTileTexture(9 * sheet.width)));
			addTile(new DirtTile(DIRT, "dirt", sheet.getCustomTileTexture(0, 0, 5, 3)));
			addTile(new MultitextureTile(GRASS, "grass", sheet.getCustomTileTexture(1, 9, 3, 1)));
			addTile(new LakeTile(LAKE, "lake", sheet.getCustomTileTexture(10, 3, 5, 3)));
			addTile(new SandTile(SAND, "sand", sheet.getCustomTileTexture(10, 0, 5, 3)));
			addTile(new RoadTile(COBBLESTONE_ROAD, "road", sheet.getCustomTileTexture(5, 0, 5, 3)));
			addTile(new SnowyGrassTile(SNOWY_GRASS, "snowy grass", sheet.getCustomTileTexture(0, 6, 5, 3)));
			addTile(new FrozenLakeTile(LAKE_FROZEN, "frozen lake", sheet.getCustomTileTexture(5, 3, 3, 3)));
			
			addTile(new Tile(AIR, "air"));
			addTile(new MultitextureForegroundTile(TREE, "tree", sheet.getCustomTileTexture(4, 9, 2, 1)));
			addTile(new MultitextureForegroundTile(BUSH, "bush", sheet.getCustomTileTexture(6, 9, 1, 1)));
			addTile(new MultitextureForegroundTile(SIGN, "sign", sheet.getCustomTileTexture(7, 9, 3, 1)));
			addTile(new MultitextureForegroundTile(ROCK, "rock", sheet.getCustomTileTexture(11, 9, 3, 1)));
			addTile(new MountainTile(MOUNTAIN, "mountain", sheet.getCustomTileTexture(0, 3, 3, 3)));
		} 
		catch (IOException ex) {
			Sys.alert("TileLand", "Unable to load tiletypes!");
			MenuManager.getMenuManager().openMenu("mainmenu");
		}
	}

	/**
	 * Insert a Tile into the appropriate dictionaries
	 * 
	 * @param bg
	 *            The Tile to be inserted
	 */
	private static void addTile(Tile tile) {
		tileTypes.put((int) tile.id, tile);
		tileNames.put(tile.name, (int) tile.id);
	}

	/**
	 * Retrieves the default background tiletype
	 * 
	 * @return The default background tiletype
	 */
	public static Tile getDefaultBg() {
		return getTile("dirt");
	}

	/**
	 * Retrieves the default foreground tiletype
	 * 
	 * @return The default foreground tiletype
	 */
	public static Tile getDefaultFg() {
		return getTile("air");
	}

	/**
	 * Retrieves the Tile with the given id
	 * 
	 * @param id
	 *            The id of the Tile to be retrieved
	 * @return The Tile with the requested id, or null if such a Tile does not
	 *         exist
	 */
	public static Tile getTile(int id) {
		return tileTypes.get(id);
	}

	/**
	 * Retrieves the Tile with the given name
	 * 
	 * @param name
	 *            The name of the Tile to be retrieved
	 * @return The Tile with the requested name, or null if such a Tile does not
	 *         exist
	 */
	public static Tile getTile(String name) {
		Integer id = tileNames.get(name);
		if (id == null)
			return null;
		return tileTypes.get(id);
	}

	public static int getTileId(String name) {
		if (tileNames.get(name) != null)
			return tileNames.get(name);
		return -1;
	}

	public static boolean playerBreakable(int tt) {
		switch (tt) {
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
