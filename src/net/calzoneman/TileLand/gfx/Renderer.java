package net.calzoneman.TileLand.gfx;

import net.calzoneman.TileLand.TileLand;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.opengl.Texture;

public class Renderer {
	private static TilelandFont font;
	/** FPS counter variables */
	private static long lastFPSMeasureTime;
	private static long currentFrames;
	private static int fps;

	/**
	 * Initializes the Renderer, loads appropriate resources, initializes GL
	 */
	public static boolean init(int width, int height) {
		if (!initGL(width, height))
			return false;
		currentFrames = 0;
		fps = 0;
		lastFPSMeasureTime = System.nanoTime() - 1000000000;
		return true;
	}

	/**
	 * Internal function to handle GL initialization
	 * 
	 * @param width
	 *            The desired screen width
	 * @param height
	 *            The desired screen height
	 */
	private static boolean initGL(int width, int height) {
		try {
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.setTitle("TileLand " + TileLand.version);
			Display.create();
			Display.setVSyncEnabled(true);
		} catch (Exception ex) {
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

	public static boolean reInit(int width, int height) {
		try {
			Display.setDisplayMode(new DisplayMode(width, height));
		} catch (Exception ex) {
			System.out.println("Error initializing GL: ");
			ex.printStackTrace();
			return false;
		}

		glViewport(0, 0, width, height);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, width, height, 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);
		return true;
	}

	public static TilelandFont getFont() {
		return font;
	}

	public static void setFont(TilelandFont fnt) {
		font = fnt;
	}

	public static void renderFPS() {
		glEnable(GL_BLEND);
		font.drawString(0, 0, "FPS: " + fps);
		glDisable(GL_BLEND);
		currentFrames++;
		if (System.nanoTime() >= lastFPSMeasureTime + 1000000000) {
			fps = (int) currentFrames;
			currentFrames = 0;
			lastFPSMeasureTime = System.nanoTime();
		}
	}

	/**
	 * Renders a Texture at the specified coordinates
	 * 
	 * @param tex
	 *            The texture to be drawn
	 * @param x
	 *            The x-coordinate at which to render the texture
	 * @param y
	 *            The y-coordinate at which to render the texture
	 */
	public static void renderTexture(Texture tex, int x, int y) {
		if (tex == null)
			return;
		tex.bind();
		glEnable(GL_BLEND);
		glBegin(GL_QUADS);
		glTexCoord2d(0.0D, 0.0D);
		glVertex2i(x, y);
		glTexCoord2d(1.0D, 0.0D);
		glVertex2i(x + tex.getTextureWidth(), y);
		glTexCoord2d(1.0D, 1.0D);
		glVertex2i(x + tex.getTextureWidth(), y + tex.getTextureHeight());
		glTexCoord2d(0.0D, 1.0D);
		glVertex2i(x, y + tex.getTextureHeight());
		glEnd();
		glDisable(GL_BLEND);
	}

	public static void renderTexture(Texture tex, int x, int y, int w, int h) {
		if (tex == null)
			return;
		tex.bind();
		glEnable(GL_BLEND);
		glBegin(GL_QUADS);
		glTexCoord2d(0.0D, 0.0D);
		glVertex2i(x, y);
		glTexCoord2d(1.0D, 0.0D);
		glVertex2i(x + w, y);
		glTexCoord2d(1.0D, 1.0D);
		glVertex2i(x + w, y + h);
		glTexCoord2d(0.0D, 1.0D);
		glVertex2i(x, y + h);
		glEnd();
		glDisable(GL_BLEND);
	}

	/**
	 * Renders a subrectangle of a Texture
	 * 
	 * @param tex
	 *            The texture to bind
	 * @param rect
	 *            The Rectangle to draw
	 * @param x
	 *            The x coordinate at which to draw the rectangle
	 * @param y
	 *            The y coordinate at which to draw the rectangle
	 */
	public static void renderTextureSubrectangle(Texture tex, Rectangle rect,
			int x, int y) {
		if (tex == null)
			return;
		int texWidth = tex.getTextureWidth();
		int texHeight = tex.getTextureHeight();
		float rectX = rect.getX();
		float rectY = rect.getY();
		float rectWidth = rect.getWidth();
		float rectHeight = rect.getHeight();
		tex.bind();
		glEnable(GL_BLEND);
		glBegin(GL_QUADS);
		glTexCoord2f(rectX / texWidth, rectY / texHeight);
		glVertex2f(x, y);
		glTexCoord2f((rectX + rectWidth) / texWidth, rectY / texHeight);
		glVertex2f(x + rectWidth, y);
		glTexCoord2f((rectX + rectWidth) / texWidth, (rectY + rectHeight)
				/ texHeight);
		glVertex2f(x + rectWidth, y + rectHeight);
		glTexCoord2f(rectX / texWidth, (rectY + rectHeight) / texHeight);
		glVertex2f(x, y + rectHeight);
		glEnd();
		glDisable(GL_BLEND);
	}

	/**
	 * Renders a subrectangle of a Texture
	 * 
	 * @param tex
	 *            The texture to bind
	 * @param rect
	 *            The Rectangle to draw
	 * @param x
	 *            The x coordinate at which to draw the rectangle
	 * @param y
	 *            The y coordinate at which to draw the rectangle
	 */
	public static void renderTextureSubrectangle(Texture tex, Rectangle rect,
			int x, int y, int w, int h) {
		int texWidth = tex.getTextureWidth();
		int texHeight = tex.getTextureHeight();
		float rectX = rect.getX();
		float rectY = rect.getY();
		float rectWidth = rect.getWidth();
		float rectHeight = rect.getHeight();
		tex.bind();
		glEnable(GL_BLEND);
		glBegin(GL_QUADS);
		glTexCoord2f(rectX / texWidth, rectY / texHeight);
		glVertex2f(x, y);
		glTexCoord2f((rectX + rectWidth) / texWidth, rectY / texHeight);
		glVertex2f(x + w, y);
		glTexCoord2f((rectX + rectWidth) / texWidth, (rectY + rectHeight)
				/ texHeight);
		glVertex2f(x + w, y + h);
		glTexCoord2f(rectX / texWidth, (rectY + rectHeight) / texHeight);
		glVertex2f(x, y + h);
		glEnd();
		glDisable(GL_BLEND);
	}

	public static void renderFilledRect(int x, int y, int w, int h, Color col) {
		glDisable(GL_TEXTURE_2D);
		if (col.getAlpha() != 255)
			glEnable(GL_BLEND);
		col.bind();
		glBegin(GL_QUADS);
		glVertex2f(x, y);
		glVertex2f(x + w, y);
		glVertex2f(x + w, y + h);
		glVertex2f(x, y + h);
		glEnd();
		if (col.getAlpha() != 255)
			glDisable(GL_BLEND);
		glEnable(GL_TEXTURE_2D);
		// Reset the color to white
		glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
	}

	public static void renderRect(int x, int y, int w, int h, Color col) {
		glDisable(GL_TEXTURE_2D);
		if (col.getAlpha() != 255)
			glEnable(GL_BLEND);
		col.bind();
		glBegin(GL_LINE_LOOP);
		glVertex2f(x, y);
		glVertex2f(x + w, y);
		glVertex2f(x + w, y + h);
		glVertex2f(x, y + h);
		glEnd();
		if (col.getAlpha() != 255)
			glDisable(GL_BLEND);
		glEnable(GL_TEXTURE_2D);
		// Reset the color to white
		glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
	}

	public static void renderString(int x, int y, String str, Color bgcolor) {
		font.drawString(x, y, str, bgcolor);
	}

	public static void renderScreenCenteredString(int y, String str,
			Color bgcolor) {
		int w = font.getWidth(str);
		renderString(Display.getWidth() / 2 - w / 2, y, str, bgcolor);
	}
}
