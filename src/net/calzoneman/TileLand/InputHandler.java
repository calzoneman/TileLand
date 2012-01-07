package net.calzoneman.TileLand;

import java.awt.Point;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class InputHandler implements MouseListener, MouseMotionListener, KeyListener, FocusListener {
	
	private enum ButtonState {
		PRESSED,
		ONCE,
		RELEASED
	}
	
	private boolean focused = false;
	
	// Mouse
	private final int NUM_BUTTONS = 3;
	private boolean[] mState;
	private ButtonState[] mPollState;
	private Point currentPos;
	private Point pollPos;
	private Point pollDelta;
	
	// Keyboard
	private final int NUM_KEYS = 256;
	private boolean[] kState;
	private ButtonState[] kPollState;
	
	public InputHandler() {
		// Mouse
		this.mState = new boolean[NUM_BUTTONS];
		this.mPollState = new ButtonState[NUM_BUTTONS];
		this.currentPos = new Point(0, 0);
		this.pollPos = new Point(0, 0);
		this.pollDelta = new Point(0, 0);
		
		// Keyboard
		this.kState = new boolean[NUM_KEYS];
		this.kPollState = new ButtonState[NUM_KEYS];
		
		// Initialize states
		for(int i = 0; i < NUM_BUTTONS; i++) {
			mState[i] = false;
			mPollState[i] = ButtonState.RELEASED;
		}
		
		for(int i = 0; i < NUM_KEYS; i++) {
			kState[i] = false;
			kPollState[i] = ButtonState.RELEASED;
		}
	}
	
	public Point getMousePosition() {
		return new Point(this.pollPos);
	}
	
	public Point getMouseDelta() {
		return new Point(this.pollDelta);
	}
	
	public boolean mouseButtonDown(int button) {
		return this.mPollState[button - 1] == ButtonState.PRESSED || this.mPollState[button - 1] == ButtonState.ONCE;
	}
	
	public boolean mouseButtonDownOnce(int button) {
		return this.mPollState[button - 1] == ButtonState.ONCE;
	}
	
	public synchronized void poll() {
		if(!focused) return;
		// Mouse
		this.pollDelta = new Point(currentPos.x - pollPos.x, currentPos.y - pollPos.y);
		this.pollPos = new Point(currentPos);
		
		for(int i = 0; i < NUM_BUTTONS; i++) {
			if(mState[i]) {
				if(mPollState[i] == ButtonState.RELEASED){
					mPollState[i] = ButtonState.ONCE;
				}
				else {
					mPollState[i] = ButtonState.PRESSED;
				}
			}
			else {
				mPollState[i] = ButtonState.RELEASED;
			}
		}
		
		// Keyboard
		for(int i = 0; i < NUM_KEYS; i++) {
			if(kState[i]) {
				if(kPollState[i] == ButtonState.RELEASED) {
					kPollState[i] = ButtonState.ONCE;
				}
				else {
					kPollState[i] = ButtonState.PRESSED;
				}
			}
			else {
				kPollState[i] = ButtonState.RELEASED;
			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent event) {
		this.currentPos = event.getPoint();
	}

	@Override
	public void mouseMoved(MouseEvent event) {
		this.currentPos = event.getPoint();

	}

	@Override
	public void mouseClicked(MouseEvent event) {
		this.currentPos = event.getPoint();
	}

	@Override
	public void mouseEntered(MouseEvent event) {
		this.currentPos = event.getPoint();

	}

	@Override
	public void mouseExited(MouseEvent event) {
		this.currentPos = event.getPoint();

	}

	@Override
	public void mousePressed(MouseEvent event) {
		this.currentPos = event.getPoint();
		this.mState[event.getButton() - 1] = true;

	}

	@Override
	public void mouseReleased(MouseEvent event) {
		this.currentPos = event.getPoint();
		this.mState[event.getButton() - 1] = false;

	}
	
	public boolean keyDown(int keyCode) {
		if(keyCode >= 0 && keyCode < NUM_KEYS) {
			return this.kPollState[keyCode] == ButtonState.PRESSED || this.kPollState[keyCode] == ButtonState.ONCE;
		}
		return false;
	}
	
	public boolean keyDownOnce(int keyCode) {
		if(keyCode >= 0 && keyCode < NUM_KEYS) {
			return this.kPollState[keyCode] == ButtonState.ONCE;
		}
		return false;
	}

	@Override
	public void keyPressed(KeyEvent event) {
		int keyCode = event.getKeyCode();
		//System.out.println(keyCode);
		if(keyCode >= 0 && keyCode < NUM_KEYS) {
			this.kState[keyCode] = true;
		}
		
	}

	@Override
	public void keyReleased(KeyEvent event) {
		int keyCode = event.getKeyCode();
		if(keyCode >= 0 && keyCode < NUM_KEYS) {
			this.kState[keyCode] = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent event) {
		int keyCode = event.getKeyCode();
		if(keyCode >= 0 && keyCode < NUM_KEYS) {
			this.kState[keyCode] = true;
		}
	}

	@Override
	public void focusGained(FocusEvent e) {
		this.focused = true;
	}

	@Override
	public void focusLost(FocusEvent e) {
		this.focused = false;
	}

	public boolean isFocused() {
		return focused;
	}

	public void setFocused(boolean focused) {
		this.focused = focused;
	}
	
	

}
