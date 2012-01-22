package net.calzoneman.TileLand.player;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.opengl.Texture;

import net.calzoneman.TileLand.level.Level;
import net.calzoneman.TileLand.level.Location;
import net.calzoneman.TileLand.tile.Tile;
import net.calzoneman.TileLand.tile.TileTypes;

public class Player {
	/** The name of the Player */
	private String name;
	/** The Texture of the player sprite */
	private Texture sprite;
	/** The Level in which the Player currently exists */
	private Level level;
	/** The Player's position within the level */
	private Location position;
	/** The current background Tile the Player is holding */
	private Tile currentBg;
	/** The current foreground Tile the Player is holding */
	private Tile currentFg;
	/** Whether the player is currently editing the foreground */
	private boolean editingFg;
	/** An array keeping the key states of the keyboard */
	private boolean[] keys;
	/** An array keeping the button states of the mouse */
	private boolean[] mouse;
	/** The keycode of the current movement key being pressed, or -1 if none are pressed */
	private int currentMoveKey;
	/** The time, in milliseconds, since the last movement */
	private long lastMoveTime;
	
	/**
	 * Parameterless constructor
	 */
	public Player() {
		this(null, null, "unnamed player", new Location(0, 0));
	}
	
	/**
	 * Constructor which sets the level and sets the position to the spawnpoint of the level
	 * @param level The level to place the Player into
	 */
	public Player(Level level) {
		this(null, level, "unnamed player", level.getSpawnpoint());
	}
	
	/**
	 * Constructor which sets the sprite, and level, and sets the position to the spawnpoint of the level
	 * @param sprite The Texture of the Player
	 * @param level The Level to place the player into
	 * @param name The name of the Player
	 */
	public Player(Texture sprite, Level level) {
		this(sprite, level, "unnamed player", level.getSpawnpoint());
	}
	
	/**
	 * Constructor which sets the sprite, name, and level, and sets the position to the spawnpoint of the level
	 * @param sprite The Texture of the Player
	 * @param level The Level to place the player into
	 * @param name The name of the Player
	 */
	public Player(Texture sprite, Level level, String name) {
		this(sprite, level, name, level.getSpawnpoint());
	}
	
	/**
	 * Constructor which sets the sprite, level, name, and position
	 * @param sprite The Texture of the Player
	 * @param level The Level to place the player into
	 * @param name The name of the Player
	 * @param position The position of the Player
	 */
	public Player(Texture sprite, Level level, String name, Location position) {
		this.setSprite(sprite);
		this.setLevel(level);
		this.setName(name);
		this.setPosition(position);
		this.currentBg = TileTypes.getDefaultBg();
		this.currentFg = TileTypes.getFgTile("tree1");
		this.editingFg = false;
		this.keys = new boolean[Keyboard.getKeyCount()];
		this.mouse = new boolean[Mouse.getButtonCount()];
		this.currentMoveKey = -1;
		this.lastMoveTime = 0;
	}
	
	public void handleInput() {
		// Handle mouse events
		while(Mouse.next()) {
			// Update mouse button state
			if(Mouse.getEventButton() != -1)
				mouse[Mouse.getEventButton()] = Mouse.getEventButtonState();
			// Tile placement
			placeTile(Mouse.getEventX(), Mouse.getEventY());
		}

		while(Keyboard.next()) {
			if(Keyboard.getEventKey() != -1) {
				// Update the key state
				keys[Keyboard.getEventKey()] = Keyboard.getEventKeyState();
				// Update the current movement key if applicable
				if(Keyboard.getEventKeyState()) {
					switch(Keyboard.getEventKey()) {
						case Keyboard.KEY_W:
						case Keyboard.KEY_S:
						case Keyboard.KEY_A:
						case Keyboard.KEY_D:
							currentMoveKey = Keyboard.getEventKey();
							break;
						default:
							break;
					}
				}
			}
			if(!keys[Keyboard.KEY_W] && !keys[Keyboard.KEY_S] && !keys[Keyboard.KEY_A] && !keys[Keyboard.KEY_D])
				currentMoveKey = -1;
			// Switch between foreground and background
			if(keys[Keyboard.KEY_F]) {
				editingFg = !editingFg;
			}
			// Switch to the next background/foreground tile
			if(keys[Keyboard.KEY_E]) {
				if(editingFg) {
					if(TileTypes.getFgTile(currentFg.getId()+1) != null)
						currentFg = TileTypes.getFgTile(currentFg.getId()+1);
				}
				else {
					if(TileTypes.getBgTile(currentBg.getId()+1) != null) 
						currentBg = TileTypes.getBgTile(currentBg.getId()+1);
				}
			}
			// Switch to the previous background/foreground tile
			if(keys[Keyboard.KEY_Q]) {
				if(editingFg) {
					if(TileTypes.getFgTile(currentFg.getId()-1) != null)
						currentFg = TileTypes.getFgTile(currentFg.getId()-1);
				}
				else {
					if(TileTypes.getBgTile(currentBg.getId()-1) != null) 
						currentBg = TileTypes.getBgTile(currentBg.getId()-1);
				}
			}
			// Saving
			if(keys[Keyboard.KEY_LCONTROL] && keys[Keyboard.KEY_S]) {
				level.save();
				return; // Don't let Ctrl+S also make the player move!
			}
		}
		// Handle movement
		if(System.currentTimeMillis() >= lastMoveTime + 100 || keys[Keyboard.KEY_LSHIFT])
			move();
	}

