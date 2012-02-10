package net.calzoneman.TileLand.input;

import net.calzoneman.TileLand.action.ActionResult;
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

public class PlayerInputController extends InputController {
	
	private boolean[] mouse;
	private boolean[] keys;
	private int currentMoveKey;
	private long lastMoveTime;
	
	public PlayerInputController() {
		this.keys = new boolean[256]; // Keyboard.getKeyCount() seems to have issues...
		this.mouse = new boolean[Mouse.getButtonCount()];
		this.currentMoveKey = -1;
		this.lastMoveTime = 0;
	}

	@Override
	public void handleMouse(Player ply) {
		while(Mouse.next()) {
			// Update mouse button state
			if(Mouse.getEventButton() != -1)
				mouse[Mouse.getEventButton()] = Mouse.getEventButtonState();
			useItem(ply, Mouse.getEventX(), Mouse.getEventY());
		}
	}

	@Override
	public void handleKeyboard(Player ply) {
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
			ActionResult ar = null;
			// You can't place null!  (You can break with it though)
			if(held == null && !mouse[1])
				return;
			// Why would you even do this [placing a tile on top of you]
			if(held instanceof Tile && ((Tile) held).isSolid() && tx == position.x && ty == position.y)
				return;
			if(mouse[0])
				ar = held.leftClick(ply, tx, ty);
			else if (mouse[1] && held == null)
				ar = defaultRightClick(ply, tx, ty);
			else if (mouse[1])
				ar = held.rightClick(ply, tx, ty);
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

}
