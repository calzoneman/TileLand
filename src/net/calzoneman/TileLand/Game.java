package net.calzoneman.TileLand;


import org.lwjgl.opengl.Display;

import net.calzoneman.TileLand.player.Player;
import net.calzoneman.TileLand.screen.ChatScreen;
import net.calzoneman.TileLand.screen.GameScreen;
import net.calzoneman.TileLand.screen.MainScreen;

public class Game {
	protected Player player;
	protected MainScreen mainScreen;
	protected GameScreen currentScreen = null;
	protected ChatScreen chatScreen;
	
	public Game(Player player) {
		this.player = player;
		this.mainScreen = new MainScreen(this);
		this.chatScreen = new ChatScreen(10, Display.getHeight() - 180, 480, 180);
		chatScreen.setParent(this);
	}
	
	public boolean isMultiplayer() {
		return false;
	}
	
	public void openChat() {
		chatScreen.setActive(true);
		if(currentScreen != null)
			closeScreen();
		mainScreen.setActive(false);
	}
	
	public void openScreen(GameScreen scr) {
		scr.setParent(this);
		// Turn over input to the screen
		scr.setActive(true);
		this.currentScreen = scr;
		mainScreen.setActive(false);
	}
	
	public void closeScreen() {
		if(currentScreen != null) {
			currentScreen.resetInput();
			currentScreen.onClosing();
		}
		currentScreen = null;
		mainScreen.setActive(true);
		mainScreen.resetInput();

	}

	public void tick() {
		if(mainScreen.isActive()) {
			mainScreen.handleInput();
		}
		else if(chatScreen.isActive()) {
			chatScreen.handleInput();
			if(!chatScreen.isActive()) {
				chatScreen.resetInput();
				mainScreen.setActive(true);
				mainScreen.resetInput();
			}
		}
		else if(currentScreen != null && currentScreen.isActive()) {
			currentScreen.handleInput();
		}
		else if(currentScreen != null) {
			closeScreen();
		}
	}

	public void render() {
		mainScreen.render();
		chatScreen.render();
		if(currentScreen != null && currentScreen.isActive())
			currentScreen.render();
	}
	
	public Player getPlayer() {
		return player;
	}
}
