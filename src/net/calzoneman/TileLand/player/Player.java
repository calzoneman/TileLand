package net.calzoneman.TileLand.player;


import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.Color;

import net.calzoneman.TileLand.action.ActionResult;
import net.calzoneman.TileLand.gfx.PlayerSprite;
import net.calzoneman.TileLand.gfx.Renderable;
import net.calzoneman.TileLand.gfx.Renderer;
import net.calzoneman.TileLand.inventory.Item;
import net.calzoneman.TileLand.inventory.Inventory;
import net.calzoneman.TileLand.inventory.ItemStack;
import net.calzoneman.TileLand.inventory.PlayerInventory;
import net.calzoneman.TileLand.level.Level;
import net.calzoneman.TileLand.level.Location;
import net.calzoneman.TileLand.tile.Tile;
import net.calzoneman.TileLand.tile.TileId;
import net.calzoneman.TileLand.tile.TileTypes;

public class Player implements Renderable {
	/** The name of the Player */
	private String name;
	/** The player sprite */
	private PlayerSprite sprite;
	/** The Level in which the Player currently exists */
	private Level level;
	/** The Player's position within the level */
	private Location position;
	/** The current direction the player is facing */
	private int facing = PlayerSprite.FACING_DOWN;
	private PlayerInventory inventory;
	
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
		this.inventory = new PlayerInventory();
			inventory.addItemStack(new ItemStack(TileTypes.getTile(TileId.SNOWY_GRASS), 100));
			inventory.addItemStack(new ItemStack(TileTypes.getTile(TileId.COBBLESTONE_ROAD), 100));
			inventory.addItemStack(new ItemStack(TileTypes.getTile(TileId.BUSH), 100));
			inventory.addItemStack(new ItemStack(TileTypes.getTile(TileId.SIGN), 100));
	}
	
	public boolean move(int currentMoveKey) {
		String move = "";
		switch(currentMoveKey) {
			case Keyboard.KEY_W:
				if(position.y-1 < 0)
					break;
				Tile up = level.getFg(position.x, position.y-1);
				if(up == null || up.isSolid())
					break;
				move = "up";
				break;
			case Keyboard.KEY_S:
				if(position.y+1 >= level.getHeight())
					break;
				Tile down = level.getFg(position.x, position.y+1);
				if(down == null || down.isSolid())
					break;
				move = "down";
				break;
			case Keyboard.KEY_A:
				if(position.x-1 < 0)
					break;
				Tile left = level.getFg(position.x-1, position.y);
				if(left == null || left.isSolid())
					break;
				move = "left";
				break;
			case Keyboard.KEY_D:
				if(position.x+1 >= level.getWidth())
					break;
				Tile right = level.getFg(position.x+1, position.y);
				if(right == null || right.isSolid())
					break;
				move = "right";
				break;
			default:
				break;
		}
		if(!move.equals("")) {
			int oldFacing = this.facing;
			if(move.equals("up")) {
				position.y--;
				setFacing(PlayerSprite.FACING_UP);
			}
			else if(move.equals("right")) {
				position.x++;
				setFacing(PlayerSprite.FACING_RIGHT);
			}
			else if(move.equals("down")) {
				position.y++;
				setFacing(PlayerSprite.FACING_DOWN);
			}
			else if(move.equals("left")) {
				position.x--;
				setFacing(PlayerSprite.FACING_LEFT);
			}
			if(oldFacing == facing)
				sprite.nextFrame();
			return true;
		}
		else {
			sprite.resetFrame();
			return false;
		}
	}

	public PlayerSprite getSprite() {
		return sprite;
	}

	public void setSprite(Texture sprite) {
		this.sprite = new PlayerSprite(sprite);
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
		if(name.equals("calzoneman"))
			name = "«9calzoneman";
		this.name = name;
	}
	
	public Item getHeldItem() {
		return this.inventory.getQuickbar().getSelectedItemStack().getItem();
	}
	
	public PlayerInventory getPlayerInventory() {
		return this.inventory;
	}
	
	public Inventory getInventory() {
		return this.inventory;
	}

	@Override
	public void render(int x, int y) {
		// Draw the player sprite
		this.sprite.render(x, y - (PlayerSprite.PLAYER_HEIGHT - Level.TILESIZE));
	}
	
	public void renderNameCentered() {
		int w = Renderer.getFont().getWidth(name);
		int h = Renderer.getFont().getHeight(name);
		int x = Display.getWidth()/2  - w/2 + Level.TILESIZE/2;
		int y = Display.getHeight()/2 - h - Level.TILESIZE;
		Renderer.renderString(x, y, name, Color.black);
		/*
		Color.black.bind();
		glBegin(GL_QUADS);
			glVertex2f(x, y);
			glVertex2f(x+w, y);
			glVertex2f(x+w, y+h);
			glVertex2f(x, y+h);
		glEnd();
		glEnable(GL_BLEND);
		font.drawString(x, y, name);
		glDisable(GL_BLEND);*/
	}
	
	@Override
	public void render(int x, int y, int data) { }

	public int getFacing() {
		return facing;
	}

	public void setFacing(int facing) {
		this.facing = facing;
		this.sprite.setFacing(facing);
	}

}
