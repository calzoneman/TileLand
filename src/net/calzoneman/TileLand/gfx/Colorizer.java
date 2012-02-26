package net.calzoneman.TileLand.gfx;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Colorizer {
	public static int[] colorize(int[] pixels, int[] colors) {
		// Build a list of colors in the source image
		List<Integer> origColors = new ArrayList<Integer>();
		for(int i = 0; i < pixels.length; i++) {
			if(!origColors.contains(pixels[i]))
				origColors.add(pixels[i]);
		}
		// Sort the colors from darkest to brightest
		Collections.sort(origColors);
		// Map each source color to a destination color
		Map<Integer, Integer> mapping = new HashMap<Integer, Integer>();
		for(int i = 0; i < origColors.size() && i < colors.length; i++) {
			mapping.put(origColors.get(i), 0xFF000000 | colors[i]);
		}
		
		// Change colors
		for(int i = 0; i < pixels.length; i++) {
			pixels[i] = mapping.get(pixels[i]);
		}
		return pixels;
	}
}
