package net.calzoneman.TileLand.gfx;

import static org.lwjgl.opengl.GL11.*;

import org.newdawn.slick.opengl.Texture;

public class PlayerSprite implements Renderable {
	public static final int FACING_UP = 0;
	public static final int FACING_RIGHT = 1;
	public static final int FACING_DOWN = 2;
	public static final int FACING_LEFT = 3;
	
	public static final int PLAYER_WIDTH = 32;
	public static final int PLAYER_HEIGHT = 42;
	
	private Texture texture;
	private int currentX;
	private int currentY;
	
	public PlayerSprite(Texture tex) {
		this.texture = tex;
		this.currentX = 0;
		this.currentY = FACING_DOWN * PLAYER_HEIGHT;
	}
	
	public void setFacing(int facing) {
		if(facing * PLAYER_HEIGHT != currentY) {
			currentX = 0;
			currentY = facing * PLAYER_HEIGHT;
		}
	}
	
	public void nextFrame() {
		currentX += PLAYER_WIDTH;
		if(currentX >= texture.getImageWidth())
			currentX = 0;
	}
	
	public void resetFrame() {
		currentX = 0;
	}
	
	public void render(int x, int y) {
		float texWidth = texture.getTextureWidth();
		float texHeight = texture.getTextureHeight();
		texture.bind();
		glEnable(GL_BLEND);
		glBegin(GL_QUADS);
			glTexCoord2f(currentX / texWidth, currentY / texHeight);
			glVertex2f(x, y);
			glTexCoord2f((currentX + PLAYER_WIDTH) / texWidth, currentY / texHeight);
			glVertex2f(x + PLAYER_WIDTH, y);
			glTexCoord2f((currentX + PLAYER_WIDTH) / texWidth, (currentY + PLAYER_HEIGHT) / texHeight);
			glVertex2f(x + PLAYER_WIDTH, y + PLAYER_HEIGHT);
			glTexCoord2f(currentX / texWidth, (currentY + PLAYER_HEIGHT) / texHeight);
			glVertex2f(x, y + PLAYER_HEIGHT);
		glEnd();
		glDisable(GL_BLEND);
	}

	@Override
	public void render(int x, int y, int data) {
		render(x, y);
	}
}
