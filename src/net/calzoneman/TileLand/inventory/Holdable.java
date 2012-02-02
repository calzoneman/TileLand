package net.calzoneman.TileLand.inventory;

import net.calzoneman.TileLand.level.Level;

public interface Holdable {
	public abstract void leftClick(Level lvl, int x, int y);
	
	public abstract void rightClick(Level lvl, int x, int y);
}
