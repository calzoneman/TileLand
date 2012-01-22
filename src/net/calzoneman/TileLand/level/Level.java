package net.calzoneman.TileLand.level;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.util.Random;

import net.calzoneman.TileLand.tile.Tile;
import net.calzoneman.TileLand.tile.TileTypes;

public class Level {
	/** The size, in bytes, of the level header */
	public static final int HEADER_SIZE = 21;
	/** The magic number for the level header */
	public static final int SAVE_MAGIC = 0x54494c45; // "TILE" in ASCII hex
	/** The file format version */
	public static final byte SAVE_VERSION = 0x02;
	/** Constant for the dimension of a Tile texture */
	public static final int TILESIZE = 32;
	/** Randomizer for Level generation */
	private Random rand;
	/** The width of the Level, in tiles */
	private int width = 0;
	/** The height of the Level, in tiles */
	private int height = 0;
	/** The spawnpoint of the Level */
	private Location spawnpoint = new Location(0, 0);
	/** An array of ids for the background layer of the Level */
	private short[] bgTiles;
	/** An array of data bytes for the background layer of the level */
	private byte[] bgData;
	/** An array of ids for the foreground layer of the Level */
	private short[] fgTiles;
	/** An array of data bytes for the background layer of the level */
	private byte[] fgData;
	/** The name of the Level */
	public String name = "save";
	
	/**
	 * Constructor - Generates a new Level with the specified width and height
	 * @param width The width of the new map, in tiles
	 * @param height The height of the new map, in tiles
	 */
	public Level(int width, int height) {
		this.rand = new Random();
		this.width = width;
		this.height = height;
		this.generate(width, height);
	}
	
	/**
	 * Constructor - Generates a new Level width the specified width, height, and name
	 * @param width The width of the new map, in tiles
	 * @param height The height of the new map, in tiles
	 * @param name The name of the new map
	 */
	public Level(int width, int height, String name) {
		this(width, height);
		this.name = name;
	}
	
	/**
	 * Constructor - Loads a Level from disk
	 * @param filename The filename to load from, relative to saves/
	 */
	public Level(String filename) {
		this.rand = new Random();
		this.load(filename);
	}

	/**
	 * Saves the map to disk, using the save path saves/[this.name].tl
	 */
	public void save() {
		File savefile = new File("saves/" + name + ".tl");
		try {
			if(!(new File("saves").exists())) new File("saves").mkdir();
			FileOutputStream fos = new FileOutputStream(savefile);
			ByteBuffer buf = ByteBuffer.allocate(HEADER_SIZE + width * height * 6);
			buf.putInt(SAVE_MAGIC);
			buf.put(SAVE_VERSION);
			buf.putInt(width);
			buf.putInt(height);
			buf.putInt(spawnpoint.x);
			buf.putInt(spawnpoint.y);
			for(int i = 0; i < width * height; i++) {
				buf.putShort(bgTiles[i]);
			}
			for(int i = 0; i < width * height; i++) {
				buf.putShort(fgTiles[i]);
			}
			buf.put(bgData);
			buf.put(fgData);
			fos.write(buf.array());
			fos.close();
			buf = null;
			System.gc();
		}
		catch(IOException ex) {
			System.out.println("Unable to save level!");
		}
		
	}
	
	/**
	 * Loads a Level from disk and assigns appropriate fields
	 * @param filename The filename to load from, relative to saves/
	 */
	public void load(String filename) {
		File savefile = new File("saves/" + filename);
		if(!savefile.exists()) {
			System.out.println("Savefile doesn't exist!");
			return;
		}
		try {
			FileInputStream fis = new FileInputStream(savefile);
			ByteBuffer hdrbuf = ByteBuffer.allocate(HEADER_SIZE);
			fis.getChannel().read(hdrbuf);
			hdrbuf.rewind();
			if(hdrbuf.getInt() != SAVE_MAGIC) {
				System.out.println("Wrong magic number in level!");
				return;
			}
			if(hdrbuf.get() > SAVE_VERSION) {
				System.out.println("Wrong save format version");
				return;
			}
			this.width = hdrbuf.getInt();
			this.height = hdrbuf.getInt();
			this.spawnpoint = new Location(hdrbuf.getInt(), hdrbuf.getInt());
			ByteBuffer databuf = ByteBuffer.allocate(width * height * 6);
			fis.getChannel().read(databuf);
			fis.close();
			databuf.rewind();
			
			ShortBuffer mapBuf = databuf.asShortBuffer();
			bgTiles = new short[width * height];
			fgTiles = new short[width * height];
			mapBuf.get(bgTiles);
			mapBuf.get(fgTiles);
			databuf.position(mapBuf.position() * 2);
			bgData = new byte[width * height];
			fgData = new byte[width * height];
			databuf.get(bgData);
			databuf.get(fgData);
			this.name = filename.substring(0, filename.indexOf(".tl"));
		}
		catch(IOException ex) {
			System.out.println("Unable to load level!");
		}
		catch(Exception ex) {
			System.out.println("Unable to load level!");
		}
	}
	
