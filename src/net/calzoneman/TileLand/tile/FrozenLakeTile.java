package net.calzoneman.TileLand.tile;

import net.calzoneman.TileLand.inventory.Item;
import net.calzoneman.TileLand.level.Level;
import net.calzoneman.TileLand.player.Player;

import org.newdawn.slick.opengl.Texture;

public class FrozenLakeTile extends PartialDirectionalTile {

	public FrozenLakeTile(short id, String name, Texture texture) {
		super(id, name, texture);
	}

	@Override
	public boolean connectsTo(Tile other) {
		return other.id == id;
	}
	
	@Override
	public boolean transitionsTo(Tile other) {
		return other.id == TypeId.SNOWY_GRASS || other.id == TypeId.GRASS;
	}
	
	@Override
	public void hit(Level level, Player ply, Item with, int tx, int ty) {
		level.setBgId(tx, ty, TypeId.LAKE);
	}
}
