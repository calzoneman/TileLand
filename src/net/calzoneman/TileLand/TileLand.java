package net.calzoneman.TileLand;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class TileLand {

	private JFrame frame;
	private Screen screen;
	private InputHandler input;
	private Level level;
	private Player ply;
	
	public static void main(String[] args) {
		new TileLand().run();
	}
	
	public TileLand() {
		
	}
	
	public void run() {
		// Set up an application frame
		frame = new JFrame("TileLand");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		input = new InputHandler();
		
		// Set up a rendering canvas
		screen = new Screen();
		screen.addMouseListener(input);
		screen.addMouseMotionListener(input);
		screen.addKeyListener(input);
		screen.requestFocusInWindow();
		
		frame.getContentPane().add(screen);
		frame.pack();
		frame.setVisible(true);
		
		screen.createBufferStrategy(2); // Double buffer to prevent flicker
		BufferStrategy bufStrategy = screen.getBufferStrategy();
		
		double nsPerTick = 1000000000.0 / 30; // 30 ticks per second
		long lastTime = System.nanoTime();
		long lastLog = System.currentTimeMillis();
		double unprocessed = 0;
		long ticks = 0;
		level = new Level(64, 64);
		level.save();
		Point accumDelta = new Point(0, 0); // In pixels, accumulates deltas from input handler
		
		ply = new Player("Player", level, new Point(1, 1), input);
		ply.setLevelDelta(new Point(ply.getLevelDelta().x - screen.getWidth() / level.TILESIZE / 2,
				ply.getLevelDelta().y - screen.getHeight() / level.TILESIZE / 2));
		
		// Main loop
		while(true) {
			// Limit tick rate
			long now = System.nanoTime();
			unprocessed += (now - lastTime) / nsPerTick;
			lastTime = now;
			while(unprocessed >= 1) {
				// Poll for user input
				input.poll();
				ply.handleInput();
				/*if(input.mouseButtonDown(1)) {
					Point mDelta = input.getMouseDelta();
					Location newDelta = new Location(accumDelta.x - mDelta.x, accumDelta.y - mDelta.y);
					if(newDelta.x >= 0 && newDelta.x < Chunk.WIDTH * Chunk.TILESIZE) {
						accumDelta.x = newDelta.x;
					}
					if(newDelta.y >= 0 && newDelta.y < Chunk.HEIGHT * Chunk.TILESIZE) {
						accumDelta.y = newDelta.y;
					}
					renderDelta.x = accumDelta.x / Chunk.TILESIZE;
					renderDelta.y = accumDelta.y / Chunk.TILESIZE;
				}*/
				// Screen scrolling
				Point lDelta = ply.getLevelDelta();
				if(ply.getPosition().x >= lDelta.x + (screen.getWidth() / Level.TILESIZE) - 1) {
					lDelta.x++;
				}
				else if(ply.getPosition().x < lDelta.x + 1) {
					lDelta.x--;
				}
				
				if(ply.getPosition().y >= lDelta.y + (screen.getHeight() / Level.TILESIZE) - 1) {
					lDelta.y++;
				}
				else if(ply.getPosition().y < lDelta.y + 1) {
					lDelta.y--;
				}
				ply.setLevelDelta(lDelta);
				unprocessed--;
				ticks++;
			}
			// Render
			long rStart = System.nanoTime();
			
			Graphics g = bufStrategy.getDrawGraphics();
			Graphics2D g2d = (Graphics2D)g;
			// Clear the screen
			screen.update(g); 
			// Render the current chunk
			render(g, ply, ticks);
			// Draw the player
			//g2d.drawImage(ply.getSprite(), null, (ply.getPosition().x - ply.getChunkDelta().x) * Chunk.TILESIZE, 
			//									 (ply.getPosition().y - ply.getChunkDelta().y) * Chunk.TILESIZE);
			
			double rDiff = (System.nanoTime() - rStart) / 1000000.0;
			char[] fpsString = ("FPS: " + 1000.0/rDiff).toCharArray();
			g.drawChars(fpsString, 0, fpsString.length, 0, 10);
			g.dispose();
			bufStrategy.show();
			if(System.currentTimeMillis() - lastLog > 1000) {
				System.out.println(ticks + " ticks");
				ticks = 0;
				lastLog = System.currentTimeMillis();
			}
			try {
				Thread.sleep(2);
			}
			catch(Exception e) {
				
			}
		}
	}

	public void render(Graphics g, Player ply, long ticks) {
		Level level = ply.getLevel();
		Point renderDelta = ply.getLevelDelta();
		if(level != null) {
			level.render(g, renderDelta.x, renderDelta.y, level.getWidth(), level.getHeight());
		}
		Graphics2D g2d = (Graphics2D)g;
		g2d.drawImage(ply.getSprite(), null, ply.getOffsetPosition().x * level.TILESIZE, ply.getOffsetPosition().y * level.TILESIZE);
		g2d.drawChars(ply.getName().toCharArray(), 0, ply.getName().length(), ply.getOffsetPosition().x * level.TILESIZE, ply.getOffsetPosition().y * level.TILESIZE - 10);
		// Render player's current tile
			Point mPos = input.getMousePosition();
			boolean canPlace = ply.canPlace(new Point(mPos.x / level.TILESIZE + renderDelta.x, mPos.y / level.TILESIZE + renderDelta.y));
			// Round to the nearest TILESIZE
			mPos.x = (mPos.x / level.TILESIZE) * level.TILESIZE;
			mPos.y = (mPos.y / level.TILESIZE) * level.TILESIZE;
			
			BufferedImage im = null;
			if(ply.isEditingFg()) {
				im = ply.getCurrentFgTile().getTextureClone();
			}
			else {
				im = ply.getCurrentBgTile().getTextureClone();
			}
			
			if(im != null) {
				Graphics2D im2d = (Graphics2D)im.getGraphics();
				if(canPlace) {
					im2d.setColor(new Color(0, 255, 0, 80));
				}
				else {
					im2d.setColor(new Color(255, 0, 0, 80));
				}
				im2d.fillRect(0, 0, im.getWidth(), im.getHeight());
				g2d.drawImage(im, null, mPos.x, mPos.y);
			}
			g2d.drawRect(mPos.x, mPos.y, level.TILESIZE, level.TILESIZE);
	}

}
