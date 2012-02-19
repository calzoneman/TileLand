package net.calzoneman.TileLand.screen;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import net.calzoneman.TileLand.TileLand;
import net.calzoneman.TileLand.event.EventManager;
import net.calzoneman.TileLand.gfx.Renderer;
import net.calzoneman.TileLand.gfx.TilelandFont;
import net.calzoneman.TileLand.gui.GUITextbox;
import net.calzoneman.TileLand.util.KeyValuePair;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;

public class ChatScreen extends GameScreen {
	static final int CHAT_HISTORY_SIZE = 15;
	static final long CHAT_VISIBILITY_DELAY = 10000000000L;
	static final long KEY_WAIT = 500000000;
	static final int PADDING_X = 3;
	static final Color background = new Color(100, 100, 100, 100);
	static final Color transparent = new Color(0, 0, 0, 0);
	
	protected List<KeyValuePair<Long, String>> chatHistory;
	protected String currentMessage;
	
	protected GUITextbox inputBox;
	
	protected int lastKey;
	protected char lastKeyChar;
	protected long lastKeyTime;

	public ChatScreen(int x, int y, int width, int height) {
		super(x, y, width, height);
		chatHistory = new ArrayList<KeyValuePair<Long, String>>();
		inputBox = new GUITextbox(x, y + height - GUITextbox.TEXTBOX_HEIGHT, width);
		inputBox.focus();
		lastKey = -1;
		lastKeyTime = System.nanoTime();
		currentMessage = "";
		active = false;
	}
	
	private void addMessage(String msg) {
		if(msg.isEmpty())
			return;
		TilelandFont fnt = TileLand.getResourceManager().getPreferredFont();
		ArrayList<String> result = new ArrayList<String>();
		String temp = "";
		while(fnt.getWidth(msg) > width - 10) {
			temp = msg.substring(msg.length() - 1) + temp;
			msg = msg.substring(0, msg.length() - 1);
			if(fnt.getWidth(temp) > width - 10) {
				result.add("> " + temp);
				temp = "";
			}
		}
		if(!temp.isEmpty())
			result.add("> " + temp);
		result.add(0, msg);
		for(String str : result) {
			if(chatHistory.size() >= CHAT_HISTORY_SIZE) {
				chatHistory.remove(0);
			}
			chatHistory.add(new KeyValuePair<Long, String>(System.nanoTime(), str));
		}
	}
	
	@Override
	public void setActive(boolean active) {
		super.setActive(active);
		if(active)
			currentMessage = "";
	}

	@Override
	public void render() {
		if(active)
			Renderer.renderFilledRect(x, y, width, height, background);
		TilelandFont fnt = TileLand.getResourceManager().getPreferredFont();
		ListIterator<KeyValuePair<Long, String>> itr = chatHistory.listIterator(chatHistory.size());
		int curY = y + height - GUITextbox.TEXTBOX_HEIGHT - 2;
		while(itr.hasPrevious()) {
			KeyValuePair<Long, String> msg = itr.previous();
			if(!active && System.nanoTime() > msg.key() + CHAT_VISIBILITY_DELAY)
				continue;
			curY -= fnt.getHeight(msg.value());
			fnt.drawString(x + PADDING_X - 1, curY + 1, TilelandFont.TEXT_BLACK + TilelandFont.stripColor(msg.value()), transparent);
			fnt.drawString(x + PADDING_X, curY, msg.value(), transparent);
		}
		if(active)
			inputBox.render();
	}
	

	@Override
	public void handleInput() {
		boolean[] oldkeys = keys.clone();
		while(Keyboard.next()) {
			int key = Keyboard.getEventKey();
			if(key != -1) {
				keys[Keyboard.getEventKey()] = Keyboard.getEventKeyState();
			}
			if(keys[Keyboard.KEY_RETURN]) {
				if(!parent.isMultiplayer())
					addMessage(parent.getPlayer().getName() + TilelandFont.TEXT_WHITE + ": " + inputBox.getText());
				EventManager.manager.onPlayerChat(parent, inputBox.getText());
				inputBox.setText("");
				active = false;
				return;
			}
			if(key != -1 && keys[key] && !oldkeys[key]) {
				inputBox.onKey(key, Keyboard.getEventCharacter());
			}
			if(key != lastKey) {
				lastKeyTime = System.nanoTime();
				lastKey = key;
				lastKeyChar = Keyboard.getEventCharacter();
			}
		}
		if(lastKey != -1 && keys[lastKey] && System.nanoTime() > lastKeyTime + KEY_WAIT) {
			inputBox.onKey(lastKey, lastKeyChar);
		}
		else if(lastKey != -1 && !keys[lastKey]) {
			lastKey = -1;
			lastKeyTime = System.nanoTime();
			lastKeyChar = Keyboard.CHAR_NONE;
		}
	}

}
