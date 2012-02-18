package net.calzoneman.TileLand.util;

public class KeyValuePair<T, U> {
	private T key;
	private U value;
	
	public KeyValuePair(T key, U value) {
		this.key = key;
		this.value = value;
	}
	
	public T key() {
		return key;
	}
	
	public U value() {
		return value;
	}
}