	// Tile getters/setters
	
	// Background
	/**
	 * Get the ID of the background tile at (x, y)
	 * @param x The x coordinate of the Tile
	 * @param y The y coordinate of the Tile
	 * @return The ID of the background Tile at (x, y), or -1 if the location is invalid
	 */
	public short getBgId(int x, int y) {
		if(x < 0 || x >= width || y < 0 || y >= height) {
			return -1;
		}
		else {
			return bgTiles[y * width + x];
		}
	}
	
	/**
	 * Get the Tile in the background layer at (x, y)
	 * @param x The x coordinate of the Tile
	 * @param y The y coordinate of the Tile
	 * @return The background Tile at (x, y), or null if the location is invalid
	 */
	public Tile getBg(int x, int y) {
		return TileTypes.getBgTile(getBgId(x, y));
	}
	
	/**
	 * Set the ID of the background tile at (x, y)
	 * @param x The x coordinate of the tile to set
	 * @param y The y coordinate of the tile to set
	 * @param id The ID for the new tile
	 * @return true if the setting was successful, false otherwise
	 */
	public boolean setBgId(int x, int y, int id) {
		if(x < 0 || x >= width || y < 0 || y >= height) {
			return false;
		}
		bgTiles[y * width + x] = (short)id;
		setBgData(x, y, 0);
		updateBg(new Location(x, y));
		return true;
	}
	
	/**
	 * Set the ID of the background tile at (x, y)
	 * @param x The x coordinate of the tile to set
	 * @param y The y coordinate of the tile to set
	 * @param t The Tile object to copy the ID from
	 * @return
	 */
	public boolean setBg(int x, int y, Tile t) {
		if(t == null)
			return false;
		return setBgId(x, y, t.getId());
	}
	
	/**
	 * Gets the data field associated with the given point
	 * @param x The x coordinate to be checked
	 * @param y The y coordinate to be checked
	 * @return The data field associated with the point x, y
	 */
	public int getBgData(int x, int y) {
		if(x < 0 || x >= width || y < 0 || y >= height) 
			return -1;
		return bgData[y * width + x];
	}
	
	/**
	 * Sets the data field at x, y
	 * @param x The x coordinate to be set
	 * @param y The y coordinate to be set
	 * @param data The data to be set at x, y
	 * @return True if successful, false if not
	 */
	public boolean setBgData(int x, int y, int data) {
		if(x < 0 || x >= width || y < 0 || y >= height) {
			return false;
		}
		bgData[y * width + x] = (byte)data;
		return true;
	}
	
	/**
	 * Updates neighbors on the background layer
	 * @param src The Location to update
	 */
	public void updateBg(Location src) {
		getBg(src.x, src.y).update(this, src, src);
		getBg(src.x + 1, src.y).update(this, new Location(src.x + 1, src.y), src);
		getBg(src.x - 1, src.y).update(this, new Location(src.x - 1, src.y), src);
		getBg(src.x, src.y + 1).update(this, new Location(src.x, src.y + 1), src);
		getBg(src.x, src.y - 1).update(this, new Location(src.x, src.y - 1), src);
	}
	// Foreground
	/**
	 * Get the ID of the foreground tile at (x, y)
	 * @param x The x coordinate of the Tile
	 * @param y The y coordinate of the Tile
	 * @return The ID of the foreground Tile at (x, y), or -1 if the location is invalid
	 */
	public short getFgId(int x, int y) {
		if(x < 0 || x >= width || y < 0 || y >= height) {
			return -1;
		}
		else {
			return fgTiles[y * width + x];
		}
	}
	
