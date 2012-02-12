package net.calzoneman.TileLand.gui;

import net.calzoneman.TileLand.TileLand;
import net.calzoneman.TileLand.gfx.Renderer;
import net.calzoneman.TileLand.util.Delegate;

import org.newdawn.slick.opengl.Texture;

public class MainMenu extends GUIMenu {
	private Texture background;
	
	public MainMenu() {
		this.background = TileLand.getResourceManager().getTexture("res/title.png");
		
		GUIButton newLvlBtn = new GUIButton(170, 200, 300, "New Game");
		newLvlBtn.setClickHandler(
				new Delegate<GUIMenu, Void>() {
					@Override
					public Void run(GUIMenu param) {
						MenuManager.getMenuManager().openMenu("newlevelmenu");
						return null;
					}
				});
		addChild("newgame", newLvlBtn);
		GUIButton loadLvlBtn = new GUIButton(170, 240, 300, "Load Game");
		loadLvlBtn.setClickHandler(
				new Delegate<GUIMenu, Void>() {
					@Override
					public Void run(GUIMenu param) {
						MenuManager.getMenuManager().openMenu("loadlevelmenu");
						return null;
					}
				});
		addChild("loadgame", loadLvlBtn);
	}
	
	@Override
	public void render() {
		Renderer.renderTexture(background, 0, 0);
		for(GUIComponent child : children.values()) {
			child.render();
		}
	}
}
