package net.calzoneman.TileLand.gui;

import net.calzoneman.TileLand.Game;

public abstract class GameScreen extends GUIComponent {
	/** The Game* that this screen is a child of 
	 *  * Also you just lost the game ;)
	 */
	protected Game parent;
	/** Whether or not this screen wants control.
	 * When set to true, the parent should pass input handling and call handleInput()
	 * When set to false, the parent should terminate the screen and resume control
	 */
	public boolean active = true;
	
	/**
	 * Inherited Constructor
	 * @param x The x-coordinate of the upper left corner of this GameScreen
	 * @param y The y-coordinate of the upper left corner of this GameScreen
	 * @param width The width of this GameScreen
	 * @param height The height of this GameScreen
	 */
	public GameScreen(int x, int y, int width, int height) {
		super(x, y, width, height);
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
		super(x, y, width, height);
		this.parent = parent;
	}
	
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
