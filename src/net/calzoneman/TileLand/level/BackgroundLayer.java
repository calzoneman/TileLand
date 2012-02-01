package net.calzoneman.TileLand.level;

import java.util.Random;

import net.calzoneman.TileLand.tile.Tile;
import net.calzoneman.TileLand.tile.TileTypes;

public class BackgroundLayer extends Layer {

	/**
	 * Inherited constructors
	 */
	public BackgroundLayer(int width, int height) {
		super(width, height);
	}
	
	public BackgroundLayer(int width, int height, Random random) {
		super(width, height, random);
	}
	
	public BackgroundLayer(int width, int height, short[] tiles, byte[] data) {
		super(width, height, tiles, data);
	}

	@Override
	public void generate(Random r) {
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++) {
				int grass = r.nextInt(100);
				if(grass <  5) {
					tiles[j * width + i] = (short)TileTypes.getBgTile("grass2").getId();
				}
				else if(grass < 10) {
					tiles[j * width + i] = (short)TileTypes.getBgTile("grass3").getId();
				}
				else {
					tiles[j * width + i] = (short)TileTypes.getBgTile("grass1").getId();
				}
			}
		}
	}

	@Override
	public Tile getTile(int x, int y) {
		return TileTypes.getBgTile(getId(x, y));
	}

}
