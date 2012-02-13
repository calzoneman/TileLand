package net.calzoneman.TileLand.gui;

import org.lwjgl.opengl.Display;

public class GUIMenu extends GUIContainer {
	public GUIMenu() {
		super(0, 0, Display.getWidth(), Display.getHeight());
	}
	
	public void init(int x, int y, int width, int height) {
		super.init(x, y, width, height);
	}
	
	public void reInit(int x, int y, int width, int height) {
		init(x, y, width, height);
	}
}