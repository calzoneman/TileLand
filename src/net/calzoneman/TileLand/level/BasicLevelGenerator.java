package net.calzoneman.TileLand.level;

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
		
		// Generate terrain
		float[][] heightmap = new float[width][height];
		n.PerlinNoiseMap(heightmap, 2, 5, 0.23f, 0, 0);
		Noise.Normalize(heightmap, 0.0f, 100.0f);
		
		for(int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				float ht = heightmap[i][j];
				int index = j * width + i;
				if(ht < 20.0f) {
					bgTiles[index] = (short) TileTypes.getBgTile("lake").getId();
					if(ht < 17.5f && rand.nextFloat() < 0.04f)
						fgTiles[index] = (short) TileTypes.getFgTile("rock1").getId();
				}
				else if(ht < 25.0f)
					bgTiles[index] = (short) TileTypes.getBgTile("sand").getId();
				else if(ht < 84.0f) {
					switch(rand.nextInt(3)) {
						case 0:
							bgTiles[index] = (short) TileTypes.getBgTile("grass1").getId();
							break;
						case 1:
							bgTiles[index] = (short) TileTypes.getBgTile("grass2").getId();
							break;
						default:
							bgTiles[index] = (short) TileTypes.getBgTile("grass3").getId();
							break;
					}
				}
				else
					fgTiles[index] = (short) TileTypes.getFgTile("mountain").getId();
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
				if(ht > 75.0f && (fgTiles[index] == 0) && 
						(bgTiles[index] == (short) TileTypes.getBgTile("grass1").getId() ||
						bgTiles[index] == (short) TileTypes.getBgTile("grass2").getId() ||
						bgTiles[index] == (short) TileTypes.getBgTile("grass3").getId())) {
					if(rand.nextInt(2) == 0)
						fgTiles[index] = (short) TileTypes.getFgTile("tree1").getId();
					else
						fgTiles[index] = (short) TileTypes.getFgTile("tree2").getId();
				}
			}
		}
		treemap = null;
		
		// Fix up mountains
		// Removes mountain pieces that are only connected to zero or one other mountain piece
		final short MOUNTAIN = (short) TileTypes.getFgTile("mountain").getId();
		int i = 0;
		int j = 0;
		while(i < width) {
			while(j < height) {
				int index = j * width + i;
				if(fgTiles[index] == MOUNTAIN) {
					if(getTile(fgTiles, (j-1) * width + i) == MOUNTAIN &&
							getTile(fgTiles, (j+1) * width + i) != MOUNTAIN &&
							getTile(fgTiles, j * width + i-1) != MOUNTAIN &&
							getTile(fgTiles, j * width + i+1) != MOUNTAIN) {
						fgTiles[index] = 0;
						j--;
					}
					else if(getTile(fgTiles, (j+1) * width + i) == MOUNTAIN &&
							getTile(fgTiles, (j-1) * width + i) != MOUNTAIN &&
							getTile(fgTiles, j * width + i-1) != MOUNTAIN &&
							getTile(fgTiles, j * width + i+1) != MOUNTAIN) {
						fgTiles[index] = 0;
						j++;
					}
					else if(getTile(fgTiles, j * width + i-1) == MOUNTAIN &&
							getTile(fgTiles, (j-1) * width + i) != MOUNTAIN &&
							getTile(fgTiles, (j+1) * width + i) != MOUNTAIN &&
							getTile(fgTiles, j * width + i+1) != MOUNTAIN) {
						fgTiles[index] = 0;
						i--;
					}
					else if(getTile(fgTiles, j * width + i+1) == MOUNTAIN &&
							getTile(fgTiles, (j-1) * width + i) != MOUNTAIN &&
							getTile(fgTiles, (j+1) * width + i) != MOUNTAIN &&
							getTile(fgTiles, j * width + i-1) != MOUNTAIN) {
						fgTiles[index] = 0;
						i++;
					}
					else if(getTile(fgTiles, j * width + i+1) != MOUNTAIN &&
							getTile(fgTiles, (j-1) * width + i) != MOUNTAIN &&
							getTile(fgTiles, (j+1) * width + i) != MOUNTAIN &&
							getTile(fgTiles, j * width + i-1) != MOUNTAIN) {
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
		bg = new BackgroundLayer(width, height, bgTiles, new byte[width*height]);
		fg = new ForegroundLayer(width, height, fgTiles, new byte[width*height]);
		
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
