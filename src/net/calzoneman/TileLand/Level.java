package net.calzoneman.TileLand;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
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
	
	private boolean needsRedraw = true;
	private ArrayList<TileChange> fgChanges;
	private ArrayList<TileChange> bgChanges;
	private BufferedImage drawn;
	
	public Level() {
		rand = new Random();
		fgChanges = new ArrayList<TileChange>();
		bgChanges = new ArrayList<TileChange>();
	}
	
	public Level(int width, int height) {
		this();
		this.width = width;
		this.height = height;
		this.generate(width, height);
		GraphicsConfiguration config = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
		drawn = config.createCompatibleImage(width * TILESIZE, height * TILESIZE, Transparency.TRANSLUCENT);
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
		bgChanges.add(new TileChange(x, y, id, false));
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
		fgChanges.add(new TileChange(x, y, id, true));
		return true;
	}
	
	public boolean setFg(int x, int y, Tile t) {
		return setFgId(x, y, t.getId());
	}
	
	// Rendering
	public void render(Graphics g, int offX, int offY, int maxW, int maxH) {
		if(needsRedraw) {
			redraw(g, offX, offY, maxW, maxH);
		}
		else {
			update(g, offX, offY, maxW, maxH);
		}
		((Graphics2D)g).drawImage(drawn, null, 0, 0);//offX * TILESIZE, offY * TILESIZE);
	}
	public void redraw(Graphics g, int offX, int offY, int maxW, int maxH) {
		Graphics2D g2d = (Graphics2D)drawn.getGraphics();
		g2d.fillRect(0, 0, width * TILESIZE, height * TILESIZE);
		for(int i = offX; i < width && i < maxW + offX; i++) {
			for(int j = offY; j < height && j < maxH + offY; j++) {
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
		needsRedraw = false;
	}
	
	public void update(Graphics g, int offX, int offY, int maxW, int maxH) {
		Graphics2D g2d = (Graphics2D)drawn.getGraphics();
		for(TileChange change : bgChanges) {
			if(change.x >= offX && change.x < maxW + offX && change.y >= offY && change.y < maxH + offY) {
				Tile bg = getBg(change.x, change.y);
				Tile fg = getFg(change.x, change.y);
				if(bg != null && fg != null) {
					g2d.drawImage(bg.getTexture(), null, (change.x - offX) * TILESIZE, (change.y - offY) * TILESIZE);
					g2d.drawImage(fg.getTexture(), null, (change.x - offX) * TILESIZE, (change.y - offY) * TILESIZE);
				}
			}
		}
		bgChanges.clear();
		for(TileChange change : fgChanges) {
			if(change.x >= offX && change.x < maxW + offX && change.y >= offY && change.y < maxH + offY) {
				Tile bg = getBg(change.x, change.y);
				Tile fg = getFg(change.x, change.y);
				if(fg != null && bg != null) {
					g2d.drawImage(bg.getTexture(), null, (change.x - offX) * TILESIZE, (change.y - offY) * TILESIZE);
					g2d.drawImage(fg.getTexture(), null, (change.x - offX) * TILESIZE, (change.y - offY) * TILESIZE);
				}
			}
		}
		fgChanges.clear();
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

	public void setNeedsRedraw(boolean needsRedraw) {
		this.needsRedraw = needsRedraw;
	}

	public boolean getNeedsRedraw() {
		return needsRedraw;
	}
	
	class TileChange {
		public int x;
		public int y;
		public short id;
		public boolean fg;
		
		public TileChange(int x, int y, short id, boolean fg) {
			this.x = x;
			this.y = y;
			this.id = id;
			this.fg = fg;
		}
	}
	
}
