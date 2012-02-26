package net.calzoneman.TileLand.level;

import net.calzoneman.TileLand.tile.MultitextureTile;
import net.calzoneman.TileLand.tile.TypeId;
import net.calzoneman.TileLand.tile.TileTypes;

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
					bgTiles[index] = TypeId.LAKE;
					if(ht < 17.5f && rand.nextFloat() < 0.04f)
						fgTiles[index] = TypeId.ROCK;
				}
				else if(ht < 25.0f)
					bgTiles[index] = TypeId.SAND;
				else if(ht < 84.0f) {
					bgTiles[index] = TypeId.GRASS;
					bgData[index] = (byte) rand.nextInt(((MultitextureTile) TileTypes.getTile(TypeId.GRASS)).getNumStates());
				}
				else {
					fgTiles[index] = TypeId.MOUNTAIN;
					bgTiles[index] = TypeId.GRASS;
					bgData[index] = (byte) rand.nextInt(((MultitextureTile) TileTypes.getTile(TypeId.GRASS)).getNumStates());
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
				if(ht > 75.0f && (fgTiles[index] == 0) && bgTiles[index] == TypeId.GRASS) {
					fgTiles[index] = TypeId.TREE;
					fgData[index] = (byte) rand.nextInt(((MultitextureTile) TileTypes.getTile(TypeId.TREE)).getNumStates());
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
				if(fgTiles[index] == TypeId.MOUNTAIN) {
					if(getTile(fgTiles, (j-1) * width + i) == TypeId.MOUNTAIN &&
							getTile(fgTiles, (j+1) * width + i) != TypeId.MOUNTAIN &&
							getTile(fgTiles, j * width + i-1) != TypeId.MOUNTAIN &&
							getTile(fgTiles, j * width + i+1) != TypeId.MOUNTAIN) {
						fgTiles[index] = 0;
						j--;
					}
					else if(getTile(fgTiles, (j+1) * width + i) == TypeId.MOUNTAIN &&
							getTile(fgTiles, (j-1) * width + i) != TypeId.MOUNTAIN &&
							getTile(fgTiles, j * width + i-1) != TypeId.MOUNTAIN &&
							getTile(fgTiles, j * width + i+1) != TypeId.MOUNTAIN) {
						fgTiles[index] = 0;
						j++;
					}
					else if(getTile(fgTiles, j * width + i-1) == TypeId.MOUNTAIN &&
							getTile(fgTiles, (j-1) * width + i) != TypeId.MOUNTAIN &&
							getTile(fgTiles, (j+1) * width + i) != TypeId.MOUNTAIN &&
							getTile(fgTiles, j * width + i+1) != TypeId.MOUNTAIN) {
						fgTiles[index] = 0;
						i--;
					}
					else if(getTile(fgTiles, j * width + i+1) == TypeId.MOUNTAIN &&
							getTile(fgTiles, (j-1) * width + i) != TypeId.MOUNTAIN &&
							getTile(fgTiles, (j+1) * width + i) != TypeId.MOUNTAIN &&
							getTile(fgTiles, j * width + i-1) != TypeId.MOUNTAIN) {
						fgTiles[index] = 0;
						i++;
					}
					else if(getTile(fgTiles, j * width + i+1) != TypeId.MOUNTAIN &&
							getTile(fgTiles, (j-1) * width + i) != TypeId.MOUNTAIN &&
							getTile(fgTiles, (j+1) * width + i) != TypeId.MOUNTAIN &&
							getTile(fgTiles, j * width + i-1) != TypeId.MOUNTAIN) {
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
