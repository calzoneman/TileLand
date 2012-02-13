package net.calzoneman.TileLand.gui;

import java.io.File;

import net.calzoneman.TileLand.gfx.Renderer;
import net.calzoneman.TileLand.gfx.TilelandFont;
import net.calzoneman.TileLand.util.Delegate;

public class LoadLevelMenu extends GUIMenu {
	
	private String levelName;
	private String playerName;
	
	public LoadLevelMenu() {
		int x1 = 175;
		int x2 = x1 + 100;
		int curY = 145;
		int charHeight = Renderer.getFont().getHeight("|");
		
		GUITextbox plyNameTxt = new GUITextbox(x2, curY, 20, "Player");
		addChild("plynametxt", plyNameTxt);
		GUILabel plyNameLbl = new GUILabel(x1, curY + plyNameTxt.getHeight()/2 - charHeight/2, "Player name");
		addChild("plynamelbl", plyNameLbl);
		curY += plyNameTxt.getHeight() + 10;
		
		GUITextbox lvlNameTxt = new GUITextbox(x2, curY, 20, "untitled");
		addChild("lvlnametxt", lvlNameTxt);
		GUILabel lvlNameLbl = new GUILabel(x1, curY + lvlNameTxt.getHeight()/2 - charHeight/2, "Level name");
		addChild("lvlnamelbl", lvlNameLbl);
		curY += lvlNameTxt.getHeight() + 10;
		
		GUIButton cancelBtn = new GUIButton(x1, curY, 135, "Cancel", this);
		cancelBtn.setClickHandler(
				new Delegate<GUIMenu, Void>() {
					@Override
					public Void run(GUIMenu param) {
						MenuManager.getMenuManager().goBack();
						return null;
					}
				});
		addChild("cancelbtn", cancelBtn);
		GUIButton createBtn = new GUIButton(x1+155, curY, 135, "Load Level", this);
		createBtn.setClickHandler(
				new Delegate<GUIMenu, Void>() {
					@Override
					public Void run(GUIMenu param) {
						((LoadLevelMenu) param).loadLevel();
						return null;
					}
				});
		addChild("createbtn", createBtn);
		curY += createBtn.getHeight() + 10;
		
		GUILabel error = new GUILabel(x1, curY, "");
		addChild("error", error);
		
		fieldOrder = new String[] { "plynametxt", "lvlnametxt" };
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
		String plyName = "Player";
		String lvlName = "";
		try {
			plyName = ((GUITextbox) getChild("plynametxt")).getText();
			lvlName = ((GUITextbox) getChild("lvlnametxt")).getText();
		}
		catch(Exception ex) {
			((GUILabel) getChild("error")).setText(TilelandFont.TEXT_RED + "One or more inputs is invalid");
		}
		
		if(!lvlName.endsWith(".tl"))
			lvlName += ".tl";
		File f = new File("saves/" + lvlName);
		if(!f.exists() || !f.isFile())
			((GUILabel) getChild("error")).setText(TilelandFont.TEXT_DARK_RED + "The level \"" + f.getPath() + "\" does not exist!");
		else {
			this.levelName = lvlName;
			this.playerName = plyName;
			MenuManager.getMenuManager().openMenu("singleplayergame");
		}
	}
}
