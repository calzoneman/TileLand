package net.calzoneman.TileLand.tile;

import java.util.HashMap;

import net.calzoneman.TileLand.level.Level;
import net.calzoneman.TileLand.level.Location;

import org.newdawn.slick.geom.Rectangle;

public class MultidirectionalTile extends Tile {
	protected HashMap<Integer, Rectangle> orientationTextures;
	protected boolean foreground;

	public MultidirectionalTile(int id, String name, Rectangle texPosition, boolean foreground) {
		super(id, name, texPosition);
		this.foreground = foreground;
		this.properties = TileProperties.MULTIDIRECTIONAL;
		this.orientationTextures = new HashMap<Integer, Rectangle>();
		loadOrientations(texPosition);
		this.texPosition = orientationTextures.get(TileOrientation.CENTER);
	}
	
	protected void loadOrientations(Rectangle rect) {
		int x = (int) rect.getX();
		int y = (int) rect.getY();
		orientationTextures.put(TileOrientation.TOP_CORNER_LEFT, new Rectangle(x, y, 32, 32));
		orientationTextures.put(TileOrientation.TOP_EDGE, new Rectangle(x+32, y, 32, 32));
		orientationTextures.put(TileOrientation.TOP_CORNER_RIGHT, new Rectangle(x+64, y, 32, 32));
		orientationTextures.put(TileOrientation.LEFT_EDGE, new Rectangle(x, y+32, 32, 32));
		orientationTextures.put(TileOrientation.CENTER, new Rectangle(x+32, y+32, 32, 32));
		orientationTextures.put(TileOrientation.RIGHT_EDGE, new Rectangle(x+64, y+32, 32, 32));
		orientationTextures.put(TileOrientation.BOTTOM_CORNER_LEFT, new Rectangle(x, y+64, 32, 32));
		orientationTextures.put(TileOrientation.BOTTOM_EDGE, new Rectangle(x+32, y+64, 32, 32));
		orientationTextures.put(TileOrientation.BOTTOM_CORNER_RIGHT, new Rectangle(x+64, y+64, 32, 32));
	}
	
	@Override
	public void update(Level level, Location self, Location src) {
		if(foreground)
			return;
		else
			updateBg(level, self, src);
	}
	
