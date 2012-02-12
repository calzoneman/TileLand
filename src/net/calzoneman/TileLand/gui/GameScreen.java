package net.calzoneman.TileLand.gui;

import net.calzoneman.TileLand.Game;

public abstract class GameScreen {
	/** The Game* that this screen is a child of 
	 *  * Also you just lost the game ;)
	 */
	protected Game parent;
	/** Whether or not this screen wants control.
	 * When set to true, the parent should pass input handling and call handleInput()
	 * When set to false, the parent should terminate the screen and resume control
	 */
	public boolean active = true;
	
	protected int x;
	protected int y;
	protected int width;
	protected int height;
	
	/**
	 * Constructor
	 * @param x The x-coordinate of the upper left corner of this GameScreen
	 * @param y The y-coordinate of the upper left corner of this GameScreen
	 * @param width The width of this GameScreen
	 * @param height The height of this GameScreen
	 */
	public GameScreen(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	/**
	 * Constructor
	 * @param x The x-coordinate of the upper left corner of this GameScreen
	 * @param y The y-coordinate of the upper left corner of this GameScreen
	 * @param width The width of this GameScreen
	 * @param height The height of this GameScreen
	 * @param parent The Game which parents this GameScreen
	 */
	public GameScreen(int x, int y, int width, int height, Game parent) {
		this(x, y, width, height);
		this.parent = parent;
	}
	
	/**
	 * Renders the screen
	 */
	public abstract void render();
	
	/**
	 * Reads user input from the Mouse and/or Keyboard and updates appropriately
	 */
	public abstract void handleInput();
	
	/**
	 * Called by this GameScreen's parent when it resumes control.
	 * This method should clean up resources used by the GameScreen and
	 * complete any operations that depend on user input
	 */
	public void onClosing() { }
	
	/**
	 * Getter for parent
	 * @see net.calzoneman.TileLand.gui.GameScreen#parent
	 * @return The parent Game for this GameScreen
	 */
	public Game getParent() {
		return this.parent;
	}
	
	/**
	 * Setter for parent
	 * @see net.calzoneman.TileLand.gui.GameScreen#parent
	 */
	public void setParent(Game parent) {
		this.parent = parent;
	}
}
