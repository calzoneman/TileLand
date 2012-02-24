package net.calzoneman.TileLand.gui;

import net.calzoneman.TileLand.TileLand;
import net.calzoneman.TileLand.gfx.Renderer;

public class GUILabel extends GUIComponent {
	
	protected String text;

	public GUILabel(int x, int y, String text) {
		super(x, y, Renderer.getFont().getWidth(text), Renderer.getFont().getHeight(text));
		this.text = text;
	}
	
	@Override
	public int getWidth() {
		return TileLand.getResourceManager().getPreferredFont().getWidth(text);
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
		this.setSize(Renderer.getFont().getWidth(text), Renderer.getFont().getHeight(text));
	}

	@Override
	public void render() {
		TileLand.getResourceManager().getPreferredFont().drawString(x, y, text);
	}

}
