package net.calzoneman.TileLand.event;

import net.calzoneman.TileLand.Game;
import net.calzoneman.TileLand.action.TileEditResult;
import net.calzoneman.TileLand.level.Location;

public abstract class EventHandler {
	
	public void onPlayerMove(Game game, Location oldPosition, int facing) { }
	
	public void onPlayerPlaceTile(Game game, Location pos, TileEditResult result) { }
	
	public void onPlayerDeleteTile(Game game, Location pos, TileEditResult result) { }
	
	public void onPlayerChat(Game game, String message) { }
	
	/** Not implemented yet */
	public void onPlayerInventoryChange(Game game) { }
}
