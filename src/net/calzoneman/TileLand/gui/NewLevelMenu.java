package net.calzoneman.TileLand.gui;

import net.calzoneman.TileLand.gfx.Renderer;
import net.calzoneman.TileLand.gfx.TilelandFont;
import net.calzoneman.TileLand.util.Delegate;

public class NewLevelMenu extends GUIMenu {
	
	private GameParameters gameParameters;
	
	public NewLevelMenu() {
		int x1 = 175;
		int x2 = x1 + 100;
		int curY = 145;
		int charHeight = Renderer.getFont().getHeight("|");
		
		GUITextbox plyNameTxt = new GUITextbox(x2, curY, 20, "Player");
		addChild("plynametxt", plyNameTxt);
		GUILabel plyNameLbl = new GUILabel(x1, curY + plyNameTxt.getHeight()/2 - charHeight/2, "Player name");
		addChild("plynamelbl", plyNameLbl);
		curY += plyNameTxt.getHeight() + 10;
		
		GUITextbox lvlWidthTxt = new GUITextbox(x2, curY, 20, "100");
		addChild("lvlwidthtxt", lvlWidthTxt);
		GUILabel lvlWidthLbl = new GUILabel(x1, curY + lvlWidthTxt.getHeight()/2 - charHeight/2, "Level width");
		addChild("lvlwidthlbl", lvlWidthLbl);
		curY += lvlWidthTxt.getHeight() + 10;
		
		GUITextbox lvlHeightTxt = new GUITextbox(x2, curY, 20, "100");
		addChild("lvlheighttxt", lvlHeightTxt);
		GUILabel lvlHeightLbl = new GUILabel(x1, curY + lvlHeightTxt.getHeight()/2 - charHeight/2, "Level height");
		addChild("lvlheightlbl", lvlHeightLbl);
		curY += lvlHeightTxt.getHeight() + 10;
		
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
		
		GUIButton createBtn = new GUIButton(x1+155, curY, 135, "Create Level", this);
		createBtn.setClickHandler(
				new Delegate<GUIMenu, Void>() {
					@Override
					public Void run(GUIMenu param) {
						((NewLevelMenu) param).createLevel();
						return null;
					}
				});
		addChild("createbtn", createBtn);
		curY += createBtn.getHeight() + 10;
		
		GUILabel widtherror = new GUILabel(x1, curY, "");
		addChild("widtherror", widtherror);
		curY += widtherror.getHeight() + 10;
		
		GUILabel heighterror = new GUILabel(x1, curY, "");
		addChild("heighterror", heighterror);
		curY += heighterror.getHeight() + 10;
		
		GUILabel levelnameerror = new GUILabel(x1, curY, "");
		addChild("levelnameerror", levelnameerror);
		curY += levelnameerror.getHeight() + 10;
		
		fieldOrder = new String[] { "plynametxt", "lvlwidthtxt", "lvlheighttxt", "lvlnametxt" };
	}
	
	@Override
	protected void onEnter() {
		createLevel();
	}
	
	public void createLevel() {
		if(validate())
			MenuManager.getMenuManager().openMenu("singleplayergame");
	}
	
	public boolean validate() {
		int w = -1;
		int h = -1;
		String lvlName = "";
		String plyName = "";
		boolean error = false;
		try {
			w = Integer.parseInt(((GUITextbox) getChild("lvlwidthtxt")).getText());
		}
		catch(Exception ex) {
			((GUILabel) getChild("widtherror")).setText(TilelandFont.TEXT_DARK_RED + "Invalid level width");
			error = true;
		}
		try {
			h = Integer.parseInt(((GUITextbox) getChild("lvlheighttxt")).getText());
		}
		catch(Exception ex) {
			((GUILabel) getChild("heighterror")).setText(TilelandFont.TEXT_DARK_RED + "Invalid level height");
			error = true;
		}
		lvlName = ((GUITextbox) getChild("lvlnametxt")).getText();
		if(lvlName.isEmpty()) {
			((GUILabel) getChild("levelnameerror")).setText(TilelandFont.TEXT_DARK_RED + "Invalid level name");
			error = true;
		}
		plyName = ((GUITextbox) getChild("plynametxt")).getText();
		if(w < 0) {
			((GUILabel) getChild("widtherror")).setText(TilelandFont.TEXT_DARK_RED + "Invalid level width");
			error = true;
		}
		if(h < 0) {
			((GUILabel) getChild("heighterror")).setText(TilelandFont.TEXT_DARK_RED + "Invalid level height");
			error = true;
		}
		gameParameters = new GameParameters(w, h, lvlName, plyName);
		return !error;
	}
	
	public GameParameters getGameParameters() {
		return gameParameters;
	}
	
	public static class GameParameters {
		public int width;
		public int height;
		public String levelName;
		public String playerName;
		
		public static final GameParameters defaultGameParameters = new GameParameters(100, 100, "untitled", "Player");
		
		public GameParameters(int width, int height, String levelName, String playerName) {
			this.width = width;
			this.height = height;
			this.levelName = levelName;
			this.playerName = playerName;
		}
	}
}
