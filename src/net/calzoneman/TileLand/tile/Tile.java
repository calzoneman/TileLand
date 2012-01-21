package net.calzoneman.TileLand.tile;

import org.newdawn.slick.geom.Rectangle;


public class Tile {
	/** The id of the tiletype.  In saves this is a short, but making it int here will save loads of unnecessary casting */
	private int id;
	/** A human readable string identifying the Tile */
	private String name;
	/** The rectangle of the tilesheet this Tile will render */
	private Rectangle texPosition;
	/** A flags variable comprised of the properties of this Tile */
	private int properties;
	
	/**
	 * Full constructor for Tile.  Sets all values appropriately.
	 * @param id The integer id for the Tile
	 * @param name A human readable name describing the Tile
	 * @param texPosition The Rectangle that this Tile's texture occupies in the tilesheet texture
	 * @param properties The properties of the Tile
	 */
	public Tile(int id, String name, Rectangle texPosition, int properties) {
		this.id = id;
		this.name = name;
		this.texPosition = texPosition;
		this.properties = properties;
	}
	
	/**
	 * Constructor
	 * @param id The integer id for the Tile
	 * @param name A human readable name describing the Tile
	 * @param texPosition The Rectangle that this Tile's texture occupies in the tilesheet texture
	 */
	public Tile(int id, String name, Rectangle texPosition) {
		this(id, name, texPosition, TileProperties.NONE);
	}
	
	/**
	 * Getter for the id field
	 * @return The id of the Tile
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Getter for the name field
	 * @return A human readable name describing the Tile
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Getter for the texPosition field
	 * @return
	 */
	public Rectangle getTexPosition() {
		return this.texPosition;
	}
	
	/**
	 * Getter for the properties field
	 * @return The combined properties of the Tile (individual properties are combined with bitwise or (|))
	 */
	public int getProperties() {
		return this.properties;
	}
	
	/**
	 * Determine whether the Tile is solid
	 * @return true if the Tile is solid, false otherwise
	 */
	public boolean isSolid() {
		return (this.properties & TileProperties.SOLID) == 1;
	}
	
	/**
	 * Determine whether the Tile is liquid
	 * @return true if the Tile is liquid, false otherwise
	 */
	public boolean isLiquid() {
		return (this.properties & TileProperties.LIQUID) == 1;
	}
}
