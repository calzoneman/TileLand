package net.calzoneman.TileLand.level;

public class Location {
	public int x;
	public int y;
	
	public Location(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Location(Location clone) {
		this.x = clone.x;
		this.y = clone.y;
	}
	
	@Override
	public boolean equals(Object other) {
		if(!(other instanceof Location)) return false;
		Location loc = (Location)other;
		return (loc.x == x && loc.y == y);
	}
	
	@Override
	public String toString() {
		return "[Location: x=" + x + ", y=" + y + "]";
	}
}
