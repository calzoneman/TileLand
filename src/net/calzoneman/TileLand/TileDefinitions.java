package net.calzoneman.TileLand;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import net.calzoneman.TileLand.Tile.TileProperties;

public class TileDefinitions {
	private static HashMap<Integer, Tile> bgdefs;
	private static HashMap<Integer, Tile> fgdefs;
	
	public static Tile DEFAULTBG;
	public static Tile DEFAULTFG;
	public static final Tile NULLBGTILE = new Tile((short)-1, "NULL TILE", null, false, TileProperties.SOLID);
	public static final Tile NULLFGTILE = new Tile((short)-1, "NULL FG TILE", null, true);
	
	static {
		BufferedImage textures = null;
		bgdefs = new HashMap<Integer, Tile>();
		fgdefs = new HashMap<Integer, Tile>();
		try {	
			textures = ImageIO.read(new File("textures.png"));
		}
		catch(IOException ex) {
			System.err.println("Unable to load textures");
		}
		if(textures != null) {
			// Format: defs.put(id, new Tile(id, name, texture, isForeground));
			bgdefs.put(BgTileTypes.GRASS1.getIntId(), new Tile(BgTileTypes.GRASS1.getId(), "Grass1", textures.getSubimage(64, 0, 32, 32), false));
			bgdefs.put(BgTileTypes.GRASS2.getIntId(), new Tile(BgTileTypes.GRASS2.getId(), "Grass2", textures.getSubimage(96, 0, 32, 32), false));
			bgdefs.put(BgTileTypes.GRASS3.getIntId(), new Tile(BgTileTypes.GRASS3.getId(), "Grass3", textures.getSubimage(128, 0, 32, 32), false));
			bgdefs.put(BgTileTypes.GRASS4.getIntId(), new Tile(BgTileTypes.GRASS4.getId(), "Grass4", textures.getSubimage(160, 0, 32, 32), false));
			
			fgdefs.put(FgTileTypes.TREE1.getIntId(), new Tile(FgTileTypes.TREE1.getId(), "Tree1" , textures.getSubimage(32, 128, 32, 32), true, TileProperties.SOLID));
			fgdefs.put(FgTileTypes.TREE2.getIntId(), new Tile(FgTileTypes.TREE2.getId(), "Tree2" , textures.getSubimage(64, 128, 32, 32), true, TileProperties.SOLID));
			fgdefs.put(FgTileTypes.SIGN1.getIntId(), new Tile(FgTileTypes.SIGN1.getId(), "Sign1", textures.getSubimage(128, 128, 32, 32), true, TileProperties.SOLID));
			fgdefs.put(FgTileTypes.SIGN2.getIntId(), new Tile(FgTileTypes.SIGN2.getId(), "Sign2", textures.getSubimage(160, 128, 32, 32), true, TileProperties.SOLID));
			fgdefs.put(FgTileTypes.SIGN3.getIntId(), new Tile(FgTileTypes.SIGN3.getId(), "Sign3", textures.getSubimage(192, 128, 32, 32), true, TileProperties.SOLID));
			fgdefs.put(FgTileTypes.ROCK1.getIntId(), new Tile(FgTileTypes.ROCK1.getId(), "Rock1", textures.getSubimage(128, 160, 32, 32), true, TileProperties.SOLID));
			fgdefs.put(FgTileTypes.ROCK2.getIntId(), new Tile(FgTileTypes.ROCK2.getId(), "Rock2", textures.getSubimage(192, 160, 32, 32), true, TileProperties.SOLID));
			
			DEFAULTBG = bgdefs.get(1);
			DEFAULTFG = NULLFGTILE;
		}
	}
	
	public static Tile getFg(int id) {
		if (fgdefs.containsKey(id)) {
			return fgdefs.get(id);
		}
		else {
			return NULLFGTILE;
		}
	}
	
	public static Tile getBg(int id) {
		if (bgdefs.containsKey(id)) {
			return bgdefs.get(id);
		}
		else {
			return NULLBGTILE;
		}
	}
	
	static int b = 0;
	public enum BgTileTypes {
		GRASS1(b++),
		GRASS2(b++),
		GRASS3(b++),
		GRASS4(b++);
		
		private final int id;
		
		BgTileTypes(int id) {
			this.id = id;
		}
		
		public int getIntId() {
			return id;
		}
		
		public short getId() {
			return (short)id;
		}
	}
	
	static int f = 0;
	public enum FgTileTypes {
		TREE1(f++),
		TREE2(f++),
		SIGN1(f++),
		SIGN2(f++),
		SIGN3(f++),
		ROCK1(f++),
		ROCK2(f++);
		
		
		private final int id;
		
		FgTileTypes(int id) {
			this.id = id;
		}
		
		public int getIntId() {
			return id;
		}
		
		public short getId() {
			return (short)id;
		}
	}
}
