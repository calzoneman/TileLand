package net.calzoneman.TileLand.gui;


import net.calzoneman.TileLand.TileLand;
import net.calzoneman.TileLand.gfx.Renderer;
import net.calzoneman.TileLand.gfx.TilelandFont;

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
	
	static final int PADDING_HORIZONTAL = EDGE_WIDTH + 8;
	
	protected Texture texture;
	protected String text;
	protected String defaultText;
	protected int maxLength;
	
	protected int cursor;
	protected int viewOffset;
	protected int endOffset;

	public GUITextbox(int x, int y, int width) {
		this(x, y, width, "");
	}
	
	public GUITextbox(int x, int y, int width, String defaultText) {
		super(x, y, width, TEXTBOX_HEIGHT);
		this.text = defaultText;
		this.defaultText = defaultText;
		this.maxLength = -1;
		this.texture = TileLand.getResourceManager().getTexture("res/gui/textbox.png");
		this.cursor = defaultText.length();
		this.viewOffset = 0;
		this.endOffset = 0;
	}
	
	public int getMaxLength() {
		return maxLength;
	}
	
	public void setMaxLength(int max) {
		this.maxLength = max;
	}
	
	public String getText() {
		return this.text;
	}
	
	public String getTextOrDefault() {
		if(text.isEmpty())
			return defaultText;
		return text;
	}
	
	public String getDefaultText() {
		return defaultText;
	}
	
	public void setText(String txt) {
		this.text = txt;
		this.cursor = text.length();
	}

	@Override
	public void render() {
		TilelandFont fnt = TileLand.getResourceManager().getPreferredFont();
		Renderer.renderTextureSubrectangle(texture, LEFT_EDGE, x, y);
		Renderer.renderTextureSubrectangle(texture, CENTER, x + EDGE_WIDTH, y, width - 2*EDGE_WIDTH, height);
		Renderer.renderTextureSubrectangle(texture, RIGHT_EDGE, x + width - EDGE_WIDTH, y);
		int h = fnt.getHeight(text);
		if(h == 0)
			h = fnt.getHeight("|");
		int sy = y + height/2 - h/2;
		if(text.length() > 0) {
			fnt.drawString(x + PADDING_HORIZONTAL, sy, text.substring(viewOffset, text.length() - endOffset), transparent);
		}
		
		if(isFocused() && (System.currentTimeMillis() / 500) % 2 == 0) {
			int w = 0;
			if(text.length() > 0)
				w = fnt.getWidth(text.substring(viewOffset, cursor));
			fnt.drawString(x + PADDING_HORIZONTAL + w, sy, "_", transparent);
		}
	}
	
	@Override
	public void onFocus() {
		if(text.equals(defaultText))
			setText("");
	}
	
	@Override
	public void onBlur() {
		if(text.isEmpty())
			setText(defaultText);
	}
	
	@Override
	public void onKey(int keycode, char keychar) {
		if(keycode == Keyboard.KEY_BACK) {
			if(cursor > 0) {
				text = backspace(text, cursor);
				cursor--;
				if(endOffset > 0)
					endOffset--;
				if(cursor - viewOffset <= 0 && viewOffset > 0)
					viewOffset--;
			}
		}
		else if(keycode == Keyboard.KEY_LEFT){
			if(cursor > 0) {
				cursor--;
				if(cursor - viewOffset < 0 && viewOffset > 0) {
					endOffset++;
					viewOffset--;
				}
			}
		}
		else if(keycode == Keyboard.KEY_RIGHT){
			if(cursor < text.length()) {
				cursor++;
				if(cursor >= text.length() - endOffset) {
					viewOffset++;
					if(endOffset > 0)
						endOffset--;
				}
			}
		}
		else if(keychar != Keyboard.CHAR_NONE && (maxLength == -1 || text.length() < maxLength-1)) {
			text = insert(text, keychar, cursor);
			int w = TileLand.getResourceManager().getPreferredFont().getWidth(text) + PADDING_HORIZONTAL;
			if(w > this.width - PADDING_HORIZONTAL) {
				endOffset++;
				if(cursor >= text.length() - endOffset) {
					viewOffset++;
					endOffset--;
				}
			}
			cursor++;
		}
	}
	
	private String backspace(String src, int cursor) {
		char[] result = new char[src.length() - 1];
		for(int i = 0; i < cursor - 1; i++) {
			result[i] = src.charAt(i);
		}
		for(int i = cursor; i < src.length(); i++) {
			result[i-1] = src.charAt(i);
		}
		return String.copyValueOf(result);
	}
	
	private String insert(String src, char dest, int index) {
		char[] result = new char[src.length() + 1];
		for(int i = 0; i < index; i++) {
			result[i] = src.charAt(i);
		}
		result[index] = dest;
		for(int i = index; i < src.length(); i++) {
			result[i+1] = src.charAt(i);
		}
		return String.copyValueOf(result);
	}
}
