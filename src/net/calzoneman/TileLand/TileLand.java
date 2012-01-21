package net.calzoneman.TileLand;

import net.calzoneman.TileLand.gfx.Renderer;
import net.calzoneman.TileLand.level.Level;
import net.calzoneman.TileLand.player.Player;
import net.calzoneman.TileLand.tile.TileTypes;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class TileLand {
	public static void main(String[] args) {
		TileTypes.init();
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
		// Load the player sprite
		Texture plyTexture = null;
		try {
			plyTexture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/player.png"));
		}
		catch(Exception ex) {
			System.out.println("Error: Player texture could not be loaded");
		}
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
}
