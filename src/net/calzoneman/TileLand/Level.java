package net.calzoneman.TileLand;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Level {
	public static final int HEADER_SIZE = 4;
	public static final int TILESIZE = 32;
	
	private Random rand;
	
	private int width = 0;
	private int height = 0;
	
	private Point spawnpoint;
	
	private short[] bgTiles;
	private short[] fgTiles;
	
	public String name = "untitled level";
	
	public Level() {
		rand = new Random();
	}
	
	public Level(int width, int height) {
		this();
		this.width = width;
		this.height = height;
		this.generate(width, height);
	}
	
	public Level(int width, int height, String name) {
		this(width, height);
		this.name = name;
	}
	
	public Level(String fname) {
		this.load(fname);
	}

	public void save() {
		
	}
	
	public void load(String filename) {
		
	}
	
	// Tile getters/setters

	
	// Background
	public short getBgId(int x, int y) {
		if(x < 0 || x >= width || y < 0 || y >= height) {
			return TileDefinitions.NULLBGTILE.getId();
		}
		else {
			return bgTiles[y * width + x];
		}
	}
	
	public Tile getBg(int x, int y) {
		return TileDefinitions.getBg(getBgId(x, y));
	}
	
	public boolean setBgId(int x, int y, short id) {
		if(x < 0 || x >= width || y < 0 || y >= height) {
			return false;
		}
		bgTiles[y * width + x] = id;
		return true;
	}
	
	public boolean setBg(int x, int y, Tile t) {
		return setBgId(x, y, t.getId());
	}
	
	// Foreground
	public short getFgId(int x, int y) {
		if(x < 0 || x >= width || y < 0 || y >= height) {
			return TileDefinitions.NULLFGTILE.getId();
		}
		return fgTiles[y * width + x];
	}
	
	public Tile getFg(int x, int y) {
		return TileDefinitions.getFg(getFgId(x, y));
	}
	
	public boolean setFgId(int x, int y, short id) {
		if(x < 0 || x >= width || y < 0 || y >= height) {
			return false;
		}
		fgTiles[y * width + x] = id;
		return true;
	}
	
	public boolean setFg(int x, int y, Tile t) {
		return setFgId(x, y, t.getId());
	}
	
	// Rendering
	public void render(Graphics g, int offX, int offY, int maxW, int maxH) {
		Graphics2D g2d = (Graphics2D)g;
		for(int i = offX; i < width && i < maxW; i++) {
			for(int j = offY; j < height && j < maxH; j++) {
				Tile bg = getBg(i, j);
				Tile fg = getFg(i, j);
				if(bg != null) {
					g2d.drawImage(bg.getTexture(), null, (i - offX) * TILESIZE, (j - offY) * TILESIZE);
				}
				if(fg != null) {
					g2d.drawImage(fg.getTexture(), null, (i - offX) * TILESIZE, (j - offY) * TILESIZE);
				}
			}
		}
	}
	
	public void render(Graphics g) {
		render(g, 0, 0, width, height);
	}
	
	// Generation
	public void generate(int width, int height) {
		this.bgTiles = new short[width * height];
		this.fgTiles = new short[width * height];
		
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++) {
				int grass = rand.nextInt(100);
				if(grass <  5) {
					bgTiles[j * width + i] = TileTypes.BG_GRASS2;
				}
				else if(grass < 10) {
					bgTiles[j * width + i] = TileTypes.BG_GRASS3;
				}
				else if(grass < 20) {
					bgTiles[j * width + i] = TileTypes.BG_GRASS4;
				}
				else {
					bgTiles[j * width + i] = TileDefinitions.DEFAULTBG.getId();
				}
			}
		}
		
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++) {
				double fgstuff = rand.nextDouble();
				if(fgstuff < 0.05) {
					fgTiles[j * width + i] = TileTypes.FG_TREE1;
				}
				else if(fgstuff < 0.07) {
					fgTiles[j * width + i] = TileTypes.FG_TREE2;
				}
				else if(fgstuff < 0.071) {
					fgTiles[j *  width + i] = TileTypes.FG_ROCK2;
				}
				else {
					fgTiles[j * width + i] = TileDefinitions.DEFAULTFG.getId();
				}
			}
		}
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public Point getSpawnpoint() {
		return spawnpoint;
	}

	public void setSpawnpoint(Point spawnpoint) {
		this.spawnpoint = spawnpoint;
	}
	
	
	
}
