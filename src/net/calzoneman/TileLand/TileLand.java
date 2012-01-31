package net.calzoneman.TileLand;

import net.calzoneman.TileLand.gfx.Renderer;
import net.calzoneman.TileLand.level.Level;
import net.calzoneman.TileLand.player.Player;
import net.calzoneman.TileLand.tile.TileTypes;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

public class TileLand {
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
		if(s.makeNewLevel) level = new Level(s.newMapSize.width, s.newMapSize.height, s.selectedMapName);
		else level = new Level(s.selectedMapName);
		Player ply = new Player(plyTexture, level, s.playerName);
		s.dispose();
		
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
