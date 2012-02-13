package net.calzoneman.TileLand.gui;

import net.calzoneman.TileLand.gfx.Renderer;

import org.newdawn.slick.opengl.Texture;

public class GUIImage extends GUIComponent {
	protected Texture texture;

	public GUIImage(int x, int y, Texture tex) {
		super(x, y, tex.getImageHeight(), tex.getImageHeight());
		this.texture = tex;
	}

	@Override
	public void render() {
		Renderer.renderTexture(texture, x, y);
	}

}
