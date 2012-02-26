package net.calzoneman.TileLand.event;

import net.calzoneman.TileLand.Game;
import net.calzoneman.TileLand.level.Location;

public abstract class EventHandler {
	
	public void onPlayerMove(Game game, Location oldPosition, int facing) { }
	
	public void onPlayerPlaceTile(Game game, Location pos, short id) { }
	
	public void onPlayerDeleteTile(Game game, Location pos, short id) { }
	
	public void onPlayerChat(Game game, String message) { }
	
	/** Not implemented yet */
	public void onPlayerInventoryChange(Game game) { }
}
