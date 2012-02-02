package net.calzoneman.TileLand.tile;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.opengl.Texture;

public class SolidTile extends Tile {

	public SolidTile(int id, String name, Texture tex, Rectangle texPosition) {
		super(id, name, tex, texPosition);
		this.properties = TileProperties.SOLID;
	}
	
	public SolidTile(int id, String name, Texture tex, Rectangle texPosition, int properties) {
		super(id, name, tex, texPosition);
		this.properties = TileProperties.SOLID | properties;
	}
}
