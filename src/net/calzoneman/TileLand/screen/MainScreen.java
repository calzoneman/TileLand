package net.calzoneman.TileLand.screen;

import net.calzoneman.TileLand.Game;
import net.calzoneman.TileLand.event.EventManager;
import net.calzoneman.TileLand.gfx.Renderer;
import net.calzoneman.TileLand.inventory.ItemStack;
import net.calzoneman.TileLand.inventory.TileItem;
import net.calzoneman.TileLand.level.Level;
import net.calzoneman.TileLand.level.Location;
import net.calzoneman.TileLand.player.Player;
import net.calzoneman.TileLand.tile.Tile;
import net.calzoneman.TileLand.tile.TypeId;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

public class MainScreen extends GameScreen {
	
	private int currentMoveKey;
	private long lastMoveTime;
	
	static final long CLICK_DELAY = 50000000L;
	
	/** Colors for Mouse overlay */
	private static Color transparentRed = new Color(255, 0, 0, 130);
	private static Color transparentGreen = new Color(0, 255, 0, 130);
	
	public MainScreen(Game parent) {
		super(0, 0, Display.getWidth(), Display.getHeight(), parent);
		this.currentMoveKey = -1;
		this.lastMoveTime = 0;
	}
	
	@Override
	public void handleInput() {
		handleMouse();
		handleKeyboard();
	}

	public void handleMouse() {
		Player ply = parent.getPlayer();
		while(Mouse.next()) {
			// Update mouse button state
			if(Mouse.getEventButton() != -1)
				mouse[Mouse.getEventButton()] = Mouse.getEventButtonState();
			click(ply, Mouse.getEventX(), Mouse.getEventY());
		}
	}

