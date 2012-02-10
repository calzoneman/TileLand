package net.calzoneman.TileLand.inventory;

import net.calzoneman.TileLand.gfx.Renderable;
import net.calzoneman.TileLand.gfx.Renderer;
import net.calzoneman.TileLand.gfx.TilelandFont;

public class ItemStack implements Cloneable, Renderable {
	public static final int MAX_STACK_SIZE = 100;
	
	private Item item;
	private byte data;
	private int count;
	
	public ItemStack(Item it) {
		this.item = it;
		this.count = 1;
		this.data = 0;
	}
	
	public ItemStack(Item it, int count) {
		this.item = it;
		if(count < 0)
			count = 0;
		else if(count <= MAX_STACK_SIZE)
			this.count = count;
		else
			this.count = MAX_STACK_SIZE;
		this.data = 0;
	}
	
	public ItemStack(Item it, int count, int data) {
		this.item = it;
		if(count < 0)
			count = 0;
		else if(count <= MAX_STACK_SIZE)
			this.count = count;
		else
			this.count = MAX_STACK_SIZE;
		this.data = (byte) data;
	}
	
	public Item getItem() {
		return item;
	}
	
	public byte getData() {
		return data;
	}
	
	public int getCount() {
		return count;
	}
	
	public void setData(int data) { 
		this.data = (byte) data;
	}
	
	public void setCount(int count) {
		if(count >= 0 && count <= MAX_STACK_SIZE)
			this.count = count;
	}
	
	@Override
	public ItemStack clone() {
		return new ItemStack(item, count, data);
	}

	@Override
	public void render(int x, int y) {
		// Draw the item
		if(item != null) {
			item.render(x, y, data);
		}
		
		// Draw the count
		if(count > 1)
			Renderer.getFont().drawString(x, y, TilelandFont.TEXT_YELLOW + count);
	}

	@Override
	public void render(int x, int y, int data) {
	}
}
