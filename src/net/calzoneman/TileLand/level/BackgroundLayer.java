package net.calzoneman.TileLand.level;


import net.calzoneman.TileLand.tile.Tile;
import net.calzoneman.TileLand.tile.TileTypes;

public class BackgroundLayer extends Layer {

	/**
	 * Inherited constructors
	 */
	public BackgroundLayer(int width, int height) {
		super(width, height);
	}
	
	public BackgroundLayer(int width, int height, short[] tiles, byte[] data) {
		super(width, height, tiles, data);
	}

	@Override
	public void generate() {
		/*
		float[][] heightmap = new float[width][height];
		noise.PerlinNoiseMap(heightmap, 2, 5, 0.23f, 0, 0);
		Noise.Normalize(heightmap, 0.0f, 100.0f);
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++) {
				float n = heightmap[i][j];
				if(n < 25)
					setTile(i, j, TileTypes.getBgTile("lake"));
				else if(n < 35)
					setTile(i, j, TileTypes.getBgTile("sand"));
				else if(n < 88)
					setTile(i, j, TileTypes.getBgTile("grass1"));
				else if(fg != null)
					fg.setTile(i, j, TileTypes.getFgTile("rock"));
				else
					setTile(i, j, TileTypes.getBgTile("snow"));
			}
		}
		
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++) {
				getTile(i, j).updateData(this, new Location(i, j));
			}
		}*/
		tiles = new short[width * height];
	}

	@Override
	public Tile getTile(int x, int y) {
		return TileTypes.getBgTile(getId(x, y));
	}

}
