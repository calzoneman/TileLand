package net.calzoneman.TileLand.gui;

import java.util.HashMap;

import net.calzoneman.TileLand.TileLand;
import net.calzoneman.TileLand.tile.TileTypes;
import net.calzoneman.TileLand.util.Delegate;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.opengl.Texture;

public class TextureChooserMenu extends GUIMenu {
	
	GUIContainer container;
	GUIListView listView;
	
	public TextureChooserMenu() {
		init(0, 0, Display.getWidth(), Display.getHeight());
	}
	
	public void init(int x, int y, int width, int height) {
		container = new GUIContainer(Display.getWidth() / 2 - 320, Display.getHeight() / 2 - 240, 640, 480);
		listView = new GUIListView(0, 0, 640, 440);
		HashMap<String, Texture> available = TileLand.getResourceManager().getTextures("res/tiles/");
		for(String key : available.keySet()) {
			GUIListItem item = new GUIListItem(0, 0, 640, 100, available.get(key));
			item.<String>setData("path", key);
			item.setText(key);
			listView.addItem(item);
		}
		
		container.addChild("listview", listView);		
		GUIButton save = new GUIButton(160, 440, 320, "Save and Go Back");
		save.setClickHandler(
				new Delegate<GUIContainer, Void>() {
					@Override
					public Void run(GUIContainer param) {
						((TextureChooserMenu) param.getParent()).save();
						return null;
					}
				});
		container.addChild("save", save);
		container.setParent(this);
		addChild("container", container);
	}
	
	public void reInit(int x, int y, int width, int height) {
		children.clear();
		init(x, y, width, height);
	}
	
	public void save() {
		if(listView.getSelected() != null) {
			TileLand.getResourceManager().setPreferredTiles(listView.getSelected().<String>getData("path"));
			//TileTypes.init();
		}
		MenuManager.getMenuManager().goBack();
	}
}
