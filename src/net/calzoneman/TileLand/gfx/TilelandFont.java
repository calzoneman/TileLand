package net.calzoneman.TileLand.gfx;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;

import org.newdawn.slick.Color;
import org.newdawn.slick.UnicodeFont;

public class TilelandFont{
	
	public static final char COLOR_CODE_DELIMITER = '«';
	
	/** Color definitions */
	
	public static final Color COLOR_BLACK = new Color(0, 0, 0);
	public static final Color COLOR_DARK_RED = new Color(170, 0, 0);
	public static final Color COLOR_DARK_GREEN = new Color(0, 170, 0);
	public static final Color COLOR_BROWN = new Color(170, 85, 0);
	public static final Color COLOR_DARK_BLUE = new Color(0, 0, 170);
	public static final Color COLOR_DARK_MAGENTA = new Color(170, 0, 170);
	public static final Color COLOR_DARK_CYAN = new Color(0, 170, 170);
	public static final Color COLOR_GRAY = new Color(170, 170, 170);
	public static final Color COLOR_DARK_GRAY = new Color(85, 85, 85);
	public static final Color COLOR_RED = new Color(255, 85, 85);
	public static final Color COLOR_GREEN = new Color(85, 255, 85);
	public static final Color COLOR_YELLOW = new Color(255, 255, 85);
	public static final Color COLOR_BLUE = new Color(85, 85, 255);
	public static final Color COLOR_MAGENTA = new Color(255, 85, 255);
	public static final Color COLOR_CYAN = new Color(85, 255, 255);
	public static final Color COLOR_WHITE = new Color(255, 255, 255);
	
	/** Color code definitions */
	public static final String TEXT_BLACK = COLOR_CODE_DELIMITER + "0";
	public static final String TEXT_DARK_RED = COLOR_CODE_DELIMITER + "1";
	public static final String TEXT_DARK_GREEN = COLOR_CODE_DELIMITER + "2";
	public static final String TEXT_BROWN = COLOR_CODE_DELIMITER + "3";
	public static final String TEXT_DARK_BLUE = COLOR_CODE_DELIMITER + "4";
	public static final String TEXT_DARK_MAGENTA = COLOR_CODE_DELIMITER + "5";
	public static final String TEXT_DARK_CYAN = COLOR_CODE_DELIMITER + "6";
	public static final String TEXT_GRAY = COLOR_CODE_DELIMITER + "7";
	public static final String TEXT_DARK_GRAY = COLOR_CODE_DELIMITER + "8";
	public static final String TEXT_RED = COLOR_CODE_DELIMITER + "9";
	public static final String TEXT_GREEN = COLOR_CODE_DELIMITER + "a";
	public static final String TEXT_YELLOW = COLOR_CODE_DELIMITER + "b";
	public static final String TEXT_BLUE = COLOR_CODE_DELIMITER + "c";
	public static final String TEXT_MAGENTA = COLOR_CODE_DELIMITER + "d";
	public static final String TEXT_CYAN = COLOR_CODE_DELIMITER + "e";
	public static final String TEXT_WHITE = COLOR_CODE_DELIMITER + "f";
	
	
	private UnicodeFont font;
	
	public TilelandFont(UnicodeFont fnt) {
		this.font = fnt;
	}
	
	public void drawString(int x, int y, String str) {
		drawString(x, y, str, COLOR_BLACK);
	}
	
	public void drawString(int x, int y, String str, Color bg) {
		int i = str.indexOf(COLOR_CODE_DELIMITER);
		int dx = 0;
		int start = 0;
		Color current = COLOR_WHITE;
		while(i != -1) {
			// Ignore if escaped with a backslash
			if(i != 0 && str.charAt(i-1) == '\\') {
				i = str.indexOf(COLOR_CODE_DELIMITER, i+1);
				continue;
			}
			else if(i+1 < str.length()) {
				String sub = str.substring(start, i);
				drawBasicString(x+dx, y, sub, current, bg);
				current = getColor(str.charAt(i+1));
				start = i+2;
				dx += font.getWidth(sub);
				i = str.indexOf(COLOR_CODE_DELIMITER, i+1);
			}
			else {
				i = str.indexOf(COLOR_CODE_DELIMITER, i+1);
			}
		}
		String sub = str.substring(start);
		drawBasicString(x+dx, y, sub, current, bg);
		COLOR_WHITE.bind();
	}
	
	// Strips color codes
	public static String stripColor(String str) {
		String out = "";
		char[] temp = str.toCharArray();
		for(int i = 0; i < temp.length; i++) {
			if(temp[i] == COLOR_CODE_DELIMITER) {
				temp[i] = 0;
				if(i+1 < temp.length)
					temp[i+1] = 0;
			}
			if(temp[i] != 0)
				out += temp[i];
		}
		return out;
	}
	
	private void drawBasicString(int x, int y, String str, Color fg, Color bg) {
		Renderer.renderFilledRect(x, y, font.getWidth(str), font.getHeight(str), bg);
		glEnable(GL_BLEND);
		font.drawString(x, y, str, fg);
		glDisable(GL_BLEND);
	}
	
	public int getWidth(String str) {
			int i = str.indexOf(COLOR_CODE_DELIMITER);
			int start = 0;
			int width = 0;
			if(i == -1) {
				return font.getWidth(str);
			}
			while(i != -1) {
				// Ignore if escaped with a backslash
				if(i != 0 && str.charAt(i-1) == '\\') {
					i = str.indexOf(COLOR_CODE_DELIMITER, i+1);
					continue;
				}
				else if(i+1 < str.length()) {
					String sub = str.substring(start, i);
					width += font.getWidth(sub);
					start = i+2;
					i = str.indexOf(COLOR_CODE_DELIMITER, i+1);
				}
				else {
					i = str.indexOf(COLOR_CODE_DELIMITER, i+1);
				}
			}
			String sub = str.substring(start);
			return width + font.getWidth(sub);
	}
	
	public int getHeight(String str) {
		return font.getHeight(str);
	}
	
	public Color getColor(char code) {
		switch(code) {
			case '0':
				return COLOR_BLACK;
			case '1':
				return COLOR_DARK_RED;
			case '2':
				return COLOR_DARK_GREEN;
			case '3':
				return COLOR_BROWN;
			case '4':
				return COLOR_DARK_BLUE;
			case '5':
				return COLOR_DARK_MAGENTA;
			case '6':
				return COLOR_DARK_CYAN;
			case '7':
				return COLOR_GRAY;
			case '8':
				return COLOR_DARK_GRAY;
			case '9':
				return COLOR_RED;
			case 'a':
				return COLOR_GREEN;
			case 'b':
				return COLOR_YELLOW;
			case 'c':
				return COLOR_BLUE;
			case 'd':
				return COLOR_MAGENTA;
			case 'e':
				return COLOR_CYAN;
			default:
				return COLOR_WHITE;
		}
	}
	
	public Color getColor(String code) {
		if(code.length() == 2)
			return getColor(code.charAt(1));
		return COLOR_WHITE;
	}

}
