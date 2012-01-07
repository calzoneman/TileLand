package net.calzoneman.TileLand;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import net.calzoneman.TileLand.Tile.TileProperties;

public class TileDefinitions {
	private static HashMap<Short, Tile> bgdefs;
	private static HashMap<Short, Tile> fgdefs;
	
	public static Tile DEFAULTBG;
	public static Tile DEFAULTFG;
	public static final Tile NULLBGTILE = new Tile((short)-1, "NULL TILE", null, false, TileProperties.SOLID);
	public static final Tile NULLFGTILE = new Tile((short)-1, "NULL FG TILE", null, true);
	
	static {
		BufferedImage textures = null;
		bgdefs = new HashMap<Short, Tile>();
		fgdefs = new HashMap<Short, Tile>();
		try {
			System.out.println(new File(".").getAbsolutePath());
			textures = ImageIO.read(new File("res/textures.png"));
		}
		catch(IOException ex) {
			System.err.println("Unable to load textures");
		}
		if(textures != null) {
			// Format: defs.put(id, new Tile(id, name, texture, isForeground));
			bgdefs.put(TileTypes.BG_GRASS1, new Tile(TileTypes.BG_GRASS1, "Grass1", textures.getSubimage(64, 0, 32, 32), false));
			bgdefs.put(TileTypes.BG_GRASS2, new Tile(TileTypes.BG_GRASS2, "Grass2", textures.getSubimage(96, 0, 32, 32), false));
			bgdefs.put(TileTypes.BG_GRASS3, new Tile(TileTypes.BG_GRASS3, "Grass3", textures.getSubimage(128, 0, 32, 32), false));
			bgdefs.put(TileTypes.BG_GRASS4, new Tile(TileTypes.BG_GRASS4, "Grass4", textures.getSubimage(160, 0, 32, 32), false));
			
			fgdefs.put(TileTypes.FG_TREE1, new Tile(TileTypes.FG_TREE1, "Tree1" , textures.getSubimage(32, 128, 32, 32), true, TileProperties.SOLID));
			fgdefs.put(TileTypes.FG_TREE2, new Tile(TileTypes.FG_TREE2, "Tree2" , textures.getSubimage(64, 128, 32, 32), true, TileProperties.SOLID));
			fgdefs.put(TileTypes.FG_SIGN1, new Tile(TileTypes.FG_SIGN1, "Sign1", textures.getSubimage(128, 128, 32, 32), true, TileProperties.SOLID));
			fgdefs.put(TileTypes.FG_SIGN2, new Tile(TileTypes.FG_SIGN2, "Sign2", textures.getSubimage(160, 128, 32, 32), true, TileProperties.SOLID));
			fgdefs.put(TileTypes.FG_SIGN3, new Tile(TileTypes.FG_SIGN3, "Sign3", textures.getSubimage(192, 128, 32, 32), true, TileProperties.SOLID));
			fgdefs.put(TileTypes.FG_ROCK1, new Tile(TileTypes.FG_ROCK1, "Rock1", textures.getSubimage(128, 160, 32, 32), true, TileProperties.SOLID));
			fgdefs.put(TileTypes.FG_ROCK2, new Tile(TileTypes.FG_ROCK2, "Rock2", textures.getSubimage(192, 160, 32, 32), true, TileProperties.SOLID));
			
			DEFAULTBG = bgdefs.get(TileTypes.BG_GRASS1);
			DEFAULTFG = NULLFGTILE;
		}
	}
	
	public static Tile getFg(int id) {
		if (fgdefs.containsKey((short)id)) {
			return fgdefs.get((short)id);
		}
		else {
			return NULLFGTILE;
		}
	}
	
	public static Tile getBg(int id) {
		if (bgdefs.containsKey((short)id)) {
			return bgdefs.get((short)id);
		}
		else {
			return NULLBGTILE;
		}
	}
	
}
