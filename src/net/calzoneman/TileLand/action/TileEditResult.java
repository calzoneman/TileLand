package net.calzoneman.TileLand.action;

public class TileEditResult extends ActionResult {
	
	private int id;
	private int layer;
	private int data;
	
	public TileEditResult(int resultCode) {
		super(resultCode);
	}

	public TileEditResult(int resultCode, int id, int data, int layer) {
		super(resultCode);
		this.id = id;
		this.layer = layer;
	}
	
	public int getId() {
		return id;
	}
	
	public int getData() {
		return data;
	}
	
	public int getLayer() {
		return layer;
	}

}
