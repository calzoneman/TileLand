package net.calzoneman.TileLand.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.opengl.Texture;

import net.calzoneman.TileLand.TileLand;
import net.calzoneman.TileLand.gfx.Renderer;
import net.calzoneman.TileLand.gfx.TilelandFont;

public class GUIListItem extends GUIComponent {
	
	// Render positions
	static final Rectangle LEFT_EDGE_NORMAL = new Rectangle(0, 0, 3, 100);
	static final Rectangle CENTER_NORMAL = new Rectangle(3, 0, 1, 100);
	static final Rectangle RIGHT_EDGE_NORMAL = new Rectangle(4, 0, 3, 100);
	
	static final Rectangle LEFT_EDGE_FOCUS = new Rectangle(0, 100, 3, 100);
	static final Rectangle CENTER_FOCUS = new Rectangle(3, 100, 1, 100);
	static final Rectangle RIGHT_EDGE_FOCUS = new Rectangle(4, 100, 3, 100);
	
	static final Color transparent = new Color(0, 0, 0, 0);
	
	public static final int HEIGHT = 100;
	static final int IMAGE_PADDING_LEFT = 32;
	static final int TEXT_PADDING_LEFT = IMAGE_PADDING_LEFT + 72;
	static final int LINE_SPACING = 4;
	
	protected GUIListView parent;
	protected GUIImage icon;
	protected List<String> text;
	protected Map<String, Object> metadata;
	protected Texture texture;

	public GUIListItem(int x, int y, int width, int height) {
		super(x, y, width, height);
		init();
	}
	
	public GUIListItem(int x, int y, int width, int height, Texture icon) {
		this(x, y, width, height, new GUIImage(x + IMAGE_PADDING_LEFT, y + height / 2 - 32, icon));
	}
	
	public GUIListItem(int x, int y, int width, int height, GUIImage icon) {
		super(x, y, width, height);
		this.icon = icon;
		init();
	}
	
	public void init() {
		if(icon != null)
			icon.setSize(64, 64);
		this.text = new ArrayList<String>();
		this.metadata = new HashMap<String, Object>();
		this.texture = TileLand.getResourceManager().getTexture("res/gui/listitem.png");
	}
	
	public void setParent(GUIListView parent) {
		this.parent = parent;
	}
	
	@Override
	public void onClick() {
		parent.select(this);
	}
	
	@Override
	public void setPosition(int x, int y) {
		super.setPosition(x, y);
		icon.setPosition(x + IMAGE_PADDING_LEFT, y + height / 2 - 32);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getData(String key) {
		return (T) metadata.get(key);
	}
	
	public <T> void setData(String key, T obj) {
		metadata.put(key, obj);
	}
	
	public List<String> getText() {
		return text;
	}
	
	public void setText(String txt) {
		this.text = new ArrayList<String>();
		addText(txt);
	}
	
	public void setText(List<String> txt) {
		this.text = new ArrayList<String>();
		for(String str : txt) {
			addText(str);
		}
	}
	
	public void addText(String txt) {
		List<String> result = new ArrayList<String>();
		String temp = "";
		while(TileLand.getResourceManager().getPreferredFont().getWidth(txt) > width - 10) {
			temp = txt.substring(txt.length() - 1) + temp;
			txt = txt.substring(0, txt.length() - 1);
			if(TileLand.getResourceManager().getPreferredFont().getWidth(temp) > width - 10) {
				result.add(temp);
				temp = "";
			}
		}
		result.add(temp);
		result.add(0, txt);
		text.addAll(result);
	}
	
	protected int getTextHeight() {
		TilelandFont fnt = TileLand.getResourceManager().getPreferredFont();
		int ht = 0;
		for(String txt : text)
			ht += fnt.getHeight(txt) + LINE_SPACING;
		return ht;
	}

	@Override
	public void render() {
		render(x, y);
	}
	
	public void render(int px, int py) {
		// Draw background
		if(isFocused()) {
			Renderer.renderTextureSubrectangle(texture, LEFT_EDGE_FOCUS, px, py);
			Renderer.renderTextureSubrectangle(texture, CENTER_FOCUS, px + 3, py, width - 6, height);
			Renderer.renderTextureSubrectangle(texture, RIGHT_EDGE_FOCUS, px + width - 3, py);
		}
		else {
			Renderer.renderTextureSubrectangle(texture, LEFT_EDGE_NORMAL, px, py);
			Renderer.renderTextureSubrectangle(texture, CENTER_NORMAL, px + 3, py, width - 6, height);
			Renderer.renderTextureSubrectangle(texture, RIGHT_EDGE_NORMAL, px + width - 3, py);
		}
		if(icon != null) {
			icon.setPosition(px + IMAGE_PADDING_LEFT, py + height / 2 - 32);
			icon.render();
		}
		
		TilelandFont fnt = TileLand.getResourceManager().getPreferredFont();
		int ht = getTextHeight();
		int curY = py + height / 2 - ht / 2;
		for(int i = 0; i < text.size(); i++) {
			fnt.drawString(px + TEXT_PADDING_LEFT, curY, text.get(i), transparent);
			curY += fnt.getHeight(text.get(i)) + LINE_SPACING;
		}
	}

}