	/**
	 * Updates the background layer
	 * @see update()
	 */
	protected void updateBg(Level level, Location self, Location src) {
		Location[] neighbors = new Location[] {
				new Location(self.x + 1, self.y),
				new Location(self.x - 1, self.y),
				new Location(self.x, self.y + 1),
				new Location(self.x, self.y - 1)
		};
		int newOrientation = TileOrientation.CENTER;
		int count = 0;
		for(Location loc : neighbors) {
			if(level.getBgId(loc.x, loc.y) == getId())
				count++;
		}
		if (count == 0) 
			return; // Nothing to update
		if (count == 1) {
			if(level.getBgId(self.x + 1, self.y) == getId()) {
				if(level.getBgData(self.x + 1, self.y) == TileOrientation.TOP_CORNER_RIGHT || level.getBgData(self.x + 1, self.y) == TileOrientation.TOP_EDGE)
					newOrientation = TileOrientation.TOP_CORNER_LEFT;
				else if(level.getBgData(self.x + 1, self.y) == TileOrientation.BOTTOM_CORNER_RIGHT || level.getBgData(self.x + 1, self.y) == TileOrientation.BOTTOM_EDGE)
					newOrientation = TileOrientation.BOTTOM_CORNER_LEFT;
			}
			else if(level.getBgId(self.x - 1, self.y) == getId()) {
				if(level.getBgData(self.x - 1, self.y) == TileOrientation.TOP_CORNER_LEFT || level.getBgData(self.x - 1, self.y) == TileOrientation.TOP_EDGE)
					newOrientation = TileOrientation.TOP_CORNER_RIGHT;
				else if(level.getBgData(self.x - 1, self.y) == TileOrientation.BOTTOM_CORNER_LEFT || level.getBgData(self.x - 1, self.y) == TileOrientation.BOTTOM_EDGE)
					newOrientation = TileOrientation.BOTTOM_CORNER_RIGHT;
			}
			else if(level.getBgId(self.x, self.y + 1) == getId()) {
				if(level.getBgData(self.x, self.y + 1) == TileOrientation.BOTTOM_CORNER_LEFT || level.getBgData(self.x, self.y + 1) == TileOrientation.LEFT_EDGE)
					newOrientation = TileOrientation.TOP_CORNER_LEFT;
				else if(level.getBgData(self.x, self.y + 1) == TileOrientation.BOTTOM_CORNER_RIGHT || level.getBgData(self.x, self.y + 1) == TileOrientation.RIGHT_EDGE)
					newOrientation = TileOrientation.TOP_CORNER_RIGHT;
				else if(level.getBgData(self.x, self.y + 1) == TileOrientation.BOTTOM_EDGE)
					newOrientation = TileOrientation.TOP_EDGE;
			}
			else if(level.getBgId(self.x, self.y - 1) == getId()) {
				if(level.getBgData(self.x, self.y - 1) == TileOrientation.TOP_CORNER_LEFT || level.getBgData(self.x, self.y - 1) == TileOrientation.LEFT_EDGE)
					newOrientation = TileOrientation.BOTTOM_CORNER_LEFT;
				else if(level.getBgData(self.x, self.y - 1) == TileOrientation.TOP_CORNER_RIGHT || level.getBgData(self.x, self.y - 1) == TileOrientation.RIGHT_EDGE)
					newOrientation = TileOrientation.BOTTOM_CORNER_RIGHT;
				else if(level.getBgData(self.x, self.y - 1) == TileOrientation.TOP_EDGE)
					newOrientation = TileOrientation.BOTTOM_EDGE;
			}
		}
		
		else if(count == 2) {
			if(level.getBgId(self.x + 1, self.y) == getId() && level.getBgId(self.x - 1, self.y) == getId()) {
				if(level.getBgData(self.x + 1, self.y) == TileOrientation.BOTTOM_CORNER_RIGHT || level.getBgData(self.x - 1, self.y) == TileOrientation.BOTTOM_CORNER_LEFT
						|| level.getBgData(self.x + 1, self.y) == TileOrientation.BOTTOM_EDGE || level.getBgData(self.x - 1, self.y) == TileOrientation.BOTTOM_EDGE)
					newOrientation = TileOrientation.BOTTOM_EDGE;
				else if(level.getBgData(self.x + 1, self.y) == TileOrientation.TOP_CORNER_RIGHT || level.getBgData(self.x - 1, self.y) == TileOrientation.TOP_CORNER_LEFT
						|| level.getBgData(self.x + 1, self.y) == TileOrientation.TOP_EDGE || level.getBgData(self.x - 1, self.y) == TileOrientation.TOP_EDGE)
					newOrientation = TileOrientation.TOP_EDGE;
			}
			else if(level.getBgId(self.x, self.y + 1) == getId() && level.getBgId(self.x, self.y - 1) == getId()) {
				if(level.getBgData(self.x, self.y + 1) == TileOrientation.BOTTOM_CORNER_LEFT || level.getBgData(self.x, self.y - 1) == TileOrientation.TOP_CORNER_LEFT
						|| level.getBgData(self.x, self.y + 1) == TileOrientation.LEFT_EDGE || level.getBgData(self.x, self.y - 1) == TileOrientation.LEFT_EDGE)
					newOrientation = TileOrientation.LEFT_EDGE;
				else if(level.getBgData(self.x, self.y + 1) == TileOrientation.BOTTOM_CORNER_RIGHT || level.getBgData(self.x, self.y - 1) == TileOrientation.TOP_CORNER_RIGHT
						|| level.getBgData(self.x, self.y + 1) == TileOrientation.RIGHT_EDGE || level.getBgData(self.x, self.y - 1) == TileOrientation.RIGHT_EDGE)
					newOrientation = TileOrientation.RIGHT_EDGE;
			}
			else if(level.getBgId(self.x - 1, self.y) == getId() && level.getBgId(self.x, self.y - 1) == getId()) {
				newOrientation = TileOrientation.BOTTOM_CORNER_RIGHT;
			}
			else if(level.getBgId(self.x + 1, self.y) == getId() && level.getBgId(self.x, self.y - 1) == getId()) {
				newOrientation = TileOrientation.BOTTOM_CORNER_LEFT;
			}
			else if(level.getBgId(self.x + 1, self.y) == getId() && level.getBgId(self.x, self.y + 1) == getId()) {
				newOrientation = TileOrientation.TOP_CORNER_LEFT;
			}
			else if(level.getBgId(self.x - 1, self.y) == getId() && level.getBgId(self.x, self.y + 1) == getId()) {
				newOrientation = TileOrientation.TOP_CORNER_RIGHT;
			}
		}
		
		else if(count == 3) {
			if(level.getBgId(self.x - 1, self.y) == getId() && level.getBgId(self.x + 1, self.y) == getId() && level.getBgId(self.x, self.y - 1) == getId())
				newOrientation = TileOrientation.BOTTOM_EDGE;
			else if(level.getBgId(self.x, self.y - 1) == getId() && level.getBgId(self.x + 1, self.y) == getId() && level.getBgId(self.x, self.y + 1) == getId())
				newOrientation = TileOrientation.LEFT_EDGE;
			else if(level.getBgId(self.x - 1, self.y) == getId() && level.getBgId(self.x + 1, self.y) == getId() && level.getBgId(self.x, self.y + 1) == getId())
				newOrientation = TileOrientation.TOP_EDGE;
			else if(level.getBgId(self.x - 1, self.y) == getId() && level.getBgId(self.x, self.y - 1) == getId() && level.getBgId(self.x, self.y + 1) == getId())
				newOrientation = TileOrientation.RIGHT_EDGE;
		}
		
		level.setBgData(self.x, self.y, newOrientation);
	}
	
