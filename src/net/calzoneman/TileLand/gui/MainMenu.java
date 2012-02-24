package net.calzoneman.TileLand.gui;

import net.calzoneman.TileLand.TileLand;
import net.calzoneman.TileLand.gfx.Renderer;
import net.calzoneman.TileLand.gfx.TilelandFont;
import net.calzoneman.TileLand.util.Delegate;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

public class MainMenu extends GUIMenu {
	
	static final String copyright = "Copyright 2012 Calvin Montgomery";
	static final Color transparent = new Color(0, 0, 0, 0);
	static final Color backgroundColor = new Color(64, 64, 64);

	public MainMenu() {
		super();
		init(0, 0, Display.getWidth(), Display.getHeight());
	}
	
	@Override
	public void init(int x, int y, int width, int height) {
		GUIContainer container = new GUIContainer(Display.getWidth()/2 - 320, Display.getHeight()/2 - 240, 640, 480);
		container.addChild("title", new GUIImage(container.getWidth()/2 - 256, 50, TileLand.getResourceManager().getTexture("res/title.png")));
		
		GUIButton newLvlBtn = new GUIButton(170, 200, 300, "New Game");
		newLvlBtn.setClickHandler(
				new Delegate<GUIContainer, Void>() {
					@Override
					public Void run(GUIContainer param) {
						MenuManager.getMenuManager().openMenu("newlevelmenu");
						return null;
					}
				});
		container.addChild("newgame", newLvlBtn);
		GUIButton loadLvlBtn = new GUIButton(170, 240, 300, "Load Game");
		loadLvlBtn.setClickHandler(
				new Delegate<GUIContainer, Void>() {
					@Override
					public Void run(GUIContainer param) {
						MenuManager.getMenuManager().openMenu("loadlevelmenu");
						return null;
					}
				});
		container.addChild("loadgame", loadLvlBtn);
		GUIButton multiplayerBtn = new GUIButton(170, 280, 300, "Multiplayer");
		//multiplayerBtn.setEnabled(false);
		multiplayerBtn.setClickHandler(
				new Delegate<GUIContainer, Void>() {
					@Override
					public Void run(GUIContainer param) {
						MenuManager.getMenuManager().openMenu("mplogin");
						return null;
					}
				});
		container.addChild("multiplayer", multiplayerBtn);
		GUIButton setTextureBtn = new GUIButton(170, 320, 300, "Select Texture");
		setTextureBtn.setClickHandler(
				new Delegate<GUIContainer, Void>() {
					@Override
					public Void run(GUIContainer param) {
						MenuManager.getMenuManager().openMenu("settexturemenu");
						return null;
					}
				});
		container.addChild("settexturebtn", setTextureBtn);
		
		container.setParent(this);
		addChild("container", container);
	}
	
	@Override
	public void reInit(int x, int y, int width, int height) {
		children.clear();
		init(x, y, width, height);
	}
	
	@Override
	public void render() {
		Renderer.renderFilledRect(0, 0, Display.getWidth(), Display.getHeight(), backgroundColor);
		for(GUIComponent child : children.values()) {
			child.render();
		}
		TilelandFont copyFont = TileLand.getResourceManager().getFont("res/font/default.ttf", 24);
		if(copyFont.getWidth(copyright) > width - 5)
			copyFont = TileLand.getResourceManager().getFont("res/font/default.ttf", 16);
		if(copyFont.getWidth(copyright) > width - 5)
			copyFont = TileLand.getResourceManager().getFont("res/font/default.ttf");
		copyFont.drawString(5, Display.getHeight() - copyFont.getHeight(copyright) - 5, copyright, transparent);
	}
}
