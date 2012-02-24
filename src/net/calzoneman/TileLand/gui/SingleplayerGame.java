package net.calzoneman.TileLand.gui;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

import net.calzoneman.TileLand.Game;
import net.calzoneman.TileLand.TileLand;
import net.calzoneman.TileLand.gfx.Renderer;
import net.calzoneman.TileLand.gui.NewLevelMenu.GameParameters;
import net.calzoneman.TileLand.level.BasicLevelGenerator;
import net.calzoneman.TileLand.level.Level;
import net.calzoneman.TileLand.player.Player;

public class SingleplayerGame extends GUIMenu {
	
	private Game game = null;
	private boolean levelError = false;
	
	public SingleplayerGame(GameParameters gp) {
		if(gp == null)
			gp = GameParameters.defaultGameParameters;
		Level lvl = new BasicLevelGenerator().generate(gp.width, gp.height);
		lvl.setName(gp.levelName);
		Player ply = new Player(TileLand.getResourceManager().getTexture("res/player/default.png"), lvl, gp.playerName);
		game = new Game(ply);
	}
	
	public SingleplayerGame(String lvlName, String playerName) {
		Level lvl = new Level(lvlName);
		levelError = !lvl.initialized;
		if(levelError)
			return;
		Player ply = new Player(TileLand.getResourceManager().getTexture("res/player/default.png"), lvl, playerName);
		game = new Game(ply);
	}
	
	@Override
	public void render() {
		if(game != null)
			game.render();
		else if(levelError) {
			Renderer.renderScreenCenteredString(Display.getHeight()/2, "Uh Oh!  The level is broken!", Color.red);
			Renderer.renderScreenCenteredString(Display.getHeight()/2 + 10, "Make sure it's not an outdated file!", Color.red);
		}
	}
	
	@Override
	public void handleInput() {
		if(game != null)
			game.tick();
	}
}
