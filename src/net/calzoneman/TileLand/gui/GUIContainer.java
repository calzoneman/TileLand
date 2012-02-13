package net.calzoneman.TileLand.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

/**
 * GUIContainer represents a bounded rectangle in which various components can be
 * placed (including other GUIContainers).
 * @author Calvin
 *
 */
public class GUIContainer extends GUIComponent {
	/** How long a key has to be held down before key-repeat works */
	static final long KEY_WAIT = 500000000;
	
	/** The parent GUIContainer for this container (null if it is the root container) */
	protected GUIContainer parent = null;
	/** The (optional) background image for this container */
	protected GUIImage background = null;
	/** A collection of components contained by the container, keyed by an identifier String */
	protected Map<String, GUIComponent> children;
	/** The order of the components in the container (used for TAB behavior). */
	protected String[] fieldOrder;
	/** The currently focused component within the container */
	protected GUIComponent focused;
	/** Holds the mouse button states */
	protected boolean[] mouse;
	/** Holds the keyboard states */
	protected boolean[] keys;
	/** The last keypress */
	protected int lastKey;
	/** The character represented by the last keypress */
	protected char lastKeyChar;
	/** The time when the last key was pressed */
	protected long lastKeyTime;
	
	/**
	 * Constructor
	 * @param x The x-coordinate of the container
	 * @param y The y-coordinate of the container
	 * @param width The width of the container
	 * @param height The height of the container
	 */
	public GUIContainer(int x, int y, int width, int height) {
		super(x, y, width, height);
		this.mouse = new boolean[Mouse.getButtonCount()];
		this.keys = new boolean[256];
		this.children = new HashMap<String, GUIComponent>();
		this.focused = null;
		this.fieldOrder = new String[] { };
		this.lastKey = -1;
		this.lastKeyChar = Keyboard.CHAR_NONE;
		this.lastKeyTime = Long.MAX_VALUE - KEY_WAIT;
	}
	
