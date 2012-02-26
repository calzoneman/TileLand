package net.calzoneman.TileLand.tile;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.opengl.Texture;

import net.calzoneman.TileLand.gfx.Renderer;
import net.calzoneman.TileLand.level.Level;

public class PartialDirectionalFgTile extends PartialDirectionalTile {
	
	public PartialDirectionalFgTile(short id, String name, Texture texture) {
		super(id, name, texture);
		foreground = true;
	}

	@Override
	public void render(Level level, int tx, int ty, int x, int y) {
		if(level == null) {
			render(CENTER, x, y);
			return;
		}
		boolean u = connectsTo(level.getFg(tx, ty - 1));
		boolean d = connectsTo(level.getFg(tx, ty + 1));
		boolean l = connectsTo(level.getFg(tx - 1, ty));
		boolean r = connectsTo(level.getFg(tx + 1, ty));

		if(u && d && l && !r)
			render(RIGHT_EDGE, x, y);
		else if(u && d && r && !l)
			render(LEFT_EDGE, x, y);
		else if(l && r && u && !d)
			render(BOTTOM_EDGE, x, y);
		else if(l && r && !u && d)
			render(TOP_EDGE, x, y);
		else if(u && r && !d && !l)
			render(BOTTOM_CORNER_LEFT, x, y);
		else if(u && l && !d && !r)
			render(BOTTOM_CORNER_RIGHT, x, y);
		else if(d && l && !u && !r)
			render(TOP_CORNER_RIGHT, x, y);
		else if(d && r && !u && !l)
			render(TOP_CORNER_LEFT, x, y);
		else
			render(CENTER, x, y);
	}
	
	public void render(Rectangle rect, int x, int y) {
		Renderer.renderTextureSubrectangle(texture, rect, x, y);
	}
}
