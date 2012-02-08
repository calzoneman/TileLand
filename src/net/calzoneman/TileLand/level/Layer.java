package net.calzoneman.TileLand.level;

import net.calzoneman.TileLand.tile.Tile;
import net.calzoneman.TileLand.tile.TileTypes;

public abstract class Layer {
	/** An array of IDs of tiles stored in this Layer */
	protected short[] tiles;
	/** An array of the data associated with each tile in the layer */
	protected byte[] data;
	/** Width of the layer, in tiles */
	protected int width = 0;
	/** Height of the layer, in tiles */
	protected int height = 0;
	
	/**
	 * Constructor
	 * Sets the width and height of the layer and generates map data with a new randomizer
	 * @param width The width of the layer
	 * @param height The height of the layer
	 */
	public Layer(int width, int height) {
		this.width = width;
		this.height = height;
		this.tiles = new short[width * height];
		this.data = new byte[width * height];
	}
	
	/**
	 * Constructor
	 * Sets the width, height, tiles, and data for the layer
	 * @param width The width of the layer
	 * @param height The height of the layer
	 * @param tiles The tile ID array of the layer
	 * @param data The data array of the layer
	 */
	public Layer(int width, int height, short[] tiles, byte[] data) {
		this.width = width;
		this.height = height;
		this.tiles = tiles;
		this.data = data;
	}
	
	/**
	 * Generates default map data for this layer (e.g. default background, default foreground = air)
	 * To be implemented by subclasses.
	 */
	public abstract void generate();
	
	/**
	 * Retrieves the Tile ID at (x, y), if it exists
	 * @param x The x-coordinate of the tile to retrieve
	 * @param y The y-coordinate of the tile to retrieve
	 * @return The ID of the tile at (x, y), or -1 if the location is out-of-bounds
	 */
	public int getId(int x, int y) {
		if(tiles == null || x < 0 || x >= width || y < 0 || y >= height)
			return -1;
		return tiles[y * width + x];
	}
	
	/**
	 * Retrieves the Tile at (x, y), if it exists
	 * @param x The x-coordinate of the tile to retrieve
	 * @param y The y-coordinate of the tile to retrieve
	 * @return The Tile for the ID at (x, y), null if the location is out-of-bounds, or null if such a Tile does not exist
	 */
	public Tile getTile(int x, int y) {
		return TileTypes.getTile(getId(x, y));
	}
	
	/**
	 * Sets the Tile ID at (x, y)
	 * @param x The x-coordinate of the tile to set
	 * @param y The y-coordinate of the tile to set
	 * @param id The ID to set at (x, y)
	 * @return True if the tile at (x, y) is set to id, else false
	 */
	public boolean setId(int x, int y, int id) {
		if(tiles == null || x < 0 || x >= width || y < 0 || y >= height)
			return false;
		tiles[y * width + x] = (short) id;
		data[y * width + x] = 0;
		return true;
	}
	
	/**
	 * Sets the Tile at (x, y)
	 * @param x The x-coordinate of the tile to set
	 * @param y The y-coordinate of the tile to set
	 * @param t the Tile to set at (x, y)
	 * @return True if the tile at (x, y) is set to t, else false
	 */
	public boolean setTile(int x, int y, Tile t) {
		return setId(x, y, t.getId());
	}
	
	/**
	 * Retrieves the data for the tile at (x, y)
	 * @param x The x-coordinate of the data to retrieve
	 * @param y The y-coordinate of the data to retrieve
	 * @return The data at (x, y), or -1 if (x, y) is out-of-bounds
	 */
	public byte getData(int x, int y) {
		if(data == null || x < 0 || x >= width || y < 0 || y >= height)
			return -1;
		return data[y * width + x];
	}
	
	/**
	 * Sets the data for the tile at (x, y)
	 * @param x The x-coordinate of the data to set
	 * @param y The y-coordinate of the data to set
	 * @return True if the data at (x, y) is set to data, false otherwise
	 */
	public boolean setData(int x, int y, int data) {
		if(this.data == null || x < 0 || x >= width || y < 0 || y >= height)
			return false;
		this.data[y * width + x] = (byte) data;
		return true;
	}
	
	/**
	 * Getter for width
	 * @return The width of the layer
	 */
	public int getWidth() {
		return this.width;
	}
	
	/**
	 * Getter for height
	 * @return The height of the layer
	 */
	public int getHeight() {
		return this.height;
	}
	
	/**
	 * Getter for tile array
	 * @return The tile array for the layer
	 */
	public short[] getTileArray() {
		return this.tiles;
	}
	
	/**
	 * Getter for data array
	 * @return The data array for the layer
	 */
	public byte[] getDataArray() {
		return this.data;
	}
}
