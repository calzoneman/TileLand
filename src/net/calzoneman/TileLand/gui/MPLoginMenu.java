package net.calzoneman.TileLand.gui;

import net.calzoneman.TileLand.TileLand;
import net.calzoneman.TileLand.gfx.TilelandFont;
import net.calzoneman.TileLand.util.Delegate;

import org.lwjgl.opengl.Display;

public class MPLoginMenu extends GUIMenu {
	
	private GUIContainer panel;
	private String player = "Player";
	private String password = "";
	private String server = "localhost";
	private int port = 1337;
	
	public MPLoginMenu() {
		super();
		init(0, 0, Display.getWidth(), Display.getHeight());
	}
	
	public void init(int x, int y, int width, int height) {
		panel = new GUIContainer(Display.getWidth() / 2 - 320, Display.getHeight() / 2 - 240, 640, 480);
		
		int x1 = 175;
		int x2 = x1 + 90;
		int curY = 80;
		int charHeight = TileLand.getResourceManager().getPreferredFont().getHeight("|");
		
		GUITextbox unameTxt = new GUITextbox(x2, curY, 200);
		unameTxt.setMaxLength(32);
		panel.addChild("unametxt", unameTxt);
		GUILabel unameLabel = new GUILabel(x1, curY + unameTxt.getHeight() / 2 - charHeight / 2, "Username: ");
		panel.addChild("unamelbl", unameLabel);
		curY += unameTxt.getHeight() + 10;
		
		GUITextbox passTxt = new GUITextbox(x2, curY, 200);
		panel.addChild("passtxt", passTxt);
		GUILabel passLabel = new GUILabel(x1, curY + passTxt.getHeight() / 2 - charHeight / 2, "Password: ");
		panel.addChild("passlbl", passLabel);
		curY += passTxt.getHeight() + 10;
		
		GUIButton cancelBtn = new GUIButton(x1, curY, 145, "Go Back");
		cancelBtn.setClickHandler(
				new Delegate<GUIContainer, Void>() {
					@Override
					public Void run(GUIContainer param) {
						MenuManager.getMenuManager().goBack();
						return null;
					}
				});
		panel.addChild("cancelBtn", cancelBtn);
		
		GUIButton loginBtn = new GUIButton(x1 + 145, curY, 145, "Login");
		loginBtn.setClickHandler(
				new Delegate<GUIContainer, Void>() {
					@Override
					public Void run(GUIContainer param) {
						((MPLoginMenu) param.getParent()).tryLogin();
						return null;
					}
				});
		panel.addChild("loginBtn", loginBtn);
		curY += loginBtn.getHeight() + 10;
		
		GUILabel error = new GUILabel(x1, curY, "");
		panel.addChild("error", error);
		
		addChild("panel", panel);
	}
	
	public void reInit(int x, int y, int width, int height) {
		String uname = ((GUITextbox) panel.getChild("unametxt")).getTextOrDefault();
		String pass = ((GUITextbox) panel.getChild("passtxt")).getTextOrDefault();
		
		children.clear();
		init(x, y, width, height);
		((GUITextbox) panel.getChild("unametxt")).setText(uname);
		((GUITextbox) panel.getChild("unametxt")).setText(pass);
		
	}
	
	public String getPlayer() {
		return player;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getServer() {
		return server;
	}
	
	public int getPort() {
		return port;
	}
	
	public void tryLogin() {
		for(GUIComponent comp : children.values())
			comp.blur();
		MenuManager.getMenuManager().openMenu("multiplayergame");
	}
}
