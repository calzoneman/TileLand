package net.calzoneman.TileLand.gfx;

import net.calzoneman.TileLand.TileLand;
import net.calzoneman.TileLand.level.Level;
import net.calzoneman.TileLand.level.Location;
import net.calzoneman.TileLand.player.Player;
import net.calzoneman.TileLand.tile.Tile;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.opengl.Texture;

public class Renderer {
	/** The font to be used for text drawing */
	private static UnicodeFont font;
	/** FPS counter variables */
	private static long lastFPSMeasureTime;
	private static long currentFrames;
	private static int fps;
	/** Colors for Mouse overlay */
	private static Color transparent_red = new Color(255, 0, 0, 130);
	private static Color transparent_green = new Color(0, 255, 0, 130);
	
	/**
	 * Initializes the Renderer, loads appropriate resources, initializes GL
	 */
	public static boolean init() {
		if(!initGL(640, 480))
			return false;
		currentFrames = 0;
		fps = 0;
		lastFPSMeasureTime = System.nanoTime() - 1000000000;
		return true;
	}
	
	/**
	 * Internal function to handle GL initialization
	 * @param width The desired screen width
	 * @param height The desired screen height
	 */
	private static boolean initGL(int width, int height) {
		try {
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.setTitle("TileLand - LWJGL Edition");
			Display.create();
			Display.setVSyncEnabled(true);
		}
		catch(Exception ex) {
			System.out.println("Error initializing GL: ");
			ex.printStackTrace();
			return false;
		}
		
		glEnable(GL_TEXTURE_2D);
		// Set the background color to black
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		// Enable Alpha
		glEnable(GL_SRC_ALPHA);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		// Setup the view
		glViewport(0, 0, width, height);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, width, height, 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);
		
