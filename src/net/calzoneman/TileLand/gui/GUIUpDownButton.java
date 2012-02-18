package net.calzoneman.TileLand.gui;

import org.newdawn.slick.geom.Rectangle;

import net.calzoneman.TileLand.TileLand;
import net.calzoneman.TileLand.gfx.Renderer;

public class GUIUpDownButton extends GUIButton {
	
	public static final int BUTTON_UPDOWN_WIDTH = 30;
	
	static final Rectangle UP_NORMAL = new Rectangle(0, 0, 30, 30);
	static final Rectangle UP_HOVER = new Rectangle(0, 30, 30, 30);
	static final Rectangle UP_DISABLED = new Rectangle(0, 60, 30, 30);
	
	static final Rectangle DOWN_NORMAL = new Rectangle(30, 0, 30, 30);
	static final Rectangle DOWN_HOVER = new Rectangle(30, 30, 30, 30);
	static final Rectangle DOWN_DISABLED = new Rectangle(30, 60, 30, 30);
	
	public static final int ORIENTATION_UP = 0;
	public static final int ORIENTATION_DOWN = 1;
	
	protected int orientation;

	public GUIUpDownButton(int x, int y) {
		this(x, y, null);
	}
	
	public GUIUpDownButton(int x, int y, GUIContainer parent) {
		super(x, y, BUTTON_UPDOWN_WIDTH, "", parent);
		this.texture = TileLand.getResourceManager().getTexture("res/gui/button_updown.png");
		this.enabled = true;
		this.orientation = ORIENTATION_UP;
	}
	
	public int getOrientation() {
		return orientation;
	}
	
	public void setOrientation(int orientation) {
		this.orientation = orientation;
	}
	
	@Override
	public void render() {
		Rectangle rect = null;
		if(orientation == ORIENTATION_UP) {
			if(!isEnabled())
				rect = UP_DISABLED;
			else if(isHovered())
				rect = UP_HOVER;
			else
				rect = UP_NORMAL;
		}
		else {
			if(!isEnabled())
				rect = DOWN_DISABLED;
			else if(isHovered())
				rect = DOWN_HOVER;
			else
				rect = DOWN_NORMAL;
		}
		if(rect != null)
			Renderer.renderTextureSubrectangle(texture, rect, x, y);
	}
}
