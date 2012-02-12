package net.calzoneman.TileLand.gui;


public abstract class GUIComponent {
	protected int x;
	protected int y;
	protected int width;
	protected int height;
		
	public GUIComponent(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public abstract void render();
	
	public void onHover() { }
	
	public void onUnHover() { }
	
	public void onClick() { }
	
	public void onClickOut() { }
	
	public void onKey(int keyCode, char keychar) { }
	
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
	
	public boolean isFocusable() { return false; }
}
