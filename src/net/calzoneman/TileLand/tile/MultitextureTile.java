package net.calzoneman.TileLand.tile;

import java.util.HashMap;
import java.util.Map;

import net.calzoneman.TileLand.gfx.Renderer;
import net.calzoneman.TileLand.level.Level;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.opengl.Texture;

public class MultitextureTile extends Tile {
	
	protected Map<Integer, Rectangle> states;

	public MultitextureTile(short id, String name, Texture texture) {
		super(id, name, texture);
		states = new HashMap<Integer, Rectangle>();
		
		for(int i = 0; i < texture.getImageWidth() / TILESIZE; i++) {
			for(int j = 0; j < texture.getImageHeight() / TILESIZE; j++) {
				states.put(j * texture.getImageWidth() / TILESIZE + i, new Rectangle(i * TILESIZE, j * TILESIZE, TILESIZE, TILESIZE));
			}
		}
	}
	
	public int getNumStates() {
		return states.size();
	}
	
	@Override
	public void render(Level level, int tx, int ty, int x, int y) {
		int data = 0;
		if(level != null)
			data = isForeground() ? level.getFgData(tx, ty) : level.getBgData(tx, ty);
		render(x, y, data);		
	}
	
	public void render(int x, int y, int data) {
		Rectangle state = states.get(data);
		if(state == null)
			state = states.get(0);
		if(state == null) {
			System.out.println("nullstate");
			return;
		}
		Renderer.renderTextureSubrectangle(texture, state, x, y);
	}

}
