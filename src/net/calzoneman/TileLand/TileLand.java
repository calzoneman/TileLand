package net.calzoneman.TileLand;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.atomic.AtomicReference;

import net.calzoneman.TileLand.gfx.Renderer;
import net.calzoneman.TileLand.gui.GUIMenu;
import net.calzoneman.TileLand.gui.MenuManager;
import net.calzoneman.TileLand.tile.TileTypes;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class TileLand {	
	public static final String version = "0.21a";
	static AtomicReference<Dimension> newSize = new AtomicReference<Dimension>();
	static boolean closeRequested = false;
	static final Dimension DEFAULT_DIMENSION = new Dimension(640, 480);
	static boolean maximized = false;
	
	static ResourceManager rm;
	public static void main(String[] args) {		
		final Frame frame = new Frame("Tileland " + version);
		frame.setLayout(new BorderLayout());
		final Canvas canvas = new Canvas();
		canvas.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				newSize.set(canvas.getSize());
			}
		});
		
		frame.addWindowFocusListener(new WindowAdapter() {
			@Override
			public void windowGainedFocus(WindowEvent e) {
				canvas.requestFocusInWindow();
			}
		});
		
		frame.addWindowListener(new WindowAdapter() {
	        @Override
	        public void windowClosing(WindowEvent e) { 
	        	closeRequested = true; 
	        }
	     });
		
		frame.setResizable(true);
		frame.add(canvas, BorderLayout.CENTER);
		canvas.setPreferredSize(DEFAULT_DIMENSION);
		canvas.setMinimumSize(DEFAULT_DIMENSION);
		frame.setVisible(true);
		frame.pack();
		
		try {
			Display.setParent(canvas);
			if(!Renderer.init(DEFAULT_DIMENSION.width, DEFAULT_DIMENSION.height)) {
				Sys.alert("TileLand", "Faild to initialize renderer.");
				Display.destroy();
				frame.dispose();
				return;
			}
			rm = new ResourceManager();
			Renderer.setFont(rm.getDefaultFont());
			TileTypes.init();
		} 
		catch (LWJGLException e1) {
			Sys.alert("Tileland", "Error initializing AWT Canvas");
		}
		
		MenuManager mm = MenuManager.getMenuManager();
		mm.openMenu("mainmenu");
		Dimension newDim;
		while(true){
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
			mm.render();
			Renderer.renderFPS();
			Display.update();
			mm.handleInput();
			Display.sync(100);
			
			newDim = newSize.getAndSet(null);
			if(newDim != null) {
				if((frame.getExtendedState() & Frame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH
						&& maximized) {
					newDim = DEFAULT_DIMENSION;
					maximized = false;
				}
				else if((frame.getExtendedState() & Frame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH) {
					maximized = true;
				}
				// Make sure the new dimensions are multiples of 32
				if(newDim.width % 32 != 0)
					newDim.width = (newDim.width / 32) * 32;
				if(newDim.height % 32 != 0)
					newDim.height = (newDim.height / 32) * 32;
				canvas.setPreferredSize(newDim);
				frame.pack();
				Renderer.reInit(newDim.width, newDim.height);
				GUIMenu menu = mm.getCurrent();
				if(menu != null)
					menu.reInit(0, 0, newDim.width, newDim.height);
			}
			
			if(Display.isCloseRequested() || closeRequested) {
				Display.destroy();
				frame.dispose();
				System.exit(0);
			}
		}
	}

	public static ResourceManager getResourceManager() {
		return rm;
	}
}
