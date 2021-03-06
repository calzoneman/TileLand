package net.calzoneman.TileLand.screen;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

import net.calzoneman.TileLand.gfx.Renderer;
import net.calzoneman.TileLand.gui.GUIButton;
import net.calzoneman.TileLand.gui.MenuManager;
import net.calzoneman.TileLand.inventory.ItemStack;
import net.calzoneman.TileLand.inventory.PlayerInventory;
import net.calzoneman.TileLand.tile.Tile;

public class InventoryScreen extends GameScreen {
	static final int SLOT_PADDING = 6;
	private Color slotBgColor = new Color(0.2f, 0.2f, 0.2f, 0.5f);
	private Color barBgColor = new Color(0.4f, 0.4f, 0.4f, 0.5f);
	private ItemStack selected = null;
	private GUIButton saveButton;
	private GUIButton quitButton;
	
	//private boolean[] oldmouse;
	//private boolean[] mouse;
	//private boolean[] keys;
	
	public InventoryScreen() {
		this(10, 57, 10 * Tile.TILESIZE + 11 * SLOT_PADDING, 3 * Tile.TILESIZE + 4 * SLOT_PADDING);
	}

	public InventoryScreen(int x, int y, int width, int height) {
		super(x, y, width, height);
		//this.keys = new boolean[256]; // Keyboard.getKeyCount() seems to have issues...
		//this.mouse = new boolean[Mouse.getButtonCount()];
		//this.oldmouse = new boolean[Mouse.getButtonCount()];
		this.saveButton = new GUIButton(Display.getWidth()-150, Display.getHeight()-120, 100, "Save");
		this.quitButton = new GUIButton(Display.getWidth()-150, Display.getHeight()-80, 100, "Quit");
	}
	
	@Override
	public void handleInput() {
		PlayerInventory inv = parent.getPlayer().getPlayerInventory();
		boolean[] oldmouse = mouse.clone();
		while(Mouse.next()) {
			// Update mouse button state
			if(Mouse.getEventButton() != -1) {
				mouse[Mouse.getEventButton()] = Mouse.getEventButtonState();
			}
			
			// Check if the save button was clicked
			int mx = Mouse.getX();
			int my = Display.getHeight() - Mouse.getY();
			if(mx > saveButton.getX() && mx < saveButton.getX() + saveButton.getWidth()
					&& my > saveButton.getY() && my < saveButton.getY() + saveButton.getHeight()) {
				saveButton.hover();
				if(mouse[0] && !oldmouse[0])
					saveButton.focus();
				else if(oldmouse[0] && !mouse[0]) {
					parent.getPlayer().getLevel().save();
					saveButton.setText("Level Saved");
				}
			}
			else {
				if(saveButton.isFocused())
					saveButton.blur();
				if(saveButton.isHovered())
					saveButton.unhover();
			}
			
			// Check hover and click for Quit button
			if(mx > quitButton.getX() && mx < quitButton.getX() + quitButton.getWidth()
					&& my > quitButton.getY() && my < quitButton.getY() + quitButton.getHeight()) {
				quitButton.hover();
				if(mouse[0] && !oldmouse[0])
					quitButton.focus();
				else if(oldmouse[0] && !mouse[0])
					MenuManager.getMenuManager().goBack();
			}
			else {
				if(quitButton.isFocused())
					quitButton.blur();
				if(quitButton.isHovered())
					quitButton.unhover();
			}
			
			// Calculate position relative to the inventory
			int offX = this.x;
			int offY = this.y;
			mx -= offX;
			if(my < this.y)
				offY = inv.getQuickbar().getY();
			my -= offY;
			
			// Calculate slot position
			int i = (mx - SLOT_PADDING) / (SLOT_PADDING + Tile.TILESIZE);
			int j = (my - SLOT_PADDING) / (SLOT_PADDING + Tile.TILESIZE);
			if(offY == this.y)
				j += 1;
			
			if(mouse[0] && !oldmouse[0]) { // Left Click
				
				if(i >= 0 && j >= 0 && i < 10 && j < 4) {
					int slot = j * 10 + i;
					ItemStack it = inv.getItemStack(slot);
					if(it != null && selected != null && it.getItem().equals(selected.getItem()) && it.getData() == selected.getData()) {
						int count = it.getCount() + selected.getCount();
						if(count > ItemStack.MAX_STACK_SIZE) {
							it.setCount(ItemStack.MAX_STACK_SIZE);
							inv.setItemStack(slot, it);
							selected.setCount(count - ItemStack.MAX_STACK_SIZE);
						}
						else {
							it.setCount(count);
							selected = null;
							inv.setItemStack(slot, it);
						}
					}
					else {
						inv.setItemStack(slot, selected);
						selected = it;
					}
				}
			}
			else if(mouse[1] && !oldmouse[1]) { // Right click
				if(i >= 0 && j >= 0 && i < 10 && j < 4) {
					int slot = j * 10 + i;
					ItemStack it = inv.getItemStack(slot);
					if(it == null && selected != null) {
						it = new ItemStack(selected.getItem(), 1, selected.getData());
						inv.setItemStack(slot, it);
						selected.setCount(selected.getCount() - 1);
						if(selected.getCount() == 0)
							selected = null;
					}
					else if(it != null && selected == null) {
						selected = new ItemStack(it.getItem(), 1, it.getData());
						inv.removeOneItem(slot);
					}
					else if(it != null && selected != null && it.getItem().equals(selected.getItem()) && it.getData() == selected.getData()) {
						if(selected.getCount() + 1 <= ItemStack.MAX_STACK_SIZE) {
							selected.setCount(selected.getCount() + 1);
							inv.removeOneItem(slot);
						}
					}
				}
			}
		}
		
		while(Keyboard.next()) {
			if(Keyboard.getEventKey() != -1) {
				// Update the key state
				keys[Keyboard.getEventKey()] = Keyboard.getEventKeyState();
				if(keys[Keyboard.KEY_ESCAPE])
					active = false;
			}
		}
	}

