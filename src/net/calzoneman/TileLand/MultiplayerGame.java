package net.calzoneman.TileLand;

import java.net.Socket;
import java.net.SocketAddress;

import net.calzoneman.TileLand.event.EventHandler;
import net.calzoneman.TileLand.level.Location;
import net.calzoneman.TileLand.player.Player;

public class MultiplayerGame extends Game {
	
	protected Socket sock;

	public MultiplayerGame(Player player, String ip, int port) {
		super(player);
		System.out.println("Attempting to connect to " + ip);
		//sock = new Socket(ip, port);
	}
	
	@Override
	public boolean isMultiplayer() {
		return true;
	}
	
	protected class MultiplayerEventHandler extends EventHandler {

	}

}
