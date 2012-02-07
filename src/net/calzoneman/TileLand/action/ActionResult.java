package net.calzoneman.TileLand.action;

public class ActionResult {
	
	public static final int FAILURE = -1;
	public static final int TILE_PLACE = 1;
	public static final int TILE_BREAK = 2;
	
	protected String message;
	protected int resultCode;
	protected Object result;
	
	public ActionResult(int resultCode) {
		this(resultCode, null, "");
	}
	
	public ActionResult(int resultCode, Object result) {
		this(resultCode, result, "");
	}
	
	public ActionResult(int resultCode, Object result, String message) {
		this.resultCode = resultCode;
		this.result = result;
		this.message = message;
	}
	
	public int getResultCode() {
		return this.resultCode;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	public Object getResult() {
		return this.result;
	}
}
