package net.calzoneman.TileLand.tile;

import org.newdawn.slick.geom.Rectangle;

public class SolidTile extends Tile {

	public SolidTile(int id, String name, Rectangle texPosition) {
		super(id, name, texPosition);
		this.properties = TileProperties.SOLID;
	}

}
