package net.calzoneman.TileLand.level;

import net.calzoneman.TileLand.tile.Tile;
import net.calzoneman.TileLand.tile.TileTypes;

public class ForegroundLayer extends Layer {
	
	/**
	 * Inherited constructors
	 */
	public ForegroundLayer(int width, int height) {
		super(width, height);
	}
	
	public ForegroundLayer(int width, int height, short[] tiles, byte[] data) {
		super(width, height, tiles, data);
	}

	@Override
	public void generate() {
		tiles = new short[width * height];
	}
}
