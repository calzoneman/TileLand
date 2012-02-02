package net.calzoneman.TileLand.inventory;

public class Inventory {
	static int NUM_SLOTS = 10; // For now it consists of only the quickbar
	private Quickbar quickbar;
	private Holdable[] slots;
	private int equipped;
	
	public Inventory() {
		this.quickbar = new Quickbar();
		this.slots = new Holdable[NUM_SLOTS];
		equipped = 0;
	}
	
	public boolean addItem(Holdable h) {
		for(int i = 0; i < NUM_SLOTS; i++) {
			if(slots[i] == null) {
				slots[i] = h;
				if(i < quickbar.getNumSlots())
					quickbar.setSlotItem(i, h);
				return true;
			}
		}
		return false;
	}
	
	public Quickbar getQuickbar() {
		return this.quickbar;
	}
	
	public Holdable getEquipped() {
		return quickbar.getSlotItem(equipped);
	}
	
	public void nextSlot() {
		if(equipped < quickbar.getNumSlots() - 1) {
			equipped++;
			quickbar.setSelectedSlot(equipped);
		}
	}
	
	public void previousSlot() {
		if(equipped > 0) {
			equipped--;
			quickbar.setSelectedSlot(equipped);
		}
	}
}
