package net.calzoneman.TileLand.gui;

import java.util.ArrayList;
import java.util.List;


import net.calzoneman.TileLand.util.Delegate;

public class GUIListView extends GUIContainer {
	
	protected List<GUIListItem> items;
	protected int offset;
	protected int numItemsDisplayed;
	protected GUIListItem selected;

	public GUIListView(int x, int y, int width, int height) {
		super(x, y, width, height);
		selected = null;
		items = new ArrayList<GUIListItem>();
		numItemsDisplayed = height / GUIListItem.HEIGHT;
		GUIUpDownButton btnUp = new GUIUpDownButton(width - 30, 0);
		btnUp.setClickHandler(
			new Delegate<GUIContainer, Void>() {
				@Override
				public Void run(GUIContainer param) {
					((GUIListView) param).scrollUp();
					return null;
				}
			});
		btnUp.setEnabled(false);
		addChild("btnup", btnUp);
		GUIUpDownButton btnDown = new GUIUpDownButton(width - 30, 30);
		btnDown.setOrientation(GUIUpDownButton.ORIENTATION_DOWN);
		btnDown.setClickHandler(
				new Delegate<GUIContainer, Void>() {
					@Override
					public Void run(GUIContainer param) {
						((GUIListView) param).scrollDown();
						return null;
					}
				});
		btnDown.setEnabled(false);
		addChild("btndown", btnDown);
		offset = 0;
	}
	
	public void addItem(GUIListItem item) {
		int yOff = items.size() * GUIListItem.HEIGHT;
		item.setPosition(x, y + yOff);
		item.setSize(width - 30, GUIListItem.HEIGHT);
		item.setParent(this);
		items.add(item);
		if(items.size() >= numItemsDisplayed)
			((GUIUpDownButton) getChild("btndown")).setEnabled(true);
		children.put(items.size() + "", item);
	}
	
	public GUIListItem getSelected() {
		return selected;
	}
	
	@Override
	public void setPosition(int x, int y) {
		int oldx = this.x;
		int oldy = this.y;
		super.setPosition(x, y);
		for(GUIComponent comp : children.values()) {
			comp.setPosition(x + (comp.getX() - oldx), y + (comp.getY() - oldy));
		}
	}
	
	public void scrollUp() {
		if(offset == 0)
			return;
		offset--;
		((GUIUpDownButton) getChild("btndown")).setEnabled(true);
		for(int i = 0; i < items.size(); i++) {
			items.get(i).setPosition(x, y + (i - offset) * GUIListItem.HEIGHT);
		}
		if(offset == 0)
			((GUIUpDownButton) getChild("btnup")).setEnabled(false);
	}
	
	public void scrollDown() {
		if(offset < items.size() - numItemsDisplayed) {
			offset++;
			((GUIUpDownButton) getChild("btnup")).setEnabled(true);
			for(int i = 0; i < items.size(); i++) {
				items.get(i).setPosition(x, y + (i - offset) * GUIListItem.HEIGHT);
			}
		}
		if(offset == items.size() - numItemsDisplayed)
			((GUIUpDownButton) getChild("btndown")).setEnabled(false);
	}
	
	@Override
	public void render() {
		GUIListItem focus = null;
		for(int i = offset; i < offset + numItemsDisplayed; i++) {
			if(i < items.size()) {
				if(items.get(i).isFocused())
					focus = items.get(i);
				else
					items.get(i).render();
			}
		}
		if(focus != null)
			focus.render();
		getChild("btnup").render();
		getChild("btndown").render();
	}

	public void select(GUIListItem item) {
		this.selected = item;
	}
}
