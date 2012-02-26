package net.calzoneman.TileLand.tile;

import org.newdawn.slick.opengl.Texture;

public class SandTile extends DirectionalTile {

	public SandTile(short id, String name, Texture texture) {
		super(id, name, texture);
	}

	@Override
	public boolean connectsTo(Tile other) {
		return other.id == id || other.id == TypeId.LAKE;
	}
	
	@Override
	public boolean transitionsTo(Tile other) {
		return other.id == TypeId.GRASS;
	}
}
