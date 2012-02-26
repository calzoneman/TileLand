package net.calzoneman.TileLand.tile;

import org.newdawn.slick.opengl.Texture;

public class SignTile extends MultitextureForegroundTile {
	
	protected String message;

	public SignTile(short id, String name, Texture texture) {
		super(id, name, texture);
		solid = true;
	}

}