	public void init(int x, int y, int width, int height) { 
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public void reInit(int x, int y, int width, int height) { }
	
	public GUIContainer getParent() {
		return parent;
	}
	
	public void setParent(GUIContainer parent) {
		this.parent = parent;
	}
	
	public String[] getFieldOrder() {
		return this.fieldOrder;
	}
	
	public void setFieldOrder(String[] fieldOrder) {
		this.fieldOrder = fieldOrder;
	}
	
	public void setBackgroundImage(GUIImage image) {
		image.setPosition(image.getX() + getX(), image.getY() + getY());
		this.background = image;
	}
	
	/**
	 * Adds a child component to this Menu
	 * @param name The reference name of the child (e.g. "cancelbutton")
	 * @param child The child component to add
	 */
	public void addChild(String name, GUIComponent child) {
		if(child == null)
			return;
		// Parenting this to itself will result in infinite recursion
		if(child == this)
			return;
		if(children.containsKey(name))
			throw new IllegalArgumentException("Name must be unique for GUIContainer#addChild()!");
		if(child instanceof GUIButton)
			((GUIButton) child).setParent(this);
		// Set position relative to the container
		child.setPosition(child.getX() + getX(), child.getY() + getY());
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
	
	/**
	 * Renders the component
	 * By default, calls render() on each component
	 * @see net.calzoneman.TileLand.gui.GUIComponent#render()
	 */
	public void render() {
		if(background != null)
			background.render();
		for(GUIComponent comp : children.values()) {
			comp.render();
		}
		
	}
	
	/**
	 * Called when this container loses focus.
	 * By default, blurs all child components.
	 */
	@Override
	public void onBlur() {
		for(GUIComponent comp : children.values())
			comp.blur();
	}
	
	/**
	 * Reads input from the Mouse and Keyboard into event lists, and then calls
	 * handleInput on those lists.  This method should be called on the root GUIContainer,
	 * which then passes the event lists to child containers
	 * @see net.calzoneman.TileLand.gui.GUIContainer#handleInput(List, List)
	 */
	public void handleInput() {
		List<MouseEvent> mouseEvents = new ArrayList<MouseEvent>();
		while(Mouse.next()) {
			mouseEvents.add(new MouseEvent(Mouse.getEventButton(), Mouse.getEventButtonState(), Mouse.getX(), Display.getHeight() - Mouse.getY()));
		}
		
		List<KeyboardEvent> keyboardEvents = new ArrayList<KeyboardEvent>();
		while(Keyboard.next()) {
			keyboardEvents.add(new KeyboardEvent(Keyboard.getEventKey(), Keyboard.getEventCharacter(), Keyboard.getEventKeyState()));
		}
		handleInput(mouseEvents, keyboardEvents);
	}
	
	/**
	 * Handles input from the generated event lists.
	 * Handles hovering, clicking, and focusing of child components, and
	 * passes event lists to child GUIContainers so they can handle them for their
	 * children.
	 * @param mouseEvents A list of MouseEvents
	 * @param keyboardEvents A list of KeyboardEvents
	 * @see net.calzoneman.TileLand.gui.GUIContainer$MouseEvent
	 * @see net.calzoneman.TileLand.gui.GUIContainer$KeyboardEvent
	 */
	public void handleInput(List<MouseEvent> mouseEvents, List<KeyboardEvent> keyboardEvents) {
		boolean[] oldmouse = mouse.clone();
		Iterator<MouseEvent> itr = mouseEvents.iterator();
		while(itr.hasNext()) {
			MouseEvent me = itr.next();
			int button = me.button;
			if(button != -1)
				mouse[button] = me.state;
			if(oldmouse[0] && !mouse[0])
				clickUp(me.x, me.y);
			else if(!mouse[0])
				hover(me.x, me.y);
			else if(mouse[0] && !oldmouse[0])
				clickDown(me.x, me.y);
			
		}
		
		boolean[] oldkeys = keys.clone();
		Iterator<KeyboardEvent> itr2 = keyboardEvents.iterator();
		while(itr2.hasNext()) {
			KeyboardEvent ke = itr2.next();
			int key = ke.key;
			if(key != -1) 
				keys[key] = ke.keyState;
			if(key != -1 && keys[key] && !oldkeys[key])
				keypress(key, ke.keyChar);
			if(key != lastKey) {
				lastKeyTime = System.nanoTime();
				lastKey = key;
				lastKeyChar = ke.keyChar;
			}
		}
		
		// Check for held down keys
		for(int k = 0; k < keys.length; k++) {
			if(keys[k] && k == lastKey && System.nanoTime() > lastKeyTime + KEY_WAIT)
				keypress(k, lastKeyChar);
		}
		
		// Check for nested containers
		for(GUIComponent comp : children.values()) {
			if(comp instanceof GUIContainer) {
				((GUIContainer) comp).handleInput(mouseEvents, keyboardEvents);
			}
		}
	}
	
	/**
	 * Called when a KeyboardEvent is processed and accepted
	 * @param keycode The integer code for the pressed key
	 * @param keychar The character represented by the pressed key
	 * @see org.lwjgl.input.Keyboard
	 */
	private void keypress(int keycode, char keychar) {
		if(keycode == Keyboard.KEY_TAB)
			onTab();
		else if(keycode == Keyboard.KEY_RETURN)
			onEnter();
		else if(focused != null) {
			focused.onKey(keycode, keychar);
		}
	}
	
	/**
	 * Called when the TAB key is pressed.
	 * Attempts to cycle forward to the next field as defined by fieldOrder.
	 * Cycles in reverse if the LSHIFT key is held
	 */
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

	/**
	 * Called when a MouseEvent results in movement.
	 * Calls hover() and unhover() appropriately on children affected
	 * by the event.
	 * @see net.calzoneman.TileLand.gui.GUIComponent#hover()
	 * @see net.calzoneman.TileLand.gui.GUIComponent#unhover()
	 * @param mx The x-coordinate of the mouse
	 * @param my The y-coordinate of the mouse
	 */
	private void hover(int mx, int my) {
		for(GUIComponent comp : children.values()) {
			if(mx > comp.getX() && mx < (comp.getX() + comp.getWidth())
					&& my > comp.getY() && my < (comp.getY() + comp.getHeight()))
				comp.hover();
			else if(comp.isHovered())
				comp.unhover();
		}
	}
	
	/**
	 * Called when the MOUSE1 button is depressed.
	 * Calls focus() and blur() appropriately for children affected
	 * by the event.
	 * @see net.calzoneman.TileLand.gui.GUIComponent#focus()
	 * @see net.calzoneman.TileLand.gui.GUIComponent#blur()
	 * @param mx The x-coordinate of the mouse
	 * @param my The y-coordinate of the mouse
	 */
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
	
	/**
	 * Called when the MOUSE1 button is released.
	 * Calls onClick() and blur() appropriately for children affected
	 * by the event.
	 * @see net.calzoneman.TileLand.gui.GUIComponent#onClick()
	 * @see net.calzoneman.TileLand.gui.GUIComponent#blur()
	 * @param mx The x-coordinate of the mouse
	 * @param my The y-coordinate of the mouse
	 */
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
			else if(comp.isHovered()) {
				comp.unhover();
			}
		}
	}
	
	/**
	 * Resets all childrens' focus and hover
	 * @see net.calzoneman.TileLand.gui.GUIComponent#focus
	 * @see net.calzoneman.TileLand.gui.GUIComponent#hover
	 */
	public void resetAll() {
		for(GUIComponent comp : children.values()) {
			comp.blur();
			comp.unhover();
		}
	}
	
	public class MouseEvent {
		public int x;
		public int y;
		public int button;
		public boolean state;
		
		public MouseEvent(int button, boolean state, int x, int y) {
			this.button = button;
			this.state = state;
			this.x = x;
			this.y = y;
		}
	}
	
	public class KeyboardEvent {
		public int key;
		public char keyChar;
		public boolean keyState;
		
		public KeyboardEvent(int key, char keyChar, boolean keyState) {
			this.key = key;
			this.keyChar = keyChar;
			this.keyState = keyState;
		}
	}
}
