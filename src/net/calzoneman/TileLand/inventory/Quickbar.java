package net.calzoneman.TileLand.inventory;

import org.newdawn.slick.Color;

import net.calzoneman.TileLand.gfx.Renderer;
import net.calzoneman.TileLand.level.Level;

public class Quickbar {
	public static final int QUICKBAR_COUNT = 10;
	static final int SLOT_PADDING = 6;
	private int x;
	private int y;
	private int width;
	private int height;
	private ItemStack[] contents;
	private int selected = 0;
	private Color slotBgColor = new Color(0.2f, 0.2f, 0.2f, 0.5f);
	private Color barBgColor = new Color(0.4f, 0.4f, 0.4f, 0.5f);
	
	public Quickbar() {
		this(10, 10, 10 * Level.TILESIZE + 11 * SLOT_PADDING, Level.TILESIZE + 2 * SLOT_PADDING);
	}
	
	public Quickbar(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.contents = new ItemStack[QUICKBAR_COUNT];
	}
	
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() { return x; }
	
	public int getY() { return y; }
	
	public void setSize(int w, int h) {
		this.width = w;
		this.height = h;
	}
	
	public int getWidth() { return width; }
	
	public int getHeight() { return height; }
	
	public void nextSlot() {
		selected++;
		if(selected >= QUICKBAR_COUNT)
			selected = 0;
	}
	
	public void prevSlot() {
		selected--;
		if(selected < 0)
			selected = QUICKBAR_COUNT-1;
	}
	
	public boolean setSelectedSlot(int selected) {
		if(selected < 0 || selected >= QUICKBAR_COUNT)
			return false;
		this.selected = selected;
		return true;
	}
	
	public int getSelectedSlot() {
		return this.selected;
	}
	
	public ItemStack getSelectedItemStack() {
		return getItemStack(getSelectedSlot());
	}
	
	public boolean setItemStack(int slot, ItemStack obj) {
		if(slot < 0 || slot >= QUICKBAR_COUNT)
			return false;
		contents[slot] = obj;
		return true;
	}
	
	public ItemStack getItemStack(int slot) {
		if(slot < 0 || slot >= QUICKBAR_COUNT)
			return null;
		return contents[slot];
	}
	
	public int getCount() {
		return QUICKBAR_COUNT;
	}

	public void render() {
		// Draw the quickbar's background
		Renderer.renderFilledRect(this.x, this.y, this.width, this.height, barBgColor);
		for(int i = 0; i < QUICKBAR_COUNT; i++) {
			int x = this.x + SLOT_PADDING*(i+1) + i*Level.TILESIZE;
			int y = this.y + SLOT_PADDING;
			// Draw the slot background
			Renderer.renderFilledRect(x, y, Level.TILESIZE, Level.TILESIZE, slotBgColor);
			// Draw the contents (where applicable)
			if(contents[i] != null)
				contents[i].render(x, y);
			// Draw the selection border
			if(i == selected)
				Renderer.renderRect(x, y, Level.TILESIZE, Level.TILESIZE, Color.green);
		}
	}
}
