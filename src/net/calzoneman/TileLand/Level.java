package net.calzoneman.TileLand;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.Transparency;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Level {
	public static final int HEADER_SIZE = 20;
	public static final int TILESIZE = 32;
	public static final int DRAWN_MAX_WIDTH = 2560;
	public static final int DRAWN_MAX_HEIGHT = 1600;
	
	private Random rand;
	
	private int width = 0;
	private int height = 0;
	
	private Point spawnpoint = new Point(0, 0);
	
	private short[] bgTiles;
	private short[] fgTiles;
	
	public String name = "save";
	
	private boolean needsRedraw = true;
	private ArrayList<TileChange> fgChanges;
	private ArrayList<TileChange> bgChanges;
	private BufferedImage drawn;
	
	public Level() {
		rand = new Random();
		fgChanges = new ArrayList<TileChange>();
		bgChanges = new ArrayList<TileChange>();
		spawnpoint = new Point(0, 0);
	}
	
	public Level(int width, int height) {
		this();
		this.width = width;
		this.height = height;
		this.generate(width, height);
		GraphicsConfiguration config = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
		int w = DRAWN_MAX_WIDTH; //(width * TILESIZE < DRAWN_MAX_WIDTH ? width * TILESIZE : DRAWN_MAX_WIDTH);
		int h = DRAWN_MAX_HEIGHT; //(height * TILESIZE < DRAWN_MAX_HEIGHT ? height * TILESIZE : DRAWN_MAX_HEIGHT);
		drawn = config.createCompatibleImage(w, h, Transparency.TRANSLUCENT);
	}
	
	public Level(int width, int height, String name) {
		this(width, height);
		this.name = name;
	}
	
	public Level(String fname) {
		this();
		this.load(fname);
		GraphicsConfiguration config = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
		int w = (width * TILESIZE < DRAWN_MAX_WIDTH ? width * TILESIZE : DRAWN_MAX_WIDTH);
		int h = (height * TILESIZE < DRAWN_MAX_HEIGHT ? height * TILESIZE : DRAWN_MAX_HEIGHT);
		drawn = config.createCompatibleImage(w, h, Transparency.TRANSLUCENT);
	}

	public void save() {
		File savefile = new File("saves/" + name + ".tl");
		try {
			if(!(new File("saves").exists())) new File("saves").mkdir();
			FileOutputStream fos = new FileOutputStream(savefile);
			ByteBuffer buf = ByteBuffer.allocate(HEADER_SIZE + width * height * 4);
			buf.putInt(0x4F07E6C8);
			buf.putInt(width);
			buf.putInt(height);
			buf.putInt(spawnpoint.x);
			buf.putInt(spawnpoint.y);
			for(int i = 0; i < width * height; i++) {
				buf.putShort(bgTiles[i]);
			}
			for(int i = 0; i < width * height; i++) {
				buf.putShort(fgTiles[i]);
			}
			fos.write(buf.array());
			fos.close();
			buf = null;
			System.gc();
		}
		catch(IOException ex) {
			System.err.println("Unable to save level!");
		}
		
	}
	
	public void load(String filename) {
		File savefile = new File("saves/" + filename);
		if(!savefile.exists()) {
			System.err.println("Savefile doesn't exist!");
			return;
		}
		try {
			FileInputStream fis = new FileInputStream(savefile);
			ByteBuffer hdrbuf = ByteBuffer.allocate(HEADER_SIZE);
			fis.getChannel().read(hdrbuf);
			hdrbuf.rewind();
			if(hdrbuf.getInt() != 0x4F07E6C8) {
				System.err.println("Wrong magic number in level!");
				return;
			}
			this.width = hdrbuf.getInt();
			this.height = hdrbuf.getInt();
			this.spawnpoint = new Point(hdrbuf.getInt(), hdrbuf.getInt());
			ByteBuffer databuf = ByteBuffer.allocate(width * height * 4);
			fis.getChannel().read(databuf);
			fis.close();
			databuf.rewind();
			
			ShortBuffer mapBuf = databuf.asShortBuffer();
			bgTiles = new short[width * height];
			fgTiles = new short[width * height];
			mapBuf.get(bgTiles);
			mapBuf.get(fgTiles);
			this.name = filename.substring(0, filename.indexOf(".tl"));
		}
		catch(IOException ex) {
			System.err.println("Unable to load level!");
		}
		catch(Exception ex) {
			System.err.println("Unable to load level!");
		}
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
		if(id == TileTypes.FG_TREE3BOTTOM) {
			if(setFgId(x, y-1, TileTypes.FG_TREE3TOP)) {
				fgTiles[y * width + x] = id;
				fgChanges.add(new TileChange(x, y, id, true));
				return true;
			}
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
		g2d.clearRect(0, 0, DRAWN_MAX_WIDTH, DRAWN_MAX_HEIGHT);
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
		
		this.spawnpoint = new Point(rand.nextInt(width), rand.nextInt(height));
		while(getFg(spawnpoint.x, spawnpoint.y) != TileDefinitions.NULLFGTILE) {
			this.spawnpoint = new Point(rand.nextInt(width), rand.nextInt(height));
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
		this.spawnpoint = new Point(spawnpoint.x, spawnpoint.y);
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
