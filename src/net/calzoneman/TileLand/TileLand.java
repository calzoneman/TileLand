package net.calzoneman.TileLand;

import net.calzoneman.TileLand.gfx.Renderer;
import net.calzoneman.TileLand.gui.MenuManager;
import net.calzoneman.TileLand.tile.TileTypes;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class TileLand {	
	public static final String version = "0.20a_03";
	static ResourceManager rm;
	public static void main(String[] args) {
		if(!Renderer.init(640, 480))
			return;
		rm = new ResourceManager();
		Renderer.setFont(rm.getDefaultFont());
		TileTypes.init();
		MenuManager mm = MenuManager.getMenuManager();
		mm.openMenu("mainmenu");
		while(true){
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
			mm.render();
			Display.update();
			mm.handleInput();
			Display.sync(100);
			if(Display.isCloseRequested()) {
				Display.destroy();
				System.exit(0);
			}
		}
	}

	public static ResourceManager getResourceManager() {
		return rm;
	}
}
