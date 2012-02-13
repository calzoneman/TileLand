package net.calzoneman.TileLand.gui;

import java.util.HashMap;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class GUIMenu {
	
	static final long KEY_WAIT = 500000000;
	
	protected HashMap<String, GUIComponent> children;
	protected String[] fieldOrder;
	protected GUIComponent focused;
	protected boolean[] mouse;
	protected boolean[] keys;
	protected int lastKey;
	protected char lastKeyChar;
	protected long lastKeyTime;
	
	public GUIMenu() {
		this.mouse = new boolean[Mouse.getButtonCount()];
		this.keys = new boolean[256];
		this.children = new HashMap<String, GUIComponent>();
		this.focused = null;
		this.fieldOrder = new String[] { };
		this.lastKey = -1;
		this.lastKeyChar = Keyboard.CHAR_NONE;
		this.lastKeyTime = Long.MAX_VALUE - KEY_WAIT;
	}
	
	/**
	 * Adds a child component to this Menu
	 * @param name The reference name of the child (e.g. "cancelbutton")
	 * @param child The child component to add
	 */
	public void addChild(String name, GUIComponent child) {
		children.put(name, child);
	}
	
	/**
	 * Retrieve a child component by reference name
	 * @param name The name of the child to retrieve
	 * @return The GUIComponent associated with name if it exists, null otherwise
	 */
	public GUIComponent getChild(String name) {
		return children.get(name);
	}
	
	public void render() {
		for(GUIComponent comp : children.values()) {
			comp.render();
		}
	}
	
	public void handleInput() {
		boolean[] oldmouse = mouse.clone();
		while(Mouse.next()) {
			int button = Mouse.getEventButton();
			if(button != -1)
				mouse[button] = Mouse.getEventButtonState();
			if(oldmouse[0] && !mouse[0])
				clickUp(Mouse.getX(), Display.getHeight() - Mouse.getY());
			else if(!mouse[0])
				hover(Mouse.getX(), Display.getHeight() - Mouse.getY());
			else if(mouse[0] && !oldmouse[0])
				clickDown(Mouse.getX(), Display.getHeight() - Mouse.getY());
			
		}
		
		boolean[] oldkeys = keys.clone();
		while(Keyboard.next()) {
			int key = Keyboard.getEventKey();
			if(key != -1) 
				keys[key] = Keyboard.getEventKeyState();
			if(key != -1 && keys[key] && !oldkeys[key])
				keypress(key, Keyboard.getEventCharacter());
			if(key != lastKey) {
				lastKeyTime = System.nanoTime();
				lastKey = key;
				lastKeyChar = Keyboard.getEventCharacter();
			}
		}
		
		// Check for held down keys
		for(int k = 0; k < keys.length; k++) {
			if(keys[k] && k == lastKey && System.nanoTime() > lastKeyTime + KEY_WAIT)
				keypress(k, lastKeyChar);
		}
	}
	
	private void keypress(int keycode, char keychar) {
		if(keycode == Keyboard.KEY_TAB)
			onTab();
		else if(keycode == Keyboard.KEY_RETURN)
			onEnter();
		else if(focused != null) {
			focused.onKey(keycode, keychar);
		}
	}
	
	protected void onTab() {
		if(focused != null) {
			String focusedKey = "";
			for(String key : children.keySet()) {
				if(children.get(key).equals(focused))
					focusedKey = key;
			}
			int i = 0;
			while(i < fieldOrder.length && !fieldOrder[i].equals(focusedKey))
				i++;
			if(keys[Keyboard.KEY_LSHIFT] && i < fieldOrder.length && i > 0) {
				focused.blur();
				focused = children.get(fieldOrder[--i]);
				focused.focus();
			}
			else if(!keys[Keyboard.KEY_LSHIFT] && i < fieldOrder.length-1) {
				focused.blur();
				focused = children.get(fieldOrder[++i]);
				focused.focus();
			}
		}
	}
	
	protected void onEnter() { }

	private void hover(int mx, int my) {
		for(GUIComponent comp : children.values()) {
			if(mx > comp.getX() && mx < (comp.getX() + comp.getWidth())
					&& my > comp.getY() && my < (comp.getY() + comp.getHeight()))
				comp.hover();
			else if(comp.isHovered())
				comp.unhover();
		}
	}
	
	private void clickDown(int mx, int my) {
		for(GUIComponent comp : children.values()) {
			if(mx > comp.getX() && mx < (comp.getX() + comp.getWidth())
					&& my > comp.getY() && my < (comp.getY() + comp.getHeight())) {
				if(!comp.isFocused()) {
					comp.focus();
					if(focused != null && !focused.equals(comp))
						focused.blur();
					focused = comp;
				}
			}
			else if(comp.isFocused()) {
				comp.blur();
			}
		}
	}
	
	private void clickUp(int mx, int my) {
		for(GUIComponent comp : children.values()) {
			if(mx > comp.getX() && mx < (comp.getX() + comp.getWidth())
					&& my > comp.getY() && my < (comp.getY() + comp.getHeight())) {
				if(comp.isFocused()) {
					comp.onClick();
				}
			}
			else if(comp.isFocused()) {
				comp.blur();
			}
		}
	}
	
	public void resetAll() {
		for(GUIComponent comp : children.values()) {
			comp.blur();
			comp.unhover();
		}
	}
}
