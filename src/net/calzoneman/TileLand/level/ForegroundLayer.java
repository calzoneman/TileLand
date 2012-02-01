package net.calzoneman.TileLand.level;

import java.util.Random;

import net.calzoneman.TileLand.tile.Tile;
import net.calzoneman.TileLand.tile.TileTypes;

public class ForegroundLayer extends Layer {

	/**
	 * Inherited constructors
	 */
	public ForegroundLayer(int width, int height) {
		super(width, height);
	}
	
	public ForegroundLayer(int width, int height, Random random) {
		super(width, height, random);
	}
	
	public ForegroundLayer(int width, int height, short[] tiles, byte[] data) {
		super(width, height, tiles, data);
	}

	@Override
	// TODO Implement
	public void generate(Random r) {
		for(int i = 0; i < width * height; i++) {
			tiles[i] = 0;
		}
	}

	@Override
	public Tile getTile(int x, int y) {
		return TileTypes.getFgTile(getId(x, y));
	}

}
