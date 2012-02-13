package net.calzoneman.TileLand.gui;

import java.util.HashMap;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class GUIMenu {
	protected HashMap<String, GUIComponent> children;
	protected String[] fieldOrder;
	protected GUIComponent focused;
	protected boolean[] mouse;
	protected boolean[] keys;
	
	public GUIMenu() {
		this.mouse = new boolean[Mouse.getButtonCount()];
		this.keys = new boolean[256];
		this.children = new HashMap<String, GUIComponent>();
		this.focused = null;
		this.fieldOrder = new String[] { };
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
			if(!mouse[0])
				checkHover(Mouse.getX(), Display.getHeight() - Mouse.getY());
			else if(mouse[0] && !oldmouse[0])
				click(Mouse.getX(), Display.getHeight() - Mouse.getY());
		}
		
		boolean[] oldkeys = keys.clone();
		while(Keyboard.next()) {
			int key = Keyboard.getEventKey();
			if(key != -1) 
				keys[key] = Keyboard.getEventKeyState();
			if(key != -1 && keys[key] && !oldkeys[key])
				keypress(key, Keyboard.getEventCharacter());
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
				focused.onClickOut();
				focused = children.get(fieldOrder[--i]);
				focused.onClick();
			}
			else if(i < fieldOrder.length-1) {
				focused.onClickOut();
				focused = children.get(fieldOrder[++i]);
				focused.onClick();
			}
		}
	}
	
	protected void onEnter() { }

	private void checkHover(int mx, int my) {
		for(GUIComponent comp : children.values()) {
			if(mx > comp.getX() && mx < (comp.getX() + comp.getWidth())
					&& my > comp.getY() && my < (comp.getY() + comp.getHeight()))
				comp.onHover();
			else
				comp.onUnHover();
		}
	}
	
	private void click(int mx, int my) {
		for(GUIComponent comp : children.values()) {
			if(mx > comp.getX() && mx < (comp.getX() + comp.getWidth())
					&& my > comp.getY() && my < (comp.getY() + comp.getHeight())) {
				comp.onClick();
				if(comp.isFocusable())
					focused = comp;
				else if(focused == comp) 
					focused = null;
			}
			else
				comp.onClickOut();
		}
	}
}