package net.calzoneman.TileLand;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

import net.calzoneman.TileLand.gfx.Renderer;
import net.calzoneman.TileLand.input.InputController;
import net.calzoneman.TileLand.inventory.ItemStack;
import net.calzoneman.TileLand.level.Level;
import net.calzoneman.TileLand.level.Location;
import net.calzoneman.TileLand.player.Player;
import net.calzoneman.TileLand.tile.Tile;
import net.calzoneman.TileLand.util.State;

public class Game implements State {
	private Player player;
	private InputController input;
	private boolean active = false;
	
	/** Colors for Mouse overlay */
	private static Color transparentRed = new Color(255, 0, 0, 130);
	private static Color transparentGreen = new Color(0, 255, 0, 130);
	
	public Game(Player player, InputController input) {
		this.player = player;
		this.input = input;
		this.active = true;
	}

	@Override
	public void handleInput() {
		input.handleMouse(player);
		input.handleKeyboard(player);
	}

	@Override
	public void render() {
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
		if(isActive())
			renderMouse(renderStart);
		// Render the player's nametag
		player.renderNameCentered();
		
		// Render the HUD
		player.getPlayerInventory().getQuickbar().render();
	}
	
	private void renderMouse(Location renderStart) {
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

	@Override
	public boolean isActive() {
		return active;
	}

	@Override
	public void setActive(boolean active) {
		this.active = active;		
	}
	
	
}
