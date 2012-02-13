package net.calzoneman.TileLand.gui;

import java.util.Stack;

public class MenuManager {
	
	static MenuManager menuManager = new MenuManager();
	
	private GUIMenu currentMenu = null;
	private Stack<GUIMenu> parentMenus = new Stack<GUIMenu>();
	
	public static MenuManager getMenuManager() {
		return menuManager;
	}
	
	private void pushParent(GUIMenu parent) {
		parent.resetAll();
		parentMenus.push(parent);
	}
	
	public void openMenu(String menuName) {
		GUIMenu old = currentMenu;
		if(menuName.equals("mainmenu"))
			currentMenu = new MainMenu();
		else if(menuName.equals("newlevelmenu"))
			currentMenu = new NewLevelMenu();
		else if(menuName.equals("loadlevelmenu")) {
			currentMenu = new LoadLevelMenu();
		}
		else if(menuName.equals("singleplayergame")) {
			if(currentMenu instanceof NewLevelMenu) {
				currentMenu = new SingleplayerGame(((NewLevelMenu) currentMenu).getGameParameters());
			}
			else if(currentMenu instanceof LoadLevelMenu) {
				LoadLevelMenu llm = (LoadLevelMenu) currentMenu;
				currentMenu = new SingleplayerGame(llm.getLevelName(), llm.getPlayerName());
			}
			old = new MainMenu();
			parentMenus = new Stack<GUIMenu>(); // Reset the menu Stack
		}
		if(old != null)
			pushParent(old);
	}
	
	public void goBack() {
		if(!parentMenus.isEmpty())
			currentMenu = parentMenus.pop();
	}
	
	public void render() {
		if(currentMenu != null)
			currentMenu.render();
	}
	
	public void handleInput() {
		if(currentMenu != null)
			currentMenu.handleInput();
	}
}
