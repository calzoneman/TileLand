package net.calzoneman.TileLand.gui;

import net.calzoneman.TileLand.MultiplayerGame;
import net.calzoneman.TileLand.player.Player;

public class MultiplayerGameMenu extends GUIMenu {
	private MultiplayerGame game;
	
	public MultiplayerGameMenu(Player ply, String ip, int port) {
		game = new MultiplayerGame(ply, ip, port);
	}
	
	@Override
	public void handleInput() {
		game.tick();
	}
	
	@Override
	public void render() {
		game.render();
	}
}
