package net.calzoneman.TileLand.util;

public interface State {
	public abstract void handleInput();
	public abstract void render();
	public abstract boolean isActive();
	public abstract void setActive(boolean active);
}
