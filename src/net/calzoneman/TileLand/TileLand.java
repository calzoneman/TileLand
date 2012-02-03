package net.calzoneman.TileLand;

import net.calzoneman.TileLand.gfx.Renderer;
import net.calzoneman.TileLand.level.BasicLevelGenerator;
import net.calzoneman.TileLand.level.Level;
import net.calzoneman.TileLand.player.Player;
import net.calzoneman.TileLand.tile.TileTypes;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

public class TileLand {
	public static final String version = "0.17a";
	static TextureManager tm;
	public static void main(String[] args) {
		StartupGUI s = new StartupGUI();
		while(!s.ready){
			try {
				Thread.sleep(50);
			}
			catch(Exception ex) {
				
			}
		}	
		if(!Renderer.init())
			return;
		tm = new TextureManager();
		TileTypes.init();
		// Load the player sprite
		Texture plyTexture = tm.getTexture("player.png");
		Level level;
		if(s.makeNewLevel) {
			level = new BasicLevelGenerator().generate(s.newMapSize.width, s.newMapSize.height);//new Level(s.newMapSize.width, s.newMapSize.height, s.selectedMapName);
			level.setName(s.selectedMapName);
		}
		else level = new Level(s.selectedMapName);
		Player ply = new Player(plyTexture, level, s.playerName);
		s.dispose();

		if(!ply.getLevel().initialized) {
			while(true) {
				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
				tm.getDefaultFont().drawString(0, 0, "Uh Oh!  The level is broken!", Color.red);
				tm.getDefaultFont().drawString(0, 10, "Make sure it's not an outdated file!", Color.red);
				Display.update();
				if(Display.isCloseRequested()) {
					Display.destroy();
					System.exit(0);
				}
			}
		}
		
		while(true) {
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
			Renderer.render(ply);
			Display.update();
			ply.handleInput();
			Display.sync(100);
			
			if(Display.isCloseRequested()) {
				Display.destroy();
				System.exit(0);
			}
		}
	}
	
	public static TextureManager getTextureManager() {
		return tm;
	}
}
