package net.calzoneman.TileLand.inventory;

import net.calzoneman.TileLand.action.ActionResult;
import net.calzoneman.TileLand.gfx.Renderable;
import net.calzoneman.TileLand.level.Level;

public abstract class Item implements Renderable {
	public abstract ActionResult leftClick(Level lvl, int x, int y);
	
	public abstract ActionResult rightClick(Level lvl, int x, int y);
}
