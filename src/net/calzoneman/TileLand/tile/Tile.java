package net.calzoneman.TileLand.tile;

import static org.lwjgl.opengl.GL11.*;
import net.calzoneman.TileLand.action.ActionResult;
import net.calzoneman.TileLand.gfx.Renderable;
import net.calzoneman.TileLand.inventory.Item;
import net.calzoneman.TileLand.inventory.ItemStack;
import net.calzoneman.TileLand.level.Layer;
import net.calzoneman.TileLand.level.Level;
import net.calzoneman.TileLand.level.Location;
import net.calzoneman.TileLand.player.Player;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.opengl.Texture;


public class Tile extends Item implements Renderable {
	/** The id of the tiletype.  In saves this is a short, but making it int here will save loads of unnecessary casting */
	private int id;
	/** A human readable string identifying the Tile */
	private String name;
	/** The tilesheet texture to be used */
	protected Texture tex;
	/** The rectangle of the tilesheet this Tile will render */
	protected Rectangle texPosition;
	/** A flags variable comprised of the properties of this Tile */
	protected int properties;
	
	/**
	 * Full constructor for Tile.  Sets all values appropriately.
	 * @param id The integer id for the Tile
	 * @param name A human readable name describing the Tile
	 * @param texPosition The Rectangle that this Tile's texture occupies in the tilesheet texture
	 */
	public Tile(int id, String name, Texture tex, Rectangle texPosition) {
		this(id, name, tex, texPosition, TileProperties.NONE);
	}
	
	public Tile(int id, String name, Texture tex, Rectangle texPosition, int properties) {
		this.id = id;
		this.name = name;
		this.tex = tex;
		this.texPosition = texPosition;
		this.properties = properties;
	}
	
	public void updateData(Layer level, Location self) { }
	
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
		return (this.properties & TileProperties.SOLID) >= 1;
	}
	
	public boolean isForeground() {
		return (this.properties & TileProperties.FOREGROUND) >= 1;
	}
	
	/**
	 * Determine whether the Tile is liquid
	 * @return true if the Tile is liquid, false otherwise
	 */
	public boolean isLiquid() {
		return (this.properties & TileProperties.LIQUID) >= 1;
	}
	
	/**
	 * Determine whether the Tile has multiple orientations
	 * @return true if the Tile has multiple orientations, false otherwise
	 */
	public boolean isMultidirectional() {
		return (this.properties & TileProperties.MULTIDIRECTIONAL) >= 1;
	}
	
	public boolean hasData() {
		return (this.properties & TileProperties.HASDATA) >= 1;
	}

	@Override
	public ActionResult leftClick(Player ply, int x, int y) {
		Level lvl = ply.getLevel();
		Tile before;
		if(isForeground())
			before = lvl.getFg(x, y);
		else
			before = lvl.getBg(x, y);
		boolean worked = true;
		if(isForeground())
			worked = lvl.getFgId(x, y) == TileId.AIR;
		if(worked)
			worked = lvl.setTile(x, y, this);
		Tile after;
		if(isForeground())
			after = lvl.getFg(x, y);
		else
			after = lvl.getBg(x, y);
		Tile result = null;
		if(before != after && worked) {
			result = before;
			ply.getInventory().removeOneItem(ply.getPlayerInventory().getQuickbar().getSelectedSlot());
		}
		return new ActionResult(ActionResult.TILE_PLACE, result);
	}

	@Override
	public ActionResult rightClick(Player ply, int x, int y) {
		Level lvl = ply.getLevel();
		int fg = lvl.getFgId(x, y);
		int fgdata = lvl.getFgData(x, y);
		if(fg == TileId.AIR) {
			int bg = lvl.getBgId(x, y);
			int bgdata = lvl.getBgData(x, y);
			if(!TileTypes.playerBreakable(bg))
				return new ActionResult(ActionResult.FAILURE, null);
			else if(lvl.setTile(x, y, TileTypes.getDefaultBg())) {
				ItemStack it = new ItemStack(TileTypes.getTile(bg), 1);
				if(TileTypes.getTile(bg).hasData() && !TileTypes.getTile(bg).isMultidirectional())
					it.setData(bgdata);
				ply.getInventory().addItemStack(it);
				return new ActionResult(ActionResult.TILE_BREAK, TileTypes.getTile(bg));
			}
			else
				return new ActionResult(ActionResult.FAILURE, null);
		}
		else if(!TileTypes.playerBreakable(fg)) {
			return new ActionResult(ActionResult.FAILURE, null);
		}
		else if (lvl.setTile(x, y, TileTypes.getDefaultFg())) {
			ItemStack it = new ItemStack(TileTypes.getTile(fg), 1);
			if(TileTypes.getTile(fg).hasData() && !TileTypes.getTile(fg).isMultidirectional())
				it.setData(fgdata);
			ply.getInventory().addItemStack(it);
			return new ActionResult(ActionResult.TILE_BREAK, TileTypes.getTile(fg));
		}
		else
			return new ActionResult(ActionResult.FAILURE, null);
	}

	@Override
	public void render(int x, int y) {
		if(tex == null || texPosition == null)
			return;
		int texWidth = tex.getTextureWidth();
		int texHeight = tex.getTextureHeight();
		float rectX = texPosition.getX();
		float rectY = texPosition.getY();
		float rectWidth = texPosition.getWidth();
		float rectHeight = texPosition.getHeight();
		tex.bind();
		glEnable(GL_BLEND);
		glBegin(GL_QUADS);
			glTexCoord2f(rectX / texWidth, rectY / texHeight);
			glVertex2f(x, y);
			glTexCoord2f((rectX + rectWidth) / texWidth, rectY / texHeight);
			glVertex2f(x + rectWidth, y);
			glTexCoord2f((rectX + rectWidth) / texWidth, (rectY + rectHeight) / texHeight);
			glVertex2f(x + rectWidth, y + rectHeight);
			glTexCoord2f(rectX / texWidth, (rectY + rectHeight) / texHeight);
			glVertex2f(x, y + rectHeight);
		glEnd();
		glDisable(GL_BLEND);
	}

	@Override
	public void render(int x, int y, int data) {
		render(x, y);		
	}
}
