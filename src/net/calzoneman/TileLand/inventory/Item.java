package net.calzoneman.TileLand.inventory;

import net.calzoneman.TileLand.action.ActionResult;
import net.calzoneman.TileLand.gfx.Renderable;
import net.calzoneman.TileLand.player.Player;

public abstract class Item implements Renderable {
	
	private short id;
	
	public Item(short id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public abstract ActionResult leftClick(Player ply, int x, int y);
	
	public abstract ActionResult rightClick(Player ply, int x, int y);
}
