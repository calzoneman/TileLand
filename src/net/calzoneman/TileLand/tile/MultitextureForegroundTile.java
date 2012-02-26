package net.calzoneman.TileLand.tile;

import org.newdawn.slick.opengl.Texture;

public class MultitextureForegroundTile extends MultitextureTile {

	public MultitextureForegroundTile(short id, String name, Texture texture) {
		super(id, name, texture);
		solid = true;
		foreground = true;
	}

}
