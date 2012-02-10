package net.calzoneman.TileLand.input;

import net.calzoneman.TileLand.player.Player;

public abstract class InputController {
	
	public abstract void handleMouse(Player ply);
	
	public abstract void handleKeyboard(Player ply);
}
