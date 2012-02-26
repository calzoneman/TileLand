package net.calzoneman.TileLand.gfx;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import javax.imageio.ImageIO;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.util.BufferedImageUtil;

public class SpriteSheet {

	public final String texturePrefix = "" + new Random().nextLong();

	public final int tilesize;
	public final int width;
	public final int height;
	public final BufferedImage image;

	public SpriteSheet(BufferedImage img, int tsize) {
		image = img;
		width = img.getWidth() / tsize;
		height = img.getHeight() / tsize;
		tilesize = tsize;
	}

	public SpriteSheet(InputStream is, int tsize) throws IOException {
		image = loadImage(is);
		width = image.getWidth() / tsize;
		height = image.getHeight() / tsize;
		tilesize = tsize;
	}

	public SpriteSheet(String path, int tsize) throws IOException {
		image = loadImage(path);
		width = image.getWidth() / tsize;
		height = image.getHeight() / tsize;
		tilesize = tsize;
	}

	public BufferedImage getTileImage(int tile) {
		int tx = tile % width;
		int ty = tile / width;
		int px = tx * tilesize;
		int py = ty * tilesize;

		BufferedImage tileClone = new BufferedImage(tilesize, tilesize,
				image.getType());
		image.getSubimage(px, py, tilesize, tilesize).copyData(
				tileClone.getRaster());
		return tileClone;
	}
	
	public BufferedImage getTileImage(int tile, int[] colors) {
		BufferedImage tileClone = getTileImage(tile);
		int[] newpx = Colorizer.colorize(
				tileClone.getRGB(0, 0, tilesize, tilesize, null, 0, tilesize),
				colors);
		tileClone.setRGB(0, 0, tilesize, tilesize, newpx, 0, tilesize);
		return tileClone;
	}

	public BufferedImage getCustomTileImage(int x, int y, int w, int h) {

		BufferedImage tileClone = new BufferedImage(w * tilesize, h * tilesize,
				image.getType());
		image.getSubimage(x * tilesize, y * tilesize, w * tilesize, h * tilesize).copyData(
				tileClone.getRaster());
		return tileClone;
	}

	public BufferedImage getCustomTileImage(int x, int y, int w, int h, int[] colors) {
		BufferedImage tileClone = getCustomTileImage(x, y, w, h);
		int[] newpx = Colorizer.colorize(
				tileClone.getRGB(0, 0, w, h, null, 0, w),
				colors);
		tileClone.setRGB(0, 0, w, h, newpx, 0, w);
		return tileClone;
	}

	public Texture getTileTexture(int tile) throws IOException {
		return convertImageToTexture(texturePrefix + tile, getTileImage(tile));
	}

	public Texture getTileTexture(int tile, int[] colors) throws IOException {
		return convertImageToTexture(texturePrefix + tile,
				getTileImage(tile, colors));
	}
	
	public Texture getCustomTileTexture(int x, int y, int w, int h) throws IOException {
		return convertImageToTexture(texturePrefix + (y * this.width + x), getCustomTileImage(x, y, w, h));
	}
	
	public Texture getCustomTileTexture(int x, int y, int w, int h, int[] colors) throws IOException {
		return convertImageToTexture(texturePrefix + (y * this.width + x), getCustomTileImage(x, y, w, h, colors));
	}

	private Texture convertImageToTexture(String name, BufferedImage img)
			throws IOException {
		return BufferedImageUtil.getTexture(name, img);
	}

	public static BufferedImage loadImage(InputStream is) throws IOException {
		return ImageIO.read(is);
	}

	public static BufferedImage loadImage(String path) throws IOException {
		return ImageIO.read(new File(path));
	}
}
