package net.calzoneman.TileLand;

import java.awt.Graphics2D;
import java.awt.GraphicsConfigTemplate;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
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
	public static BufferedImage VOIDTEXTURE;
	
	public static void init() {
		BufferedImage textures = null;
		bgdefs = new HashMap<Short, Tile>();
		fgdefs = new HashMap<Short, Tile>();
		try {
			System.out.println(new File(".").getAbsolutePath());
			BufferedImage raw = ImageIO.read(new File("res/textures.png"));
			// Convert the image to an efficient format
		    GraphicsConfiguration config = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
			textures = config.createCompatibleImage(raw.getWidth(), raw.getHeight(), Transparency.TRANSLUCENT);
			Graphics2D g2d = (Graphics2D)textures.getGraphics();
			g2d.drawImage(raw, null, 0, 0);
			g2d.dispose();
		}
		catch(IOException ex) {
			System.err.println("Unable to load textures");
		}
		if(textures != null) {
			// Format: defs.put(id, new Tile(id, name, texture, isForeground));
			VOIDTEXTURE = textures.getSubimage(128, 2528, 32, 32);
			bgdefs.put(TileTypes.BG_GRASS1, new Tile(TileTypes.BG_GRASS1, "Grass1", textures.getSubimage(64, 0, 32, 32), false));
			bgdefs.put(TileTypes.BG_GRASS2, new Tile(TileTypes.BG_GRASS2, "Grass2", textures.getSubimage(96, 0, 32, 32), false));
			bgdefs.put(TileTypes.BG_GRASS3, new Tile(TileTypes.BG_GRASS3, "Grass3", textures.getSubimage(128, 0, 32, 32), false));
			bgdefs.put(TileTypes.BG_GRASS4, new Tile(TileTypes.BG_GRASS4, "Grass4", textures.getSubimage(160, 0, 32, 32), false));
			bgdefs.put(TileTypes.BG_SAND_TLCORNER, new Tile(TileTypes.BG_SAND_TLCORNER, "SandTopLeftCorner", textures.getSubimage(96, 3264, 32, 32), false));
			bgdefs.put(TileTypes.BG_SAND_TMIDDLE, new Tile(TileTypes.BG_SAND_TMIDDLE, "SandTopMiddle", textures.getSubimage(128, 3264, 32, 32), false));
			bgdefs.put(TileTypes.BG_SAND_TRCORNER, new Tile(TileTypes.BG_SAND_TRCORNER, "SandTopRightCorner", textures.getSubimage(160, 3264, 32, 32), false));
			bgdefs.put(TileTypes.BG_SAND_MLEFT, new Tile(TileTypes.BG_SAND_MLEFT, "SandMiddleLeft", textures.getSubimage(96, 3296, 32, 32), false));
			bgdefs.put(TileTypes.BG_SAND_MMIDDLE, new Tile(TileTypes.BG_SAND_MMIDDLE, "SandMiddleMiddle", textures.getSubimage(128, 3296, 32, 32), false));
			bgdefs.put(TileTypes.BG_SAND_MRIGHT, new Tile(TileTypes.BG_SAND_MRIGHT, "SandMiddleRight", textures.getSubimage(160, 3296, 32, 32), false));
			bgdefs.put(TileTypes.BG_SAND_BLCORNER, new Tile(TileTypes.BG_SAND_BLCORNER, "SandBottomLeftCorner", textures.getSubimage(96, 3328, 32, 32), false));
			bgdefs.put(TileTypes.BG_SAND_BMIDDLE, new Tile(TileTypes.BG_SAND_BMIDDLE, "SandBottomMiddle", textures.getSubimage(128, 3328, 32, 32), false));
			bgdefs.put(TileTypes.BG_SAND_BRCORNER, new Tile(TileTypes.BG_SAND_BRCORNER, "SandBottomRightCorner", textures.getSubimage(160, 3328, 32, 32), false));
			bgdefs.put(TileTypes.BG_GRASS_WTF, new Tile(TileTypes.BG_GRASS_WTF, "GrassWtf", textures.getSubimage(32, 32, 32, 32), false));
			bgdefs.put(TileTypes.BG_LAKE_TLCORNER, new Tile(TileTypes.BG_LAKE_TLCORNER, "LAKETopLeftCorner", textures.getSubimage(96, 3072, 32, 32), false, TileProperties.LIQUID));
			bgdefs.put(TileTypes.BG_LAKE_TMIDDLE, new Tile(TileTypes.BG_LAKE_TMIDDLE, "LAKETopMiddle", textures.getSubimage(128, 3072, 32, 32), false, TileProperties.LIQUID));
			bgdefs.put(TileTypes.BG_LAKE_TRCORNER, new Tile(TileTypes.BG_LAKE_TRCORNER, "LAKETopRightCorner", textures.getSubimage(160, 3072, 32, 32), false, TileProperties.LIQUID));
			bgdefs.put(TileTypes.BG_LAKE_MLEFT, new Tile(TileTypes.BG_LAKE_MLEFT, "LAKEMiddleLeft", textures.getSubimage(96, 3104, 32, 32), false, TileProperties.LIQUID));
			bgdefs.put(TileTypes.BG_LAKE_MMIDDLE, new Tile(TileTypes.BG_LAKE_MMIDDLE, "LAKEMiddleMiddle", textures.getSubimage(128, 3104, 32, 32), false, TileProperties.LIQUID));
			bgdefs.put(TileTypes.BG_LAKE_MRIGHT, new Tile(TileTypes.BG_LAKE_MRIGHT, "LAKEMiddleRight", textures.getSubimage(160, 3104, 32, 32), false, TileProperties.LIQUID));
			bgdefs.put(TileTypes.BG_LAKE_BLCORNER, new Tile(TileTypes.BG_LAKE_BLCORNER, "LAKEBottomLeftCorner", textures.getSubimage(96, 3136, 32, 32), false, TileProperties.LIQUID));
			bgdefs.put(TileTypes.BG_LAKE_BMIDDLE, new Tile(TileTypes.BG_LAKE_BMIDDLE, "LAKEBottomMiddle", textures.getSubimage(128, 3136, 32, 32), false, TileProperties.LIQUID));
			bgdefs.put(TileTypes.BG_LAKE_BRCORNER, new Tile(TileTypes.BG_LAKE_BRCORNER, "LAKEBottomRightCorner", textures.getSubimage(160, 3136, 32, 32), false, TileProperties.LIQUID));

			
			fgdefs.put(TileTypes.FG_TREE1, new Tile(TileTypes.FG_TREE1, "Tree1" , textures.getSubimage(32, 128, 32, 32), true, TileProperties.SOLID));
			fgdefs.put(TileTypes.FG_TREE2, new Tile(TileTypes.FG_TREE2, "Tree2" , textures.getSubimage(64, 128, 32, 32), true, TileProperties.SOLID));
			fgdefs.put(TileTypes.FG_SIGN1, new Tile(TileTypes.FG_SIGN1, "Sign1", textures.getSubimage(128, 128, 32, 32), true, TileProperties.SOLID));
			fgdefs.put(TileTypes.FG_SIGN2, new Tile(TileTypes.FG_SIGN2, "Sign2", textures.getSubimage(160, 128, 32, 32), true, TileProperties.SOLID));
			fgdefs.put(TileTypes.FG_SIGN3, new Tile(TileTypes.FG_SIGN3, "Sign3", textures.getSubimage(192, 128, 32, 32), true, TileProperties.SOLID));
			fgdefs.put(TileTypes.FG_ROCK1, new Tile(TileTypes.FG_ROCK1, "Rock1", textures.getSubimage(128, 160, 32, 32), true, TileProperties.SOLID));
			fgdefs.put(TileTypes.FG_ROCK2, new Tile(TileTypes.FG_ROCK2, "Rock2", textures.getSubimage(192, 160, 32, 32), true, TileProperties.SOLID));
			fgdefs.put(TileTypes.FG_BUSH1, new Tile(TileTypes.FG_BUSH1, "Bush1", textures.getSubimage(224, 0, 32, 32), true));
			fgdefs.put(TileTypes.FG_BUSH1DEAD, new Tile(TileTypes.FG_BUSH1DEAD, "Bush1dead", textures.getSubimage(0, 64, 32, 32), true));
			fgdefs.put(TileTypes.FG_BUSH2, new Tile(TileTypes.FG_BUSH2, "Bush2", textures.getSubimage(96, 128, 32, 32), true, TileProperties.SOLID));
			fgdefs.put(TileTypes.FG_ROCK3, new Tile(TileTypes.FG_ROCK3, "Rock3", textures.getSubimage(32, 288, 32, 32), true, TileProperties.SOLID));
			fgdefs.put(TileTypes.FG_EGGTHING, new Tile(TileTypes.FG_EGGTHING, "Eggthing", textures.getSubimage(224, 320, 32, 32), true, TileProperties.SOLID));
			fgdefs.put(TileTypes.FG_TREE3BOTTOM, new Tile(TileTypes.FG_TREE3BOTTOM, "Tree3bottom", textures.getSubimage(192, 608, 32, 32), true, TileProperties.SOLID));
			fgdefs.put(TileTypes.FG_TREE3TOP, new Tile(TileTypes.FG_TREE3TOP, "Tree3top", textures.getSubimage(192, 576, 32, 32), true));
			
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
