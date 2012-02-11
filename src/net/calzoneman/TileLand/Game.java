package net.calzoneman.TileLand;


import net.calzoneman.TileLand.gui.GameScreen;
import net.calzoneman.TileLand.gui.MainScreen;
import net.calzoneman.TileLand.player.Player;

public class Game {
	private Player player;
	private MainScreen mainScreen;
	private GameScreen currentScreen = null;
	
	
	public Game(Player player) {
		this.player = player;
		this.mainScreen = new MainScreen(this);
	}
	
	public void openScreen(GameScreen scr) {
		scr.setParent(this);
		// Turn over input to the screen
		scr.active = true;
		this.currentScreen = scr;
		mainScreen.active = false;
	}
	
	public void closeScreen() {
		if(currentScreen != null)
			currentScreen.onClosing();
		currentScreen = null;
		mainScreen.active = true;
	}

	public void handleInput() {
		if(mainScreen.active) {
			mainScreen.handleInput();
		}
		else if(currentScreen != null && currentScreen.active) {
			currentScreen.handleInput();
		}
		else if(currentScreen != null)
			mainScreen.active = true;
	}

	public void render() {
		mainScreen.render();
		if(currentScreen != null && currentScreen.active)
			currentScreen.render();
	}
	
	public Player getPlayer() {
		return player;
	}
}