	/**
	 * Updates the foreground layer
	 * @see update()
	 */
	protected void updateFg(Level level, Location self, Location src) {
		Location[] neighbors = new Location[] {
				new Location(self.x + 1, self.y),
				new Location(self.x - 1, self.y),
				new Location(self.x, self.y + 1),
				new Location(self.x, self.y - 1)
		};
		int newOrientation = TileOrientation.CENTER;
		int count = 0;
		for(Location loc : neighbors) {
			if(level.getFgId(loc.x, loc.y) == getId())
				count++;
		}
		if (count == 0) 
			return; // Nothing to update
		if (count == 1) {
			if(level.getFgId(self.x + 1, self.y) == getId()) {
				if(level.getFgData(self.x + 1, self.y) == TileOrientation.TOP_CORNER_RIGHT || level.getFgData(self.x + 1, self.y) == TileOrientation.TOP_EDGE)
					newOrientation = TileOrientation.TOP_CORNER_LEFT;
				else if(level.getFgData(self.x + 1, self.y) == TileOrientation.BOTTOM_CORNER_RIGHT || level.getFgData(self.x + 1, self.y) == TileOrientation.BOTTOM_EDGE)
					newOrientation = TileOrientation.BOTTOM_CORNER_LEFT;
			}
			else if(level.getFgId(self.x - 1, self.y) == getId()) {
				if(level.getFgData(self.x - 1, self.y) == TileOrientation.TOP_CORNER_LEFT || level.getFgData(self.x - 1, self.y) == TileOrientation.TOP_EDGE)
					newOrientation = TileOrientation.TOP_CORNER_RIGHT;
				else if(level.getFgData(self.x - 1, self.y) == TileOrientation.BOTTOM_CORNER_LEFT || level.getFgData(self.x - 1, self.y) == TileOrientation.BOTTOM_EDGE)
					newOrientation = TileOrientation.BOTTOM_CORNER_RIGHT;
			}
			else if(level.getFgId(self.x, self.y + 1) == getId()) {
				if(level.getFgData(self.x, self.y + 1) == TileOrientation.BOTTOM_CORNER_LEFT || level.getFgData(self.x, self.y + 1) == TileOrientation.LEFT_EDGE)
					newOrientation = TileOrientation.TOP_CORNER_LEFT;
				else if(level.getFgData(self.x, self.y + 1) == TileOrientation.BOTTOM_CORNER_RIGHT || level.getFgData(self.x, self.y + 1) == TileOrientation.RIGHT_EDGE)
					newOrientation = TileOrientation.TOP_CORNER_RIGHT;
				else if(level.getFgData(self.x, self.y + 1) == TileOrientation.BOTTOM_EDGE)
					newOrientation = TileOrientation.TOP_EDGE;
			}
			else if(level.getFgId(self.x, self.y - 1) == getId()) {
				if(level.getFgData(self.x, self.y - 1) == TileOrientation.TOP_CORNER_LEFT || level.getFgData(self.x, self.y - 1) == TileOrientation.LEFT_EDGE)
					newOrientation = TileOrientation.BOTTOM_CORNER_LEFT;
				else if(level.getFgData(self.x, self.y - 1) == TileOrientation.TOP_CORNER_RIGHT || level.getFgData(self.x, self.y - 1) == TileOrientation.RIGHT_EDGE)
					newOrientation = TileOrientation.BOTTOM_CORNER_RIGHT;
				else if(level.getFgData(self.x, self.y - 1) == TileOrientation.TOP_EDGE)
					newOrientation = TileOrientation.BOTTOM_EDGE;
			}
		}
		
		else if(count == 2) {
			if(level.getFgId(self.x + 1, self.y) == getId() && level.getFgId(self.x - 1, self.y) == getId()) {
				if(level.getFgData(self.x + 1, self.y) == TileOrientation.BOTTOM_CORNER_RIGHT || level.getFgData(self.x - 1, self.y) == TileOrientation.BOTTOM_CORNER_LEFT
						|| level.getFgData(self.x + 1, self.y) == TileOrientation.BOTTOM_EDGE || level.getFgData(self.x - 1, self.y) == TileOrientation.BOTTOM_EDGE)
					newOrientation = TileOrientation.BOTTOM_EDGE;
				else if(level.getFgData(self.x + 1, self.y) == TileOrientation.TOP_CORNER_RIGHT || level.getFgData(self.x - 1, self.y) == TileOrientation.TOP_CORNER_LEFT
						|| level.getFgData(self.x + 1, self.y) == TileOrientation.TOP_EDGE || level.getFgData(self.x - 1, self.y) == TileOrientation.TOP_EDGE)
					newOrientation = TileOrientation.TOP_EDGE;
			}
			else if(level.getFgId(self.x, self.y + 1) == getId() && level.getFgId(self.x, self.y - 1) == getId()) {
				if(level.getFgData(self.x, self.y + 1) == TileOrientation.BOTTOM_CORNER_LEFT || level.getFgData(self.x, self.y - 1) == TileOrientation.TOP_CORNER_LEFT
						|| level.getFgData(self.x, self.y + 1) == TileOrientation.LEFT_EDGE || level.getFgData(self.x, self.y - 1) == TileOrientation.LEFT_EDGE)
					newOrientation = TileOrientation.LEFT_EDGE;
				else if(level.getFgData(self.x, self.y + 1) == TileOrientation.BOTTOM_CORNER_RIGHT || level.getFgData(self.x, self.y - 1) == TileOrientation.TOP_CORNER_RIGHT
						|| level.getFgData(self.x, self.y + 1) == TileOrientation.RIGHT_EDGE || level.getFgData(self.x, self.y - 1) == TileOrientation.RIGHT_EDGE)
					newOrientation = TileOrientation.RIGHT_EDGE;
			}
			else if(level.getFgId(self.x - 1, self.y) == getId() && level.getFgId(self.x, self.y - 1) == getId()) {
				newOrientation = TileOrientation.BOTTOM_CORNER_RIGHT;
			}
			else if(level.getFgId(self.x + 1, self.y) == getId() && level.getFgId(self.x, self.y - 1) == getId()) {
				newOrientation = TileOrientation.BOTTOM_CORNER_LEFT;
			}
			else if(level.getFgId(self.x + 1, self.y) == getId() && level.getFgId(self.x, self.y + 1) == getId()) {
				newOrientation = TileOrientation.TOP_CORNER_LEFT;
			}
			else if(level.getFgId(self.x - 1, self.y) == getId() && level.getFgId(self.x, self.y + 1) == getId()) {
				newOrientation = TileOrientation.TOP_CORNER_RIGHT;
			}
		}
		
		else if(count == 3) {
			if(level.getFgId(self.x - 1, self.y) == getId() && level.getFgId(self.x + 1, self.y) == getId() && level.getFgId(self.x, self.y - 1) == getId())
				newOrientation = TileOrientation.BOTTOM_EDGE;
			else if(level.getFgId(self.x, self.y - 1) == getId() && level.getFgId(self.x + 1, self.y) == getId() && level.getFgId(self.x, self.y + 1) == getId())
				newOrientation = TileOrientation.LEFT_EDGE;
			else if(level.getFgId(self.x - 1, self.y) == getId() && level.getFgId(self.x + 1, self.y) == getId() && level.getFgId(self.x, self.y + 1) == getId())
				newOrientation = TileOrientation.TOP_EDGE;
			else if(level.getFgId(self.x - 1, self.y) == getId() && level.getFgId(self.x, self.y - 1) == getId() && level.getFgId(self.x, self.y + 1) == getId())
				newOrientation = TileOrientation.RIGHT_EDGE;
		}
		
		level.setFgData(self.x, self.y, newOrientation);
	}
	
	@Override
	public Rectangle getTexPosition(int orientation) {
		if(orientationTextures.containsKey(orientation))
			return orientationTextures.get(orientation);
		return texPosition;
	}
}
