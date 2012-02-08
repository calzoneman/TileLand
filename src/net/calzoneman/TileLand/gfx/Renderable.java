package net.calzoneman.TileLand.gfx;

/**
 * The Renderable interface describes an Object which can render itself to the screen.
 * Implementors may make use of basic drawing functions provided in Renderer and/or implement
 * their own rendering routines.
 * @author Calvin Montgomery
 *
 */
public interface Renderable {
	/**
	 * Render the object to the screen at coordinate (x, y) (origin is the top-left of the window)
	 * @param x The x coordinate at which to render
	 * @param y The y coordiante at which to render
	 */
	public abstract void render(int x, int y);
	
	/**
	 * Render the object to the screen at coordinate (x, y) (origin is the top-left of the window)
	 * @param x The x coordinate at which to render
	 * @param y The y coordiante at which to render
	 * @param data A parameter specifying a state or data on which the rendering depends (e.g. a frame of an animated texture)
	 */
	public abstract void render(int x, int y, int data);
}