	/**
	 * Get the Tile in the foreground layer at (x, y)
	 * @param x The x coordinate of the Tile
	 * @param y The y coordinate of the Tile
	 * @return The foreground Tile at (x, y), or null if the location is invalid
	 */
	public Tile getFg(int x, int y) {
		return TileTypes.getFgTile(getFgId(x, y));
	}
	
	/**
	 * Set the ID of the foreground tile at (x, y)
	 * @param x The x coordinate of the tile to set
	 * @param y The y coordinate of the tile to set
	 * @param id The ID for the new tile
	 * @return true if the setting was successful, false otherwise
	 */
	public boolean setFgId(int x, int y, int id) {
		if(x < 0 || x >= width || y < 0 || y >= height) {
			return false;
		}
		fgTiles[y * width + x] = (short)id;
		setFgData(x, y, 0);
		updateFg(new Location(x, y));
		return true;
	}
	
	/**
	 * Set the ID of the foreground tile at (x, y)
	 * @param x The x coordinate of the tile to set
	 * @param y The y coordinate of the tile to set
	 * @param t The Tile object to copy the ID from
	 * @return
	 */
	public boolean setFg(int x, int y, Tile t) {
		if(t == null)
			return false;
		return setFgId(x, y, t.getId());
	}
	
	/**
	 * Gets the data field associated with the given point
	 * @param x The x coordinate to be checked
	 * @param y The y coordinate to be checked
	 * @return The data field associated with the point x, y
	 */
	public int getFgData(int x, int y) {
		if(x < 0 || x >= width || y < 0 || y >= height) 
			return -1;
		return fgData[y * width + x];
	}
	
	/**
	 * Sets the data field at x, y
	 * @param x The x coordinate to be set
	 * @param y The y coordinate to be set
	 * @param data The data to be set at x, y
	 * @return True if successful, false if not
	 */
	public boolean setFgData(int x, int y, int data) {
		if(x < 0 || x >= width || y < 0 || y >= height) {
			return false;
		}
		fgData[y * width + x] = (byte)data;
		return true;
	}
	
	/**
	 * Updates neighbors on the foreground layer
	 * @param src The Location to update
	 */
	public void updateFg(Location src) {
		getFg(src.x, src.y).update(this, src, src);
		getFg(src.x + 1, src.y).update(this, new Location(src.x + 1, src.y), src);
		getFg(src.x - 1, src.y).update(this, new Location(src.x - 1, src.y), src);
		getFg(src.x, src.y + 1).update(this, new Location(src.x, src.y + 1), src);
		getFg(src.x, src.y - 1).update(this, new Location(src.x, src.y - 1), src);
	}
	
	// Generation
	public void generate(int width, int height) {
		this.bgTiles = new short[width * height];
		this.fgTiles = new short[width * height];
		
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++) {
				int grass = rand.nextInt(100);
				if(grass <  5) {
					bgTiles[j * width + i] = (short)TileTypes.getBgTile("grass2").getId();
				}
				else if(grass < 10) {
					bgTiles[j * width + i] = (short)TileTypes.getBgTile("grass3").getId();;
				}
				else {
					bgTiles[j * width + i] = (short)TileTypes.getBgTile("grass1").getId();;
				}
			}
		}
		
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++) {
				double fgstuff = rand.nextDouble();
				if(fgstuff < 0.05) {
					fgTiles[j * width + i] = (short)TileTypes.getFgTile("tree1").getId();
				}
				else if(fgstuff < 0.07) {
					fgTiles[j * width + i] = (short)TileTypes.getFgTile("tree2").getId();
				}
				else if(fgstuff < 0.09) {
					fgTiles[j *  width + i] = (short)TileTypes.getFgTile("bush1").getId();
				}
				else {
					fgTiles[j * width + i] = (short)TileTypes.getFgTile("air").getId();
				}
			}
		}
		
		this.spawnpoint = new Location(rand.nextInt(width), rand.nextInt(height));
		while(getFgId(spawnpoint.x, spawnpoint.y) != -1) {
			this.spawnpoint = new Location(rand.nextInt(width), rand.nextInt(height));
		}
	}
	
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public Location getSpawnpoint() {
		return spawnpoint;
	}

	public void setSpawnpoint(Location spawnpoint) {
		this.spawnpoint = new Location(spawnpoint.x, spawnpoint.y);
	}
	
}
