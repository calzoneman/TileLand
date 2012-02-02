package net.calzoneman.TileLand.inventory;

import static org.lwjgl.opengl.GL11.*;

import org.newdawn.slick.Color;

import net.calzoneman.TileLand.gfx.Renderable;
import net.calzoneman.TileLand.gui.GUIComponent;

public class Quickbar extends GUIComponent {
	static final int NUM_SLOTS = 10;
	private Holdable[] slots;
	private int selected = 0;
	private Color slotBgColor = new Color(0.2f, 0.2f, 0.2f, 0.5f);
	private Color barBgColor = new Color(0.4f, 0.4f, 0.4f, 0.5f);
	
	public Quickbar() {
		super(10, 10, 375, 42);
		this.slots = new Holdable[NUM_SLOTS];
	}
	
	public Quickbar(int x, int y, int width, int height) {
		super(x, y, 375, 42);
		this.slots = new Holdable[NUM_SLOTS];
	}
	
	public boolean setSelectedSlot(int selected) {
		if(selected < 0 || selected >= NUM_SLOTS)
			return false;
		this.selected = selected;
		return true;
	}
	
	public int getSelectedSlot() {
		return this.selected;
	}
	
	public boolean setSlotItem(int slot, Holdable obj) {
		if(slot < 0 || slot >= NUM_SLOTS)
			return false;
		slots[slot] = obj;
		return true;
	}
	
	public Holdable getSlotItem(int slot) {
		if(slot < 0 || slot >= NUM_SLOTS)
			return null;
		return slots[slot];
	}
	
	public int getNumSlots() {
		return NUM_SLOTS;
	}

	@Override
	public void render() {
		drawFilledRect(this.x, this.y, this.width, this.height, barBgColor);
		for(int i = 0; i < NUM_SLOTS; i++) {
			int x = this.x + 5*(i+1) + i*32;
			int y = this.y + 5;
			drawFilledRect(x, y, 32, 32, slotBgColor);
			if(slots[i] != null && (slots[i] instanceof Renderable)) {
				Renderable r = (Renderable)slots[i];
				r.render(x, y);
			}
			if(i == selected)
				drawRect(x, y, 32, 32, Color.green);
			else
				drawRect(x, y, 32, 32, Color.black);
		}
	}
	
	private void drawRect(int x, int y, int w, int h, Color col) {
		glDisable(GL_TEXTURE_2D);
		col.bind();
		glBegin(GL_LINE_LOOP);
			glVertex2f(x, y);
			glVertex2f(x+w, y);
			glVertex2f(x+w, y+h);
			glVertex2f(x, y+h);
		glEnd();
		glEnable(GL_TEXTURE_2D);
		// Reset the color to white
		glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
	}

	private void drawFilledRect(int x, int y, int w, int h, Color col) {
		glDisable(GL_TEXTURE_2D);
		if(col.getAlpha() != 255)
			glEnable(GL_BLEND);
		col.bind();
		glBegin(GL_QUADS);
			glVertex2f(x, y);
			glVertex2f(x+w, y);
			glVertex2f(x+w, y+h);
			glVertex2f(x, y+h);
		glEnd();
		if(col.getAlpha() != 255)
			glDisable(GL_BLEND);
		glEnable(GL_TEXTURE_2D);
		// Reset the color to white
		glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
	}
}