	@Override
	public void render() {
		PlayerInventory inv = parent.getPlayer().getPlayerInventory();
		
		// Draw the background
		Renderer.renderFilledRect(this.x, this.y, this.width, this.height, barBgColor);
		for(int j = 0; j < 3; j++) {
			for(int i = 0; i < 10; i++) {
				int x = this.x + SLOT_PADDING*(i+1) + i*Tile.TILESIZE;
				int y = this.y + SLOT_PADDING*(j+1) + (j)*Tile.TILESIZE;
				// Draw the slot background
				Renderer.renderFilledRect(x, y, Tile.TILESIZE, Tile.TILESIZE, slotBgColor);
				// Draw the contents (where applicable)
				if(inv.getItemStack((j+1) * 10 + i) != null)
					inv.getItemStack((j+1) * 10 + i).render(x, y);
			}
		}
		
		// Calculate position relative to the inventory
		int offX = this.x;
		int offY = this.y;
		int mx = Mouse.getX();
		mx -= offX;
		int my = (Display.getHeight() - Mouse.getY());
		if(my < this.y)
			offY = inv.getQuickbar().getY();
		my -= offY;
		
		// Calculate slot position
		int i = (mx - SLOT_PADDING) / (SLOT_PADDING + Tile.TILESIZE);
		int j = (my - SLOT_PADDING) / (SLOT_PADDING + Tile.TILESIZE);
		// Don't draw if out of bounds
		if(i >= 0 && j >= 0 && i < 10 && j < 4) {
			// Calculate rounded mouse position
			mx = (i+1) * SLOT_PADDING + i*Tile.TILESIZE;
			my = (j+1) * SLOT_PADDING + j*Tile.TILESIZE;
			Renderer.renderRect(mx + offX, my + offY, Tile.TILESIZE, Tile.TILESIZE, Color.blue);
		}
		if(selected != null)
			selected.render(Mouse.getX(), Display.getHeight() - Mouse.getY());
		
		saveButton.render();
		quitButton.render();
	}

}
