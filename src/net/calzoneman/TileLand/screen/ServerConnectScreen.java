package net.calzoneman.TileLand.screen;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

import net.calzoneman.TileLand.Game;
import net.calzoneman.TileLand.TileLand;
import net.calzoneman.TileLand.gfx.Renderer;
import net.calzoneman.TileLand.gfx.TilelandFont;

public class ServerConnectScreen extends GameScreen {
	
	public static final int STATE_CONNECTING = 1;
	public static final int STATE_MAPRECV = 2;
	public static final int STATE_CONNECTED = 3;
	public static final int STATE_ERROR = -1;
	
	public static final String MSG_CONNECTING = "Connecting...";
	public static final String MSG_CONNECTED = "Connected.";
	public static final String MSG_MAPRECV = "Receiving Map...";
	
	public static final Color BG_NORMAL = new Color(64, 64, 64);
	public static final Color BG_ERROR = new Color(87, 0, 0);
	public static final Color BG_TRANSPARENT = new Color(0, 0, 0, 0);
	
	private int state = STATE_CONNECTING;
	private String message = MSG_CONNECTING;
	private String messageDetail = "";
	
	public ServerConnectScreen(Game parent) {
		super(0, 0, Display.getWidth(), Display.getHeight(), parent);
	}
	
	public int getState() {
		return state;
	}
	
	public void setState(int state) {
		this.state = state;
		if(state == STATE_CONNECTING)
			message = MSG_CONNECTING;
		else if(state == STATE_CONNECTED)
			message = MSG_CONNECTED;
		else if(state == STATE_MAPRECV)
			message = MSG_MAPRECV;
		else
			message = "ERROR";
	}
	
	public String getMessage() {
		return message;
	}
	
	public String getMessageDetail() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public void setMessageDetail(String msg) {
		this.messageDetail = msg;
	}

	@Override
	public void render() {
		if(state == STATE_ERROR)
			Renderer.renderFilledRect(0, 0, this.width, this.height, BG_ERROR);
		else
			Renderer.renderFilledRect(0, 0, this.width, this.height, BG_NORMAL);
		
		TilelandFont font = TileLand.getResourceManager().getFont("res/font/default.ttf", 24);
		int sy = this.height / 2 - (font.getHeight(message) + font.getHeight(messageDetail)) / 2;
		int sx = this.width / 2 - font.getWidth(message) / 2;
		
		font.drawString(sx, sy, message, BG_TRANSPARENT);
		sy += font.getHeight(message);
		font.drawString(sx, sy, messageDetail, BG_TRANSPARENT);
	}

	@Override
	public void handleInput() {
		
	}
}
