package net.calzoneman.TileLand.level;

import java.util.Random;

public abstract class LevelGenerator {
	protected Random rand;
	
	public LevelGenerator() {
		rand = new Random();
	}
	
	public Level generate(int width, int height) {
		return generate(width, height, rand.nextInt());
	}
	
	public abstract Level generate(int width, int height, int seed);
}