	private void placeTile(int mouseX, int mouseY) {
		if(!mouse[0] && !mouse[1])
			return;
		Location offset = new Location(
				position.x - Display.getWidth() / Level.TILESIZE / 2,
				position.y - Display.getHeight() / Level.TILESIZE / 2);
		int tx = mouseX / Level.TILESIZE + offset.x;
		int ty = (Display.getHeight() - mouseY) / Level.TILESIZE + offset.y;
		if(!(tx < 0 || tx >= level.getWidth() || ty < 0 || ty >= level.getHeight())) {
			// Don't let the player place a solid tile on top of themselves!
			if(!(tx == position.x && ty == position.y && (editingFg && currentFg.isSolid()))) {
				if(editingFg) {
					if(mouse[0])
						level.setFg(tx, ty, currentFg);
					else if(mouse[1])
						level.setFg(tx, ty, TileTypes.getDefaultFg());
				}
				else {
					if(mouse[0])
						level.setBg(tx, ty, currentBg);
					else if(mouse[1])
						level.setBg(tx, ty, TileTypes.getDefaultBg());
				}
			}
		}
	}
	
	private void move() {
		switch(currentMoveKey) {
		case Keyboard.KEY_W:
			if(position.y-1 < 0)
				break;
			Tile up = level.getFg(position.x, position.y-1);
			if(up == null || up.isSolid())
				break;
			position.y--;
			lastMoveTime = System.currentTimeMillis();
			placeTile(Mouse.getX(), Mouse.getY());
			break;
		case Keyboard.KEY_S:
			if(position.y+1 >= level.getHeight())
				break;
			Tile down = level.getFg(position.x, position.y+1);
			if(down == null || down.isSolid())
				break;
			position.y++;
			lastMoveTime = System.currentTimeMillis();
			placeTile(Mouse.getX(), Mouse.getY());
			break;
		case Keyboard.KEY_A:
			if(position.x-1 < 0)
				break;
			Tile left = level.getFg(position.x-1, position.y);
			if(left == null || left.isSolid())
				break;
			position.x--;
			lastMoveTime = System.currentTimeMillis();
			placeTile(Mouse.getX(), Mouse.getY());
			break;
		case Keyboard.KEY_D:
			if(position.x+1 >= level.getWidth())
				break;
			Tile right = level.getFg(position.x+1, position.y);
			if(right == null || right.isSolid())
				break;
			position.x++;
			lastMoveTime = System.currentTimeMillis();
			placeTile(Mouse.getX(), Mouse.getY());
			break;
		default:
			break;
	}
	}
	
	public Texture getSprite() {
		return sprite;
	}

	public void setSprite(Texture sprite) {
		this.sprite = sprite;
	}

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public Location getPosition() {
		return position;
	}

	public void setPosition(Location position) {
		this.position = new Location(position); // Create a new instance of Location so we aren't modifying the original input
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Tile getCurrentTile() {
		if(editingFg)
			return this.currentFg;
		else
			return this.currentBg;
	}
	
	public Tile getCurrentBg() {
		return this.currentBg;
	}
	
	public Tile getCurrentFg() {
		return this.currentFg;
	}
	
	public boolean isEditingFg() {
		return this.editingFg;
	}
}
