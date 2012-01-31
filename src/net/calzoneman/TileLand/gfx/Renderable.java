package net.calzoneman.TileLand.gfx;

public interface Renderable {
	public abstract void render(int x, int y);
	
	public abstract void render(int x, int y, int data);
}
