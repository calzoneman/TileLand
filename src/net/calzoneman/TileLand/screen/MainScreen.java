package net.calzoneman.TileLand.screen;

import net.calzoneman.TileLand.Game;
import net.calzoneman.TileLand.action.ActionResult;
import net.calzoneman.TileLand.gfx.Renderer;
import net.calzoneman.TileLand.inventory.Item;
import net.calzoneman.TileLand.inventory.ItemStack;
import net.calzoneman.TileLand.inventory.PlayerInventory;
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
				useItem(ply, Mouse.getX(), Mouse.getY());
				lastMoveTime = System.currentTimeMillis();
			}
		}
	}
	
	private void useItem(Player ply, int mouseX, int mouseY) {
		Location position = ply.getPosition();
		Level level = ply.getLevel();
		PlayerInventory inventory = ply.getPlayerInventory();
		if(!mouse[0] && !mouse[1])
			return;
		// Mouse offset in the level
		Location offset = new Location(
				position.x - Display.getWidth() / Level.TILESIZE / 2,
				position.y - Display.getHeight() / Level.TILESIZE / 2);
		// tx and ty are the coordinates for the tile under the mouse cursor
		int tx = mouseX / Level.TILESIZE + offset.x;
		int ty = (Display.getHeight() - mouseY) / Level.TILESIZE + offset.y;
		if(!(tx < 0 || tx >= level.getWidth() || ty < 0 || ty >= level.getHeight())) {
			Item held = null;
			if (inventory.getQuickbar().getSelectedItemStack() != null)
				held = inventory.getQuickbar().getSelectedItemStack().getItem();
			// You can't place null!  (You can break with it though)
			if(held == null && mouse[0])
				return;
			// Why would you even do this [placing a tile on top of you]
			if(held instanceof Tile && ((Tile) held).isSolid() && tx == position.x && ty == position.y)
				return;
			// Limit time spacing
			if(System.nanoTime() < lastClickTime + CLICK_DELAY)
				return;
			ActionResult ar = null;
			if(mouse[0])
				ar = held.leftClick(ply, tx, ty);
			else if (mouse[1] && held == null)
				ar = defaultRightClick(ply, tx, ty);
			else if (mouse[1])
				ar = held.rightClick(ply, tx, ty);
			if(ar != null && ar.getResultCode() != ActionResult.FAILURE)
				lastClickTime = System.nanoTime();
		}
	}
	
	private ActionResult defaultRightClick(Player ply, int x, int y) {
		Level lvl = ply.getLevel();
		int fg = lvl.getFgId(x, y);
		int fgdata = lvl.getFgData(x, y);
		if(fg == TileId.AIR) {
			int bg = lvl.getBgId(x, y);
			int bgdata = lvl.getBgData(x, y);
			if(!TileTypes.playerBreakable(bg))
				return new ActionResult(ActionResult.FAILURE, null);
			else if(lvl.setTile(x, y, TileTypes.getDefaultBg())) {
				ItemStack it = new ItemStack(TileTypes.getTile(bg), 1);
				if(TileTypes.getTile(bg).hasData() && !TileTypes.getTile(bg).isMultidirectional())
					it.setData(bgdata);
				ply.getInventory().addItemStack(it);
				return new ActionResult(ActionResult.TILE_BREAK, TileTypes.getTile(bg));
			}
			else
				return new ActionResult(ActionResult.FAILURE, null);
		}
		else if(!TileTypes.playerBreakable(fg)) {
			return new ActionResult(ActionResult.FAILURE, null);
		}
		else if (lvl.setTile(x, y, TileTypes.getDefaultFg())) {
			ItemStack it = new ItemStack(TileTypes.getTile(fg), 1);
			if(TileTypes.getTile(fg).hasData() && !TileTypes.getTile(fg).isMultidirectional())
				it.setData(fgdata);
			ply.getInventory().addItemStack(it);
			return new ActionResult(ActionResult.TILE_BREAK, TileTypes.getTile(fg));
		}
		else
			return new ActionResult(ActionResult.FAILURE, null);
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
