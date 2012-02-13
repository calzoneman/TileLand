package net.calzoneman.TileLand.gui;

import java.io.File;

import org.lwjgl.opengl.Display;

import net.calzoneman.TileLand.gfx.Renderer;
import net.calzoneman.TileLand.gfx.TilelandFont;
import net.calzoneman.TileLand.util.Delegate;

public class LoadLevelMenu extends GUIMenu {
	
	private String levelName;
	private String playerName;
	
	public LoadLevelMenu() {
		super();
		init(0, 0, Display.getWidth(), Display.getHeight());
	}
	
	@Override
	public void init(int x, int y, int width, int height) {
		GUIContainer container = new GUIContainer(Display.getWidth()/2 - 320, Display.getHeight()/2 - 240, 640, 480);
		int x1 = 175;
		int x2 = x1 + 100;
		int curY = 145;
		int charHeight = Renderer.getFont().getHeight("|");
		
		GUITextbox plyNameTxt = new GUITextbox(x2, curY, 20, "Player");
		container.addChild("plynametxt", plyNameTxt);
		GUILabel plyNameLbl = new GUILabel(x1, curY + plyNameTxt.getHeight()/2 - charHeight/2, "Player name");
		container.addChild("plynamelbl", plyNameLbl);
		curY += plyNameTxt.getHeight() + 10;
		
		GUITextbox lvlNameTxt = new GUITextbox(x2, curY, 20, "untitled");
		container.addChild("lvlnametxt", lvlNameTxt);
		GUILabel lvlNameLbl = new GUILabel(x1, curY + lvlNameTxt.getHeight()/2 - charHeight/2, "Level name");
		container.addChild("lvlnamelbl", lvlNameLbl);
		curY += lvlNameTxt.getHeight() + 10;
		
		GUIButton cancelBtn = new GUIButton(x1, curY, 135, "Cancel");
		cancelBtn.setClickHandler(
				new Delegate<GUIContainer, Void>() {
					@Override
					public Void run(GUIContainer param) {
						MenuManager.getMenuManager().goBack();
						return null;
					}
				});
		container.addChild("cancelbtn", cancelBtn);
		GUIButton createBtn = new GUIButton(x1+155, curY, 135, "Load Level");
		createBtn.setClickHandler(
				new Delegate<GUIContainer, Void>() {
					@Override
					public Void run(GUIContainer param) {
						((LoadLevelMenu) param.getParent()).loadLevel();
						return null;
					}
				});
		container.addChild("createbtn", createBtn);
		curY += createBtn.getHeight() + 10;
		
		GUILabel error = new GUILabel(x1, curY, "");
		container.addChild("error", error);
		
		container.setFieldOrder(new String[] { "plynametxt", "lvlnametxt" });
		container.setParent(this);
		addChild("container", container);
	}
	
	@Override
	public void reInit(int x, int y, int width, int height) {
		GUIContainer container = (GUIContainer) getChild("container");
		String plyName = ((GUITextbox) container.getChild("plynametxt")).getTextOrDefault();
		String lvlName = ((GUITextbox) container.getChild("lvlnametxt")).getTextOrDefault();
		children.clear();
		container = new GUIContainer(Display.getWidth()/2 - 320, Display.getHeight()/2 - 240, 640, 480);
		int x1 = 175;
		int x2 = x1 + 100;
		int curY = 145;
		int charHeight = Renderer.getFont().getHeight("|");
		
		GUITextbox plyNameTxt = new GUITextbox(x2, curY, 20, "Player");
		plyNameTxt.setText(plyName);
		container.addChild("plynametxt", plyNameTxt);
		GUILabel plyNameLbl = new GUILabel(x1, curY + plyNameTxt.getHeight()/2 - charHeight/2, "Player name");
		container.addChild("plynamelbl", plyNameLbl);
		curY += plyNameTxt.getHeight() + 10;
		
		GUITextbox lvlNameTxt = new GUITextbox(x2, curY, 20, "untitled");
		lvlNameTxt.setText(lvlName);
		container.addChild("lvlnametxt", lvlNameTxt);
		GUILabel lvlNameLbl = new GUILabel(x1, curY + lvlNameTxt.getHeight()/2 - charHeight/2, "Level name");
		container.addChild("lvlnamelbl", lvlNameLbl);
		curY += lvlNameTxt.getHeight() + 10;
		
		GUIButton cancelBtn = new GUIButton(x1, curY, 135, "Cancel");
		cancelBtn.setClickHandler(
				new Delegate<GUIContainer, Void>() {
					@Override
					public Void run(GUIContainer param) {
						MenuManager.getMenuManager().goBack();
						return null;
					}
				});
		container.addChild("cancelbtn", cancelBtn);
		GUIButton createBtn = new GUIButton(x1+155, curY, 135, "Load Level");
		createBtn.setClickHandler(
				new Delegate<GUIContainer, Void>() {
					@Override
					public Void run(GUIContainer param) {
						((LoadLevelMenu) param.getParent()).loadLevel();
						return null;
					}
				});
		container.addChild("createbtn", createBtn);
		curY += createBtn.getHeight() + 10;
		
		GUILabel error = new GUILabel(x1, curY, "");
		container.addChild("error", error);
		
		container.setFieldOrder(new String[] { "plynametxt", "lvlnametxt" });
		container.setParent(this);
		addChild("container", container);
	}
	
	@Override
	protected void onEnter() {
		loadLevel();
	}
	
	public String getPlayerName() {
		return this.playerName;
	}
	
	public String getLevelName() {
		return this.levelName;
	}

	protected void loadLevel() {
		GUIContainer container = (GUIContainer) getChild("container");
		String plyName = "Player";
		String lvlName = "";
		plyName = ((GUITextbox) container.getChild("plynametxt")).getText();
		lvlName = ((GUITextbox) container.getChild("lvlnametxt")).getText();
		
		if(!lvlName.endsWith(".tl"))
			lvlName += ".tl";
		File f = new File("saves/" + lvlName);
		if(!f.exists() || !f.isFile())
			((GUILabel) container.getChild("error")).setText(TilelandFont.TEXT_DARK_RED + "The level \"" + f.getPath() + "\" does not exist!");
		else {
			this.levelName = lvlName;
			this.playerName = plyName;
			MenuManager.getMenuManager().openMenu("singleplayergame");
		}
	}
}
