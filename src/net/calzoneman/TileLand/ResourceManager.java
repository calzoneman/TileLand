package net.calzoneman.TileLand;

import java.io.File;
import java.util.HashMap;

import net.calzoneman.TileLand.gfx.SpriteSheet;
import net.calzoneman.TileLand.gfx.TilelandFont;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class ResourceManager {
	private HashMap<String, Texture> textures;
	private HashMap<String, SpriteSheet> tileSpritesheets;
	private HashMap<String, TilelandFont> fonts;
	private TilelandFont preferredFont = null;
	private Texture preferredTiles = null;
	private Texture preferredPlayer = null;
	final String[] textureFormats = { "PNG", "TGA" };
	final String[] fontFormats = { "TTF" };
	
	public ResourceManager() {
		this("res");
	}
	
	public ResourceManager(String sourcefolder) {
		textures = new HashMap<String, Texture>();
		tileSpritesheets = new HashMap<String, SpriteSheet>();
		fonts = new HashMap<String, TilelandFont>();
		processFolder(sourcefolder, true);
		dump();
	}
	
	public void processFolder(String folder, boolean recursive) {
		File src = new File(folder);
		for(File f : src.listFiles()) {
			if(f.isDirectory() && recursive)
				processFolder(f.getPath(), true);//folder + File.pathSeparator + f.getName(), true);
			else if(f.isFile()) {
				String fmt = "";
				if(!f.getName().contains("."))
					continue;
				fmt = f.getName().substring(f.getName().lastIndexOf(".") + 1);
				if(isTexture(fmt))
					loadTexture(f, fmt);
				else if(isFont(fmt))
					loadFont(f);
			}
		}
	}
	
	public boolean loadTexture(File f, String fmt) {
		try {
			Texture t = TextureLoader.getTexture(fmt, ResourceLoader.getResourceAsStream(f.getPath()));
			textures.put(f.getPath().replace('\\', '/'), t);
			System.out.println("INFO: Loaded texture from " + f.getName());
			return true;
		}
		catch(Exception e) {
			System.out.println("WARNING: Failed to load texture from " + f.getName() + " (Parsed format: " + fmt + ")");
			e.printStackTrace();
			return false;
		}
	}
	
	@SuppressWarnings("unchecked")
	public boolean loadFont(File f) {
		try {
			UnicodeFont fnt = new UnicodeFont(f.getPath(), 8, false, false);
			fnt.addAsciiGlyphs();
			fnt.getEffects().add(new ColorEffect()); // For some reason you have to add an effect...
			fnt.loadGlyphs();
			fonts.put(f.getPath().replace('\\', '/'), new TilelandFont(fnt));
			System.out.println("INFO: Loaded font from " + f.getName());
			return true;
		} 
		catch (SlickException e) {
			System.out.println("Unable to load font: " + f.getName());
			return false;
		}
	}
	
	public HashMap<String, Texture> getTextures(String path) {
		HashMap<String, Texture> result = new HashMap<String, Texture>();
		for(String key : textures.keySet()) {
			if (key.startsWith(path)) {
				result.put(key, getTexture(key));
			}
		}
		return result;
	}
	
	public Texture getPreferredTiles() {
		if(preferredTiles == null)
			return getTexture("res/tiles/default.png");
		return preferredTiles;
	}
	
	public void setPreferredTiles(String preferredTiles) {
		this.preferredTiles = getTexture(preferredTiles);
	}
	
	public Texture getPreferredPlayer() {
		if(preferredPlayer == null)
			return getTexture("res/player/default.png");
		return preferredPlayer;
	}
	
	public void setPreferredPlayer(String preferredPlayer) {
		this.preferredPlayer = getTexture(preferredPlayer);
	}
	
	public TilelandFont getPreferredFont() {
		if(preferredFont == null)
			return getDefaultFont();
		return preferredFont;
	}
	
	public void setPreferredFont(String preferredFont) {
		this.preferredFont = getFont(preferredFont);
	}

	public TilelandFont getDefaultFont() {
		return fonts.get("res/font/default.ttf");
	}
	
	public TilelandFont getFont(String name) {
		return fonts.get(name);
	}
	
	@SuppressWarnings("unchecked")
	public TilelandFont getFont(String name, int pt) {
		if(fonts.containsKey(name + "@" + pt))
			return fonts.get(name + "@" + pt);
		else {
			try {
				UnicodeFont fnt = new UnicodeFont(name, pt, false, false);
				fnt.addAsciiGlyphs();
				fnt.getEffects().add(new ColorEffect()); // For some reason you have to add an effect...
				fnt.loadGlyphs();
				TilelandFont tf = new TilelandFont(fnt);
				fonts.put(name + "@" + pt, tf);
				return tf;
			} 
			catch (SlickException e) {
				return getDefaultFont();
			}
		}
	}
	
	public Texture getTexture(String name) {
		return textures.get(name);
	}
	
	public boolean isTexture(String fmt) {
		fmt = fmt.toUpperCase();
		for(String valid : textureFormats)
			if(valid.equals(fmt))
				return true;
		return false;
	}
	
	public boolean isFont(String fmt) {
		fmt = fmt.toUpperCase();
		for(String valid : fontFormats)
			if(valid.equals(fmt))
				return true;
		return false;
	}
	
	public void dump() {
		for(String key : textures.keySet()) {
			System.out.println(key + " => " + textures.get(key));
		}
		
		for(String key : fonts.keySet()) {
			System.out.println(key + " => " + fonts.get(key));
		}
	}
}
