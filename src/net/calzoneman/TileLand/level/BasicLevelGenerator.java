package net.calzoneman.TileLand.level;

import net.calzoneman.TileLand.tile.TileId;
import net.calzoneman.TileLand.tile.TileTypes;
import net.calzoneman.TileLand.tile.MultidataTile;

public class BasicLevelGenerator extends LevelGenerator {
	
	public BasicLevelGenerator() {
		super();
	}

	@Override
	public Level generate(int width, int height, int seed) {		
		Noise n = new Noise(seed, Noise.INTERPOLATE_SPLINE);
		Level level;
		BackgroundLayer bg;
		ForegroundLayer fg;
		
		short[] bgTiles = new short[width * height];
		short[] fgTiles = new short[width * height];
		
		byte[] bgData = new byte[width * height];
		byte[] fgData = new byte[width * height];
		
		// Generate terrain
		float[][] heightmap = new float[width][height];
		n.PerlinNoiseMap(heightmap, 2, 5, 0.23f, 0, 0);
		Noise.Normalize(heightmap, 0.0f, 100.0f);
		
		for(int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				float ht = heightmap[i][j];
				int index = j * width + i;
				if(ht < 20.0f) {
					bgTiles[index] = TileId.LAKE;
					if(ht < 17.5f && rand.nextFloat() < 0.04f)
						fgTiles[index] = TileId.ROCK_1;
				}
				else if(ht < 25.0f)
					bgTiles[index] = TileId.SAND;
				else if(ht < 84.0f) {
					bgTiles[index] = TileId.GRASS;
					bgData[index] = (byte) rand.nextInt(((MultidataTile) TileTypes.getTile(TileId.GRASS)).getNumStates());
				}
				else {
					fgTiles[index] = TileId.MOUNTAIN;
					bgTiles[index] = TileId.GRASS;
					bgData[index] = (byte) rand.nextInt(((MultidataTile) TileTypes.getTile(TileId.GRASS)).getNumStates());
				}
			}
		}
		
		heightmap = null;
		
		// Generate trees
		float[][] treemap = new float[width][height];
		n.PerlinNoiseMap(treemap, 3, 5, 0.46f, -width, -height);
		Noise.Normalize(treemap, 0.0f, 100.0f);
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++) {
				float ht = treemap[i][j];
				int index = j * width + i;
				if(ht > 75.0f && (fgTiles[index] == 0) && bgTiles[index] == TileId.GRASS) {
					if(rand.nextInt(2) == 0)
						fgTiles[index] = TileId.TREE_1;
					else
						fgTiles[index] = TileId.TREE_2;
				}
			}
		}
		treemap = null;
		
		// Fix up mountains
		// Removes mountain pieces that are only connected to zero or one other mountain piece
		int i = 0;
		int j = 0;
		while(i < width) {
			while(j < height) {
				int index = j * width + i;
				if(fgTiles[index] == TileId.MOUNTAIN) {
					if(getTile(fgTiles, (j-1) * width + i) == TileId.MOUNTAIN &&
							getTile(fgTiles, (j+1) * width + i) != TileId.MOUNTAIN &&
							getTile(fgTiles, j * width + i-1) != TileId.MOUNTAIN &&
							getTile(fgTiles, j * width + i+1) != TileId.MOUNTAIN) {
						fgTiles[index] = 0;
						j--;
					}
					else if(getTile(fgTiles, (j+1) * width + i) == TileId.MOUNTAIN &&
							getTile(fgTiles, (j-1) * width + i) != TileId.MOUNTAIN &&
							getTile(fgTiles, j * width + i-1) != TileId.MOUNTAIN &&
							getTile(fgTiles, j * width + i+1) != TileId.MOUNTAIN) {
						fgTiles[index] = 0;
						j++;
					}
					else if(getTile(fgTiles, j * width + i-1) == TileId.MOUNTAIN &&
							getTile(fgTiles, (j-1) * width + i) != TileId.MOUNTAIN &&
							getTile(fgTiles, (j+1) * width + i) != TileId.MOUNTAIN &&
							getTile(fgTiles, j * width + i+1) != TileId.MOUNTAIN) {
						fgTiles[index] = 0;
						i--;
					}
					else if(getTile(fgTiles, j * width + i+1) == TileId.MOUNTAIN &&
							getTile(fgTiles, (j-1) * width + i) != TileId.MOUNTAIN &&
							getTile(fgTiles, (j+1) * width + i) != TileId.MOUNTAIN &&
							getTile(fgTiles, j * width + i-1) != TileId.MOUNTAIN) {
						fgTiles[index] = 0;
						i++;
					}
					else if(getTile(fgTiles, j * width + i+1) != TileId.MOUNTAIN &&
							getTile(fgTiles, (j-1) * width + i) != TileId.MOUNTAIN &&
							getTile(fgTiles, (j+1) * width + i) != TileId.MOUNTAIN &&
							getTile(fgTiles, j * width + i-1) != TileId.MOUNTAIN) {
						fgTiles[index] = 0;
						j++;
					}
					else {
						j++;
					}
				}
				else {
					j++;
				}
			}
			i++;
			j = 0;
		}
		
		// Set up layers
		bg = new BackgroundLayer(width, height, bgTiles, bgData);
		fg = new ForegroundLayer(width, height, fgTiles, fgData);
		
		// Process tile data
		for(i = 0; i < width; i++) {
			for(j = 0; j < height; j++) {
				bg.getTile(i, j).updateData(bg, new Location(i, j));
				fg.getTile(i, j).updateData(fg, new Location(i, j));
			}
		}
		
		// Make the Level
		level = new Level(bg, fg);
		return level;
	}
	
	static short getTile(short[] tiles, int index) {
		if(index < tiles.length && index > 0)
			return tiles[index];
		return -1;
	}
}
