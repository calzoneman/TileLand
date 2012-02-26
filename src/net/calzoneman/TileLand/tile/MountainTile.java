package net.calzoneman.TileLand.tile;

import org.newdawn.slick.opengl.Texture;

public class MountainTile extends PartialDirectionalFgTile {

	public MountainTile(short id, String name, Texture texture) {
		super(id, name, texture);
		solid = true;
	}

	@Override
	public boolean connectsTo(Tile other) {
		return other.id == id;
	}
	
	@Override
	public boolean transitionsTo(Tile other) {
		return other.id != id;
	}
}
