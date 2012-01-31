package net.calzoneman.TileLand;

import java.io.File;
import java.util.HashMap;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class TextureManager {
	private HashMap<String, Texture> textures;
	final String[] validFormats = { "PNG", "TGA" };
	private UnicodeFont defaultFont;
	
	public TextureManager() {
		this("res");
	}
	
	public TextureManager(String sourcefolder) {
		textures = new HashMap<String, Texture>();
		File src = new File(sourcefolder);
		for(File f : src.listFiles()) {
			if(f.isFile()) {
				if(f.getName().equals("default.ttf")) {
					try {
						defaultFont = new UnicodeFont(f.getPath(), 8, false, false);
						defaultFont.addAsciiGlyphs();
						defaultFont.getEffects().add(new ColorEffect()); // For some reason you have to add an effect...
						defaultFont.loadGlyphs();
						System.out.println("INFO: Loaded font from default.ttf");
					} 
					catch (SlickException e) {
						System.out.println("Unable to load default font!");
					}
				}
				String fmt = "";
				if(!f.getName().contains("."))
					continue;
				fmt = f.getName().substring(f.getName().lastIndexOf(".") + 1);
				if(!isFormatValid(fmt))
					continue;
				try {
					Texture t = TextureLoader.getTexture(fmt, ResourceLoader.getResourceAsStream(f.getPath()));
					textures.put(f.getName(), t);
					System.out.println("INFO: Loaded texture from " + f.getName());
				}
				catch(Exception e) {
					System.out.println("WARNING: Failed to load texture from " + f.getName() + " (Parsed format: " + fmt + ")");
					e.printStackTrace();
				}
				
			}
		}
	}
	
	public Texture getTexture(String name) {
		return textures.get(name);
	}
	
	public UnicodeFont getDefaultFont() {
		return defaultFont;
	}
	
	public boolean isFormatValid(String fmt) {
		fmt = fmt.toUpperCase();
		for(String valid : validFormats)
			if(valid.equals(fmt))
				return true;
		return false;
	}
}
