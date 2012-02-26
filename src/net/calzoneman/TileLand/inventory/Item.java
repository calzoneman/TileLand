package net.calzoneman.TileLand.inventory;

import net.calzoneman.TileLand.gfx.Renderer;

import org.newdawn.slick.opengl.Texture;

public class Item {
	public final short id;
	public final String name;
	protected Texture texture;
	protected byte data;

	public Item(short id, String name) {
		this.id = id;
		this.name = name;
	}

	public Item(short id, String name, Texture texture) {
		this.id = id;
		this.name = name;
		this.texture = texture;
	}
	
	public Item(short id, String name, Texture texture, int data) {
		this(id, name, texture);
		this.data = (byte) data;
	}

	public Texture getTexture() {
		return texture;
	}
	
	public void setTexture(Texture tex) {
		this.texture = tex;
	}
	
	public int getData() {
		return data;
	}
	
	public void setData(int data) {
		this.data = (byte) data;
	}
	
	@Override
	public boolean equals(Object other) {
		if(other == this)
			return true;
		if(other == null)
			return false;
		if(other instanceof Item) {
			Item it = (Item) other;
			return this.id == it.id && this.data == it.data;
		}
		else
			return false;
	}
	
	public void render(int x, int y) {
		Renderer.renderTexture(texture, x, y);
	}
}
