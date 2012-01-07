package net.calzoneman.TileLand;

import java.awt.AWTException;
import java.awt.BufferCapabilities;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.JFrame;

public class TileLand {

	private JFrame frame;
	private Screen screen;
	private InputHandler input;
	private Level level;
	private Player ply;
	
	public static void main(String[] args) {
		TileDefinitions.init();
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
		screen.addFocusListener(input);
		screen.requestFocusInWindow();
		
		frame.getContentPane().add(screen);
		frame.pack();
		frame.setVisible(true);
		
		GraphicsConfiguration config = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
		BufferCapabilities bufCapabilities = config.getBufferCapabilities();
		try {
			// Double buffer to prevent flicker
			screen.createBufferStrategy(2, bufCapabilities);
		} 
		catch (AWTException ex) {
			System.err.println("Error creating BufferStrategy");
			return;
		} 
		
		BufferStrategy bufStrategy = screen.getBufferStrategy();
		
		double nsPerTick = 1000000000.0 / 30; // 30 ticks per second
		long lastTime = System.nanoTime();
		long lastLog = System.currentTimeMillis();
		double unprocessed = 0;
		long ticks = 0;
		if(new File("saves/save.tl").exists()) {
			level = new Level("save.tl");
		}
		else {
			level = new Level(512, 512);
		}
		
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
				unprocessed--;
				ticks++;
			}
			// Render
			long rStart = System.nanoTime();
			
			Graphics g = bufStrategy.getDrawGraphics();

			// Render the game
			g.clearRect(0, 0, screen.getWidth(), screen.getHeight());
			render(g, ply, ticks);
			double rDiff = (System.nanoTime() - rStart) / 1000000.0;
			String fpsString = ("FPS: " + (int)(1000.0/rDiff));
			drawString(g, fpsString, 0, 0, Color.WHITE, Color.BLACK);
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
			level.render(g, renderDelta.x, renderDelta.y, screen.getWidth() / level.TILESIZE, screen.getHeight() / level.TILESIZE);
		}
		Graphics2D g2d = (Graphics2D)g;
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
			g2d.drawImage(ply.getSprite(), null, ply.getOffsetPosition().x * level.TILESIZE, ply.getOffsetPosition().y * level.TILESIZE);
		// Render player's name
		int x = (int)((ply.getOffsetPosition().x + .5) * level.TILESIZE) - (int)getStringBounds(g, ply.getName()).getWidth() / 2;
		int y = ply.getOffsetPosition().y * level.TILESIZE - (int)getStringBounds(g, ply.getName()).getHeight();
		drawString(g, ply.getName(), x, y, Color.BLACK, Color.WHITE);
	}

	public Rectangle2D getStringBounds(Graphics g, String str) {
		return g.getFontMetrics().getStringBounds(str, g);
	}
	
	public void drawString(Graphics g, String str, int x, int y, Color fg, Color bg) {
		FontMetrics fm = g.getFontMetrics();
		Rectangle2D bounds = fm.getStringBounds(str, g);
		g.setColor(bg);
		g.fillRect(x, y, (int)bounds.getWidth(), (int)bounds.getHeight());
		g.setColor(fg);
		g.drawString(str, x, y + (int)bounds.getHeight());
	}
}
