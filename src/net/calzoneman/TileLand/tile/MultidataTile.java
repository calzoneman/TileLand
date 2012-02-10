package net.calzoneman.TileLand.tile;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.util.HashMap;

import net.calzoneman.TileLand.level.Level;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.opengl.Texture;

public class MultidataTile extends Tile {
	protected HashMap<Integer, Rectangle> states;

	public MultidataTile(int id, String name, Texture tex, Rectangle texPosition) {
		this(id, name, tex, texPosition, TileProperties.HASDATA);
	}
	
	public MultidataTile(int id, String name, Texture tex, Rectangle texPosition, int properties) {
		super(id, name, tex, texPosition, properties | TileProperties.HASDATA);
		loadStates(texPosition);
	}

	public void loadStates(Rectangle rect) {
		states = new HashMap<Integer, Rectangle>();
		int n = 0;
		for(int j = 0; j < rect.getHeight(); j += Level.TILESIZE) {
			for(int i = 0; i < rect.getWidth(); i += Level.TILESIZE) {
				states.put(n, new Rectangle(i + rect.getX(), j + rect.getY(), Level.TILESIZE, Level.TILESIZE));
				n++;
			}
		}
	}
	
	public int getNumStates() {
		return states.size();
	}
	
	@Override
	public void render(int x, int y) {
		render(x, y, 0);
	}
	
	@Override
	public void render(int x, int y, int data) {
		Rectangle rect = states.get(data);
		if(rect == null)
			return;
		int texWidth = tex.getTextureWidth();
		int texHeight = tex.getTextureHeight();
		float rectX = rect.getX();
		float rectY = rect.getY();
		float rectWidth = rect.getWidth();
		float rectHeight = rect.getHeight();
		glEnable(GL_BLEND);
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
		glDisable(GL_BLEND);
	}
}