	public void handleKeyboard() {
		Player ply = parent.getPlayer();
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
			if(currentMoveKey != -1 && !keys[currentMoveKey]) {
				if(keys[Keyboard.KEY_W])
					currentMoveKey = Keyboard.KEY_W;
				else if(keys[Keyboard.KEY_S])
					currentMoveKey = Keyboard.KEY_S;
				else if(keys[Keyboard.KEY_A])
					currentMoveKey = Keyboard.KEY_A;
				else if(keys[Keyboard.KEY_D])
					currentMoveKey = Keyboard.KEY_D;
			}
			// Switch to the next background/foreground tile
			if(keys[Keyboard.KEY_E]) {
				ply.getPlayerInventory().getQuickbar().nextSlot();
			}
			// Switch to the previous background/foreground tile
			if(keys[Keyboard.KEY_Q]) {
				ply.getPlayerInventory().getQuickbar().prevSlot();
			}
			// Set spawnpoint
			if(keys[Keyboard.KEY_RETURN])
				ply.getLevel().setSpawnpoint(ply.getPosition());
			// Return to spawnpoint
			if(keys[Keyboard.KEY_R])
				ply.setPosition(ply.getLevel().getSpawnpoint());
			if(keys[Keyboard.KEY_ESCAPE]) 
				parent.openScreen(new InventoryScreen());
			if(keys[Keyboard.KEY_T])
				parent.openChat();
			// Saving
			if(keys[Keyboard.KEY_LCONTROL] && keys[Keyboard.KEY_S]) {
				ply.getLevel().save();
				return; // Don't let Ctrl+S also make the player move!
			}
		}
		// Handle movement
		if(System.currentTimeMillis() >= lastMoveTime + 100 || keys[Keyboard.KEY_LSHIFT]) {
			if(ply.move(currentMoveKey)) {
				EventManager.manager.onPlayerMove(parent, ply.getPosition(), ply.getFacing());
				click(ply, Mouse.getX(), Mouse.getY());
				lastMoveTime = System.currentTimeMillis();
			}
		}
	}
	
	private void click(Player ply, int mouseX, int mouseY) {
		if(!mouse[0] && !mouse[1])
			return;
		Location position = ply.getTilePosition();
		Level level = ply.getLevel();
		// Mouse offset in the level
		Location offset = new Location(
				position.x - Display.getWidth() / Tile.TILESIZE / 2,
				position.y - Display.getHeight() / Tile.TILESIZE / 2);
		// tx and ty are the coordinates for the tile under the mouse cursor
		int tx = mouseX / Tile.TILESIZE + offset.x;
		int ty = (Display.getHeight() - mouseY) / Tile.TILESIZE + offset.y;
		
		if(tx < 0 || ty < 0 || tx >= level.getWidth() || ty >= level.getHeight())
			return;
		
		if(mouse[1]) {
			int fg = level.getFgId(tx, ty);
			if(fg != -1 && fg != TypeId.AIR) {
				level.getFg(tx, ty).hit(level, ply, ply.getHeldItem(), tx, ty);
			}
			else {
				level.getBg(tx, ty).hit(level, ply, ply.getHeldItem(), tx, ty);
			}
		}
		else if(mouse[0] && tx >= 0 && ty >= 0 && tx < level.getWidth() && ty < level.getHeight()) {
			if(ply.getHeldItem() instanceof TileItem) {
				TileItem it = (TileItem) ply.getHeldItem();
				if(it.getTile().isForeground()) {
					if(level.getFgId(tx, ty) == TypeId.AIR && tx != ply.getTilePosition().x && ty != ply.getTilePosition().y) { 
						level.setFg(tx, ty, it.getTile());
						level.setFgData(tx, ty, it.getData());
						ply.getInventory().removeOneItem(ply.getPlayerInventory().getQuickbar().getSelectedSlot());
					}
				}
				else {
					if(level.getBgId(tx, ty) != it.getTile().id) {
						level.setBg(tx, ty, it.getTile());
						level.setBgData(tx, ty, it.getData());
						ply.getInventory().removeOneItem(ply.getPlayerInventory().getQuickbar().getSelectedSlot());
					}
				}
			}
		}
	}

	@Override
	public void render() {
		Player player = parent.getPlayer();
		Renderer.renderFilledRect(0, 0, Display.getWidth(), Display.getHeight(), Color.red);
		Level level = player.getLevel();
		Location position = player.getTilePosition();
		// Calculate at what offset to begin rendering the level
		Location renderStart = new Location(
				position.x - Display.getWidth() / Tile.TILESIZE / 2,
				position.y - Display.getHeight() / Tile.TILESIZE / 2);
		// Render the level
		if(level != null) {
			// Background
			renderLevel(renderStart.x, renderStart.y, Display.getWidth() / Tile.TILESIZE, Display.getHeight() / Tile.TILESIZE);
		}
		// Render the player sprite
		player.render((position.x - renderStart.x) * Tile.TILESIZE, (position.y - renderStart.y) * Tile.TILESIZE);
		// Render the mouse
		if(active)
			renderMouse(renderStart);
		// Render the HUD
		player.getPlayerInventory().getQuickbar().render();
	}
	
	private void renderMouse(Location renderStart) {
		Player player = parent.getPlayer();
		Level level = player.getLevel();
		ItemStack current = null;
		Color col = transparentGreen;
		int tx = Mouse.getX() / Tile.TILESIZE + renderStart.x;
		int ty = (Display.getHeight() - Mouse.getY()) / Tile.TILESIZE + renderStart.y;
		if(player.getPlayerInventory().getQuickbar().getSelectedItemStack() == null // Selected item is null
				|| (tx == player.getPosition().x && ty == player.getPosition().y) // Cursor is over the player
				|| tx < 0 || tx >= level.getWidth() || ty < 0 || ty >= level.getHeight()) // Mouse it outside the bounds of the Level
			col = transparentRed;
		current = player.getPlayerInventory().getQuickbar().getSelectedItemStack();
		
		int mx = Mouse.getX();
		int my = Mouse.getY();
		tx = mx / Tile.TILESIZE;
		ty = (Display.getHeight() - my) / Tile.TILESIZE;
		if(current != null) {
			col.bind();
			current.getItem().render(tx * Tile.TILESIZE, ty * Tile.TILESIZE);
		}
		else {
			Renderer.renderFilledRect(tx * Tile.TILESIZE, ty * Tile.TILESIZE, Tile.TILESIZE, Tile.TILESIZE, col);
		}
		// Draw border
		Renderer.renderRect(tx * Tile.TILESIZE, ty * Tile.TILESIZE, Tile.TILESIZE, Tile.TILESIZE, Color.black);
	}
	
	private void renderLevel(int offX, int offY, int maxWidth, int maxHeight) {
		Player player = parent.getPlayer();
		Level lvl = player.getLevel();
		for(int i = offX; i < offX + maxWidth; i++) {
			for(int j = offY; j < offY + maxHeight; j++) {
				Tile bg = lvl.getBg(i, j);
				if(bg != null) {
					bg.render(lvl, i, j, (i - offX) * Tile.TILESIZE, (j - offY) * Tile.TILESIZE);
				}
				Tile fg = lvl.getFg(i, j);
				if(fg != null && fg.id != -1) {
					fg.render(lvl, i, j, (i - offX) * Tile.TILESIZE, (j - offY) * Tile.TILESIZE);
				}
			}
		}
	}

}
