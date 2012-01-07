package net.calzoneman.TileLand;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Player {

	private Point position;
	private String name;
	private Level level;
	private BufferedImage sprite;
	private InputHandler input;
	private Tile currentFgTile;
	private Tile currentBgTile;
	private boolean editingFg;
	private Point levelDelta; // Offset within the level
	
	public Player() {
		this("Player", null, new Point(0, 0), null);
	}
	
	public Player(Level lvl) {
		this("Player", lvl, new Point(0, 0), null);
	}
	
	public Player(Level lvl, InputHandler input) {
		this("Player", lvl, new Point(0, 0), input);
	}
	
	public Player(String name, Level lvl, Point initPos, InputHandler input) {
		this.name = name;
		this.setPosition(initPos);
		this.level = lvl;
		this.input = input;
		loadDefaultSprite();
		this.currentFgTile = TileDefinitions.getFg(TileTypes.BG_GRASS1);
		this.currentBgTile = TileDefinitions.getBg(TileTypes.FG_TREE1);
		this.editingFg = false;
		this.setLevelDelta(new Point(0, 0));
	}
	
	public void loadDefaultSprite() {
		try {
			this.sprite = ImageIO.read(new File("res/pokeball.png"));
		}
		catch(IOException ex) {
			System.err.println("Failed to load sprite");
			this.sprite = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		}
	}
	
	public void handleInput() {
		if(input == null) return;
		// Movement
			boolean w = false, s = false, a = false, d = false;
			if(input.keyDownOnce(KeyEvent.VK_W)) w = true;
			else if(input.keyDownOnce(KeyEvent.VK_S)) s = true;
			else if(input.keyDownOnce(KeyEvent.VK_A)) a = true;
			else if(input.keyDownOnce(KeyEvent.VK_D)) d = true;
			
			// Hit detection
			if(w && !a && !d && position.y > 0 && !level.getFg(position.x, position.y-1).isSolid()) {
				position.y--;
				levelDelta.y--;
				level.setNeedsRedraw(true);
			}
			else if(s && !a && !d && position.y < level.getHeight()-1 && !level.getFg(position.x, position.y+1).isSolid()) {
				position.y++;
				levelDelta.y++;
				level.setNeedsRedraw(true);

			}
			else if(a && !w && !s && position.x > 0 && !level.getFg(position.x-1, position.y).isSolid()) {
				position.x--;
				levelDelta.x--;
				level.setNeedsRedraw(true);

			}
			else if(d && !w && !s && position.x < level.getWidth()-1 && !level.getFg(position.x+1, position.y).isSolid()) {
				position.x++;
				levelDelta.x++;
				level.setNeedsRedraw(true);

			}
		// Placement
		if(input.keyDownOnce(KeyEvent.VK_F)) {
			this.editingFg = !this.editingFg;
		}
		
		if(input.keyDownOnce(KeyEvent.VK_E)) {
			if(editingFg && TileDefinitions.getFg(currentFgTile.getId() + 1) != TileDefinitions.NULLFGTILE) {
				currentFgTile = TileDefinitions.getFg(currentFgTile.getId() + 1);
			}
			else if(!editingFg && TileDefinitions.getBg(currentBgTile.getId() + 1) != TileDefinitions.NULLBGTILE) {
				currentBgTile = TileDefinitions.getBg(currentBgTile.getId() + 1);
			}
		}
		else if(input.keyDownOnce(KeyEvent.VK_Q)) {
			if(editingFg && TileDefinitions.getFg(currentFgTile.getId() - 1) != TileDefinitions.NULLFGTILE) {
				currentFgTile = TileDefinitions.getFg(currentFgTile.getId() - 1);
			}
			else if(!editingFg && TileDefinitions.getBg(currentBgTile.getId() - 1) != TileDefinitions.NULLBGTILE) {
				currentBgTile = TileDefinitions.getBg(currentBgTile.getId() - 1);
			}
		}
		
		
		if(input.mouseButtonDown(1)) {
			Point mPos = input.getMousePosition();
			mPos.x = mPos.x / level.TILESIZE + levelDelta.x;
			mPos.y = mPos.y / level.TILESIZE + levelDelta.y;
			
			if(editingFg) {
				if(mPos.x != position.x && mPos.y != position.y) {
					level.setFg(mPos.x, mPos.y, currentFgTile);
				}
			}
			else {
				level.setBg(mPos.x, mPos.y, currentBgTile);
			}
		}
		else if(input.mouseButtonDown(3)) {
			Point mPos = input.getMousePosition();
			mPos.x = mPos.x / level.TILESIZE + levelDelta.x;
			mPos.y = mPos.y / level.TILESIZE + levelDelta.y;
			if(editingFg) {
				level.setFg(mPos.x, mPos.y, TileDefinitions.DEFAULTFG);
			}
			else {
				level.setBg(mPos.x, mPos.y, TileDefinitions.DEFAULTBG);
			}
		}
	}

	public String getName() {
		return this.name;
	}
	
	public BufferedImage getSprite() {
		return sprite;
	}

	public void setSprite(BufferedImage sprite) {
		this.sprite = sprite;
	}

	public Point getPosition() {
		return position;
	}
	
	public Point getOffsetPosition() {
		return new Point(position.x - levelDelta.x, position.y - levelDelta.y);
	}

	public void setPosition(Point position) {
		this.position = position;
	}

	public Point getLevelDelta() {
		return new Point(levelDelta.x, levelDelta.y);
	}

	public void setLevelDelta(Point levelDelta) {
		this.levelDelta = levelDelta;
	}
	
	public Tile getCurrentFgTile() {
		return this.currentFgTile;
	}
	
	public Tile getCurrentBgTile() {
		return this.currentBgTile;
	}
	
	public boolean isEditingFg() {
		return this.editingFg;
	}
	
	public boolean canPlace(Point p) {
		if(p.x == position.x && p.y == position.y && editingFg) {
			return false;
		}
		if(p.x < 0 || p.x >= level.getWidth() || p.y < 0 || p.y >= level.getHeight()) {
			return false;
		}
		return true;
	}
	
	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}
}
