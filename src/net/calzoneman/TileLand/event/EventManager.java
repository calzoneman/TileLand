package net.calzoneman.TileLand.event;

import java.util.ArrayList;
import java.util.List;

import net.calzoneman.TileLand.Game;
import net.calzoneman.TileLand.level.Location;

public class EventManager {
	
	public static EventManager manager = new EventManager();
	
	protected List<EventHandler> handlers;
	
	public EventManager() {
		handlers = new ArrayList<EventHandler>();
	}
	
	public void onPlayerMove(Game game, Location oldPosition, int facing) {
		for(EventHandler handler : handlers) {
			handler.onPlayerMove(game, oldPosition, facing);
		}
	}
	
	public void onPlayerPlaceTile(Game game, Location pos, short id) {
		for(EventHandler handler : handlers) {
			handler.onPlayerPlaceTile(game, pos, id);
		}
	}
	
	public void onPlayerDeleteTile(Game game, Location pos, short id) {
		for(EventHandler handler : handlers) {
			handler.onPlayerDeleteTile(game, pos, id);
		}
	}
	
	public void onPlayerChat(Game game, String message) {
		for(EventHandler handler : handlers) {
			handler.onPlayerChat(game, message);
		}
	}
	
	public void onPlayerInventoryChange(Game game) { }
	/*
	public void onPlayerInventoryChange(Game game, int slot, ItemStack oldIt, ItemStack newIt) {
		for(EventHandler handler : handlers) {
			handler.onPlayerInventoryChange(game, slot, oldIt, newIt);
		}
	}*/
}
