package net.calzoneman.TileLand.inventory;

import org.newdawn.slick.Color;

import net.calzoneman.TileLand.gfx.Renderer;
import net.calzoneman.TileLand.gui.GUIComponent;

public class Quickbar extends GUIComponent {
	public static final int QUICKBAR_COUNT = 10;
	private ItemStack[] contents;
	private int selected = 0;
	private Color slotBgColor = new Color(0.2f, 0.2f, 0.2f, 0.5f);
	private Color barBgColor = new Color(0.4f, 0.4f, 0.4f, 0.5f);
	
	public Quickbar() {
		super(10, 10, 375, 42);
		this.contents = new ItemStack[QUICKBAR_COUNT];
	}
	
	public Quickbar(int x, int y, int width, int height) {
		super(x, y, 375, 42);
		this.contents = new ItemStack[QUICKBAR_COUNT];
	}
	
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

	@Override
	public void render() {
		// Draw the quickbar's background
		Renderer.drawFilledRect(this.x, this.y, this.width, this.height, barBgColor);
		for(int i = 0; i < QUICKBAR_COUNT; i++) {
			int x = this.x + 5*(i+1) + i*32;
			int y = this.y + 5;
			// Draw the slot background
			Renderer.drawFilledRect(x, y, 32, 32, slotBgColor);
			// Draw the contents (where applicable)
			if(contents[i] != null)
				contents[i].render(x, y);
			// Draw the selection border
			if(i == selected)
				Renderer.drawRect(x, y, 32, 32, Color.green);
		}
	}
}
