package net.calzoneman.TileLand.screen;

import net.calzoneman.TileLand.Game;
import net.calzoneman.TileLand.action.ActionResult;
import net.calzoneman.TileLand.action.TileEditResult;
import net.calzoneman.TileLand.event.EventManager;
import net.calzoneman.TileLand.gfx.Renderer;
import net.calzoneman.TileLand.inventory.ItemStack;
import net.calzoneman.TileLand.level.Level;
import net.calzoneman.TileLand.level.Location;
import net.calzoneman.TileLand.player.Player;
import net.calzoneman.TileLand.tile.Tile;
import net.calzoneman.TileLand.tile.TileId;
import net.calzoneman.TileLand.tile.TileTypes;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

public class MainScreen extends GameScreen {
	
	private int currentMoveKey;
	private long lastMoveTime;
	private long lastClickTime;
	
	static final long CLICK_DELAY = 50000000L;
	
	/** Colors for Mouse overlay */
	private static Color transparentRed = new Color(255, 0, 0, 130);
	private static Color transparentGreen = new Color(0, 255, 0, 130);
	
	public MainScreen(Game parent) {
		super(0, 0, Display.getWidth(), Display.getHeight(), parent);
		this.currentMoveKey = -1;
		this.lastMoveTime = 0;
		this.lastClickTime = 0;
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
			useItem(ply, Mouse.getEventX(), Mouse.getEventY());
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
				useItem(ply, Mouse.getX(), Mouse.getY());
				lastMoveTime = System.currentTimeMillis();
			}
		}
	}
	
	private void useItem(Player ply, int mouseX, int mouseY) {
		Location position = ply.getPosition();
		Level level = ply.getLevel();
		if(!mouse[0] && !mouse[1])
			return;
		// Mouse offset in the level
		Location offset = new Location(
				position.x - Display.getWidth() / Level.TILESIZE / 2,
				position.y - Display.getHeight() / Level.TILESIZE / 2);
		// tx and ty are the coordinates for the tile under the mouse cursor
		int tx = mouseX / Level.TILESIZE + offset.x;
		int ty = (Display.getHeight() - mouseY) / Level.TILESIZE + offset.y;
		if(ply.getHeldItem() instanceof Tile && !(tx < 0 || tx >= level.getWidth() || ty < 0 || ty >= level.getHeight())) {
			Tile held = (Tile) ply.getHeldItem();
			// You can't place null!  (You can break with it though)
			if(held == null && mouse[0])
				return;
			// Why would you even do this [placing a tile on top of you]
			if(held.isSolid() && tx == position.x && ty == position.y)
				return;
			// Limit time spacing
			if(System.nanoTime() < lastClickTime + CLICK_DELAY)
				return;
			TileEditResult result = null;
			if(mouse[0])
				result = placeTile(ply, tx, ty);
			else if (mouse[1])
				result = deleteTile(ply, tx, ty);
			if(result != null && result.getResultCode() != ActionResult.FAILURE) {
				lastClickTime = System.nanoTime();
				if(result.getResultCode() == ActionResult.TILE_BREAK) {
					EventManager.manager.onPlayerDeleteTile(parent, new Location(tx, ty), result);
				}
				else if(result.getResultCode() == ActionResult.TILE_PLACE) {
					EventManager.manager.onPlayerPlaceTile(parent, new Location(tx, ty), result);
				}
			}
		}
	}
	
	public TileEditResult placeTile(Player ply, int x, int y) {
		Level lvl = ply.getLevel();
		Tile held = (Tile) ply.getHeldItem();
		Tile before;
		if(held.isForeground())
			before = lvl.getFg(x, y);
		else
			before = lvl.getBg(x, y);
		boolean worked = true;
		if(held.isForeground())
			worked = lvl.getFgId(x, y) == TileId.AIR;
		if(worked)
			worked = lvl.setTile(x, y, held);
		Tile after;
		if(held.isForeground())
			after = lvl.getFg(x, y);
		else
			after = lvl.getBg(x, y);
		if(before != after && worked && !parent.isMultiplayer()) {
			ply.getInventory().removeOneItem(ply.getPlayerInventory().getQuickbar().getSelectedSlot());
		}
		return new TileEditResult(ActionResult.TILE_PLACE, held.getId(), 0, held.isForeground() ? 1 : 0);
	}
	
	private TileEditResult deleteTile(Player ply, int x, int y) {
		Level lvl = ply.getLevel();
		int fg = lvl.getFgId(x, y);
		int fgdata = lvl.getFgData(x, y);
		if(fg == TileId.AIR) {
			int bg = lvl.getBgId(x, y);
			int bgdata = lvl.getBgData(x, y);
			if(!TileTypes.playerBreakable(bg))
				return new TileEditResult(ActionResult.FAILURE);
			else if(lvl.setTile(x, y, TileTypes.getDefaultBg())) {
				ItemStack it = new ItemStack(TileTypes.getTile(bg), 1);
				if(TileTypes.getTile(bg).hasData() && !TileTypes.getTile(bg).isMultidirectional())
					it.setData(bgdata);
				if(!parent.isMultiplayer())
					ply.getInventory().addItemStack(it);
				return new TileEditResult(ActionResult.TILE_BREAK, bg, bgdata, 0);
			}
			else
				return new TileEditResult(ActionResult.FAILURE);
		}
		else if(!TileTypes.playerBreakable(fg)) {
			return new TileEditResult(ActionResult.FAILURE);
		}
		else if (lvl.setTile(x, y, TileTypes.getDefaultFg())) {
			ItemStack it = new ItemStack(TileTypes.getTile(fg), 1);
			if(TileTypes.getTile(fg).hasData() && !TileTypes.getTile(fg).isMultidirectional())
				it.setData(fgdata);
			if(!parent.isMultiplayer())
				ply.getInventory().addItemStack(it);
			return new TileEditResult(ActionResult.TILE_BREAK, fg, fgdata, 1);
		}
		else
			return new TileEditResult(ActionResult.FAILURE);
	}

	@Override
	public void render() {
		Player player = parent.getPlayer();
		Renderer.renderFilledRect(0, 0, Display.getWidth(), Display.getHeight(), Color.red);
		Level level = player.getLevel();
		// Calculate at what offset to begin rendering the level
		Location renderStart = new Location(
				player.getPosition().x - Display.getWidth() / Level.TILESIZE / 2,
				player.getPosition().y - Display.getHeight() / Level.TILESIZE / 2);
		// Render the level
		if(level != null) {
			// Background
			renderLevel(renderStart.x, renderStart.y, Display.getWidth() / Level.TILESIZE, Display.getHeight() / Level.TILESIZE, false);
			// Foreground
			renderLevel(renderStart.x, renderStart.y, Display.getWidth() / Level.TILESIZE, Display.getHeight() / Level.TILESIZE, true);
		}
		// Render the player sprite
		player.render((player.getPosition().x - renderStart.x) * Level.TILESIZE, (player.getPosition().y - renderStart.y) * Level.TILESIZE);
		// Render the mouse
		if(active)
			renderMouse(renderStart);
		// Render the player's nametag
		player.renderNameCentered();
		
		// Render the HUD
		player.getPlayerInventory().getQuickbar().render();
	}
	
	private void renderMouse(Location renderStart) {
		Player player = parent.getPlayer();
		Level level = player.getLevel();
		ItemStack current = null;
		Color col = transparentGreen;
		int tx = Mouse.getX() / Level.TILESIZE + renderStart.x;
		int ty = (Display.getHeight() - Mouse.getY()) / Level.TILESIZE + renderStart.y;
		if(player.getPlayerInventory().getQuickbar().getSelectedItemStack() == null // Selected item is null
				|| (tx == player.getPosition().x && ty == player.getPosition().y) // Cursor is over the player
				|| tx < 0 || tx >= level.getWidth() || ty < 0 || ty >= level.getHeight()) // Mouse it outside the bounds of the Level
			col = transparentRed;
		current = player.getPlayerInventory().getQuickbar().getSelectedItemStack();
		
		int mx = Mouse.getX();
		int my = Mouse.getY();
		tx = mx / Level.TILESIZE;
		ty = (Display.getHeight() - my) / Level.TILESIZE;
		if(current != null) {
			col.bind();
			current.getItem().render(tx * Level.TILESIZE, ty * Level.TILESIZE);
		}
		else {
			Renderer.renderFilledRect(tx * Level.TILESIZE, ty * Level.TILESIZE, Level.TILESIZE, Level.TILESIZE, col);
		}
		// Draw border
		Renderer.renderRect(tx * Level.TILESIZE, ty * Level.TILESIZE, Level.TILESIZE, Level.TILESIZE, Color.black);
	}
	
	private void renderLevel(int offX, int offY, int maxWidth, int maxHeight, boolean foreground) {
		Player player = parent.getPlayer();
		Level lvl = player.getLevel();
		for(int i = offX; i < offX + maxWidth; i++) {
			for(int j = offY; j < offY + maxHeight; j++) {
				if(foreground) {
					Tile fg = lvl.getFg(i, j);
					if(fg != null && fg.getId() != -1) {
						if(fg.hasData())
							fg.render((i - offX) * Level.TILESIZE, (j - offY) * Level.TILESIZE, lvl.getFgData(i,  j));
						else
							fg.render((i - offX) * Level.TILESIZE, (j - offY) * Level.TILESIZE);
					}
				}
				else {
					Tile bg = lvl.getBg(i, j);
					if(bg != null) {
						if(bg.hasData()) {
							bg.render((i - offX) * Level.TILESIZE, (j - offY) * Level.TILESIZE, lvl.getBgData(i,  j));
						}
						else
							bg.render((i - offX) * Level.TILESIZE, (j - offY) * Level.TILESIZE);
					}
				}
				
			}
		}
	}

}
