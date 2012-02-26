package net.calzoneman.TileLand.tile;

import org.newdawn.slick.opengl.Texture;

public class RoadTile extends DirectionalTile {

	public RoadTile(short id, String name, Texture texture) {
		super(id, name, texture);
	}

	@Override
	public boolean connectsTo(Tile other) {
		return other.id == id;
	}
	
	@Override
	public boolean transitionsTo(Tile other) {
		return other.id == TypeId.GRASS;
	}
}
