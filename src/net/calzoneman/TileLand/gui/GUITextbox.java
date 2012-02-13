package net.calzoneman.TileLand.gui;

import net.calzoneman.TileLand.TileLand;
import net.calzoneman.TileLand.gfx.Renderer;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.opengl.Texture;

public class GUITextbox extends GUIComponent {
	
	public static final int TEXTBOX_HEIGHT = 30;
	static final int EDGE_WIDTH = 3;
	static final Rectangle LEFT_EDGE = new Rectangle(0, 0, 3, 30);
	static final Rectangle CENTER = new Rectangle(3, 0, 1, 30);
	static final Rectangle RIGHT_EDGE = new Rectangle(4, 0, 3, 30);
	
	static final Color transparent = new Color(0, 0, 0, 0);
	
	static final int PADDING_LEFT = 8;
	
	protected Texture texture;
	protected String text;
	protected String defaultText;
	protected int maxLength;

	public GUITextbox(int x, int y, int maxlen) {
		this(x, y, maxlen, "");
	}
	
	public GUITextbox(int x, int y, int maxlen, String defaultText) {
		super(x, y, PADDING_LEFT + EDGE_WIDTH + Renderer.getFont().getWidth("_") * maxlen, TEXTBOX_HEIGHT);
		this.text = defaultText;
		this.defaultText = defaultText;
		this.maxLength = maxlen;
		this.texture = TileLand.getResourceManager().getTexture("res/gui/textbox.png");
	}
	
	public String getText() {
		return this.text;
	}
	
	public void setText(String txt) {
		this.text = txt;
	}

	@Override
	public void render() {
		Renderer.renderTextureSubrectangle(texture, LEFT_EDGE, x, y);
		Renderer.renderTextureSubrectangle(texture, CENTER, x + EDGE_WIDTH, y, width - 2*EDGE_WIDTH, height);
		Renderer.renderTextureSubrectangle(texture, RIGHT_EDGE, x + width - EDGE_WIDTH, y);
		int h = Renderer.getFont().getHeight(text);
		int sy = y + height/2 - h/2;
		Renderer.getFont().drawString(x + PADDING_LEFT + EDGE_WIDTH, sy, text, transparent);
		
		if(isFocused() && (System.currentTimeMillis() / 500) % 2 == 0)
			Renderer.getFont().drawString(x + PADDING_LEFT + EDGE_WIDTH + Renderer.getFont().getWidth(text), sy, "_", transparent);
	}
	
	@Override
	public void onFocus() {
		if(text.equals(defaultText))
			text = "";
	}
	
	@Override
	public void onBlur() {
		if(text.isEmpty())
			text = defaultText;
	}
	
	@Override
	public void onKey(int keycode, char keychar) {
		if(keycode == Keyboard.KEY_BACK) {
			if(text.length() > 0)
				text = text.substring(0, text.length()-1);
		}
		else if(keychar != Keyboard.CHAR_NONE && text.length() < maxLength-1) {
			text += keychar;
		}
	}
}
