package net.calzoneman.TileLand.tile;

import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

import net.calzoneman.TileLand.gfx.Renderer;
import net.calzoneman.TileLand.inventory.Item;
import net.calzoneman.TileLand.inventory.TileItem;
import net.calzoneman.TileLand.level.Level;
import net.calzoneman.TileLand.player.Player;

public class Tile {

	public static final int TILESIZE = 32;

	static final Color hoverGreen = new Color(0, 255, 0, 120);
	static final Color solidGreen = new Color(0, 255, 0);
	static final Color hoverRed = new Color(255, 0, 0, 120);
	static final Color solidRed = new Color(255, 0, 0);

	public final short id;
	public final String name;
	protected Texture texture;

	protected boolean solid = false;
	protected boolean liquid = false;
	protected boolean foreground = false;

	public Tile(short id, String name) {
		this.id = id;
		this.name = name;
	}

	public Tile(short id, String name, Texture texture) {
		this.id = id;
		this.name = name;
		this.texture = texture;
	}

	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture tex) {
		texture = tex;
	}

	public boolean connectsTo(Tile other) {
		return false;
	}
	
	public boolean transitionsTo(Tile other) {
		return false;
	}

	public void hit(Level level, Player player, Item with, int tx, int ty) {
		int data = isForeground() ? level.getFgData(tx, ty) : level.getBgData(tx, ty);
		if(isForeground())
			level.setFgId(tx, ty, TypeId.AIR);
		else
			level.setBgId(tx, ty, TypeId.DIRT);
		player.getPlayerInventory().addItem(new TileItem(this, data));
	}

	public void render(Level level, int tx, int ty, int x, int y) {
		Renderer.renderTexture(texture, x, y);
	}

	public boolean isSolid() {
		return solid;
	}
	
	public boolean isLiquid() {
		return liquid;
	}
	
	public boolean isForeground() {
		return foreground;
	}
}