		return true;
	}
	
	/**
	 * Renders the game
	 * @param ply The player object for which the game is being rendered
	 */
	public static void render(Player ply) {
		if(font == null)
			font = TileLand.getTextureManager().getDefaultFont();
		if(System.nanoTime() >= lastFPSMeasureTime + 1000000000) {
			fps = (int) currentFrames;
			currentFrames = 0;
			lastFPSMeasureTime = System.nanoTime();
		}
		
		Level level = ply.getLevel();
		// Calculate at what offset to begin rendering the level
		Location renderStart = new Location(
				ply.getPosition().x - Display.getWidth() / Level.TILESIZE / 2,
				ply.getPosition().y - Display.getHeight() / Level.TILESIZE / 2);
		// Render the background
		if(level != null) {
			renderLevel(level, renderStart.x, renderStart.y, Display.getWidth() / Level.TILESIZE, Display.getHeight() / Level.TILESIZE, false);
		}
		// Render the player sprite
		ply.render((ply.getPosition().x - renderStart.x) * Level.TILESIZE, (ply.getPosition().y - renderStart.y) * Level.TILESIZE);
		//renderTexture(ply.getSprite(), (ply.getPosition().x - renderStart.x) * Level.TILESIZE, (ply.getPosition().y - renderStart.y) * Level.TILESIZE);
		// Render the foreground
		if(level != null) {
			renderLevel(level, renderStart.x, renderStart.y, Display.getWidth() / Level.TILESIZE, Display.getHeight() / Level.TILESIZE, true);
		}
		// Render the mouse hover
		Tile current = null;
		Color col = transparent_green;
		int tx = Mouse.getX() / Level.TILESIZE + renderStart.x;
		int ty = (Display.getHeight() - Mouse.getY()) / Level.TILESIZE + renderStart.y;
		if((tx == ply.getPosition().x && ty == ply.getPosition().y && ply.isEditingFg()) || tx < 0 || tx >= level.getWidth() || ty < 0 || ty >= level.getHeight())
			col = transparent_red;
		current = ply.getCurrentTile();
		col.bind();
		renderMouse(current);
		
		// Render the player's nametag
		ply.renderNameCentered();
		// Render FPS
		glEnable(GL_BLEND);
		font.drawString(0, 0, "FPS: " + fps);
		glDisable(GL_BLEND);
		currentFrames++;
	}
	
	/**
	 * Renders a Level object from the specified offset within the Level and filling the specified width and height
	 * @param lvl The Level to be rendered
	 * @param offX The x offset at which to begin rendering the level
	 * @param offY The y offset at which to begin rendering the level
	 * @param maxWidth The width to render (in tiles)
	 * @param maxHeight The height to render (in tiles)
	 * @param foreground Whether to render the foreground or the background layer
	 */
	public static void renderLevel(Level lvl, int offX, int offY, int maxWidth, int maxHeight, boolean foreground) {		
		for(int i = offX; i < offX + maxWidth; i++) {
			for(int j = offY; j < offY + maxHeight; j++) {
				if(foreground) {
					Tile fg = lvl.getFg(i, j);
					if(fg != null && fg.getId() != -1) {
						if(fg.hasData())
							fg.render((i - offX) * Level.TILESIZE, (j - offY) * Level.TILESIZE, lvl.getFgData(i,  j));
						else
							fg.render((i - offX) * Level.TILESIZE, (j - offY) * Level.TILESIZE);
					}
				}
				else {
					Tile bg = lvl.getBg(i, j);
					if(bg != null) {
						if(bg.hasData()) {
							bg.render((i - offX) * Level.TILESIZE, (j - offY) * Level.TILESIZE, lvl.getBgData(i,  j));
						}
						else
							bg.render((i - offX) * Level.TILESIZE, (j - offY) * Level.TILESIZE);
					}
				}
				
			}
		}
	}
	
	/**
	 * Renders the mouse hover
	 */
	public static void renderMouse(Tile currentTile) {
		if(currentTile == null)
			return;
		int mx = Mouse.getX();
		int my = Mouse.getY();
		int tx = mx / Level.TILESIZE;
		int ty = (Display.getHeight() - my) / Level.TILESIZE;
		// Draw overlay
		currentTile.render(tx * Level.TILESIZE, ty * Level.TILESIZE);
		// Draw border
		Color.black.bind();
		renderRectangle(new Rectangle(tx * Level.TILESIZE, ty * Level.TILESIZE, Level.TILESIZE, Level.TILESIZE), false);
	}
	
	/**
	 * Renders a Texture at the specified coordinates
	 * @param x The x-coordinate at which to render the texture
	 * @param y The y-coordinate at which to render the texture
	 */
	@Deprecated
	public static void renderTexture(Texture tex, int x, int y) {
		tex.bind();
		glBegin(GL_QUADS);
			glTexCoord2f(0, 0);
			glVertex2f(x, y);
			glTexCoord2f(1, 0);
			glVertex2f(x+tex.getTextureWidth(), y);
			glTexCoord2f(1, 1);
			glVertex2f(x+tex.getTextureWidth(), y+tex.getTextureHeight());
			glTexCoord2f(0, 1);
			glVertex2f(x, y+tex.getTextureHeight());
		glEnd();
	}
	
	/**
	 * Renders a subrectangle of a Texture
	 * @param tex The texture to bind
	 * @param rect The Rectangle to draw
	 * @param x The x coordinate at which to draw the rectangle
	 * @param y The y coordinate at which to draw the rectangle
	 */
	@Deprecated
	public static void renderTextureSubrectangle(Texture tex, Rectangle rect, int x, int y) {
		int texWidth = tex.getTextureWidth();
		int texHeight = tex.getTextureHeight();
		float rectX = rect.getX();
		float rectY = rect.getY();
		float rectWidth = rect.getWidth();
		float rectHeight = rect.getHeight();
		tex.bind();
		glBegin(GL_QUADS);
			glTexCoord2f(rectX / texWidth, rectY / texHeight);
			glVertex2f(x, y);
			glTexCoord2f((rectX + rectWidth) / texWidth, rectY / texHeight);
			glVertex2f(x + rectWidth, y);
			glTexCoord2f((rectX + rectWidth) / texWidth, (rectY + rectHeight) / texHeight);
			glVertex2f(x + rectWidth, y + rectHeight);
			glTexCoord2f(rectX / texWidth, (rectY + rectHeight) / texHeight);
			glVertex2f(x, y + rectHeight);
		glEnd();
	}
	
	/**
	 * Renders a rectangle
	 * @param rect The rectangle to be rendered
	 * @param filled Whether the rectangle is filled or solid
	 */
	public static void renderRectangle(Rectangle rect, boolean filled) {
		if(filled)
			glBegin(GL_QUADS);
		else
			glBegin(GL_LINE_LOOP);
		glVertex2f(rect.getX(), rect.getY());
		glVertex2f(rect.getX() + rect.getWidth(), rect.getY());
		glVertex2f(rect.getX() + rect.getWidth(), rect.getY() + rect.getHeight());
		glVertex2f(rect.getX(), rect.getY() + rect.getHeight());
		glEnd();
	}
}
