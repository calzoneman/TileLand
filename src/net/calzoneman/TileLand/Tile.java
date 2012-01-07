package net.calzoneman.TileLand;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

public class Tile {
	private String name;
	private short id;
	private BufferedImage texture;
	private boolean foreground;
	private int properties;
	
	public Tile(short id, boolean fg) {
		this.id = id;
		this.name = "UNNAMED TILE";
		this.texture = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		this.foreground = fg;
	}
	
	public Tile(short id, BufferedImage texture, boolean fg) {
		this.id = id;
		this.name = "UNNAMED TILE";
		this.texture = texture;
		this.foreground = fg;
	}
	
	public Tile(short id, String name, BufferedImage texture, boolean fg) {
		this.id = id;
		this.name = name;
		this.texture = texture;
		this.foreground = fg;
	}
	
	public Tile(short id, String name, BufferedImage texture, boolean fg, int properties) {
		this.id = id;
		this.name = name;
		this.texture = texture;
		this.foreground = fg;
		this.properties = properties;
	}

	public String getName() {
		return name;
	}

	public short getId() {
		return id;
	}

	public BufferedImage getTexture() {
		return texture;
	}
	
	public BufferedImage getTextureClone() {
		BufferedImage clone = new BufferedImage(texture.getWidth(), texture.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = (Graphics2D)clone.getGraphics();
		g2d.drawImage(texture, null, 0, 0);
		return clone;
		/*ColorModel cm = texture.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = texture.copyData(null);
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);*/
	}
	
	public boolean isForeground() {
		return foreground;
	}
	
	public boolean isSolid() {
		return (this.properties & TileProperties.SOLID) > 0;
	}
	
	public boolean isLiquid() { 
		return (this.properties & TileProperties.LIQUID) > 0; 
	}
	
	public class TileProperties {
		public static final int SOLID = 0x01;
		public static final int LIQUID = 0x02;
	}


}
