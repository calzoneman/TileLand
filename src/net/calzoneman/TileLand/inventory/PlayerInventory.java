package net.calzoneman.TileLand.inventory;

public class PlayerInventory extends Inventory {
	public static final int PLAYER_INVENTORY_COUNT = 40;
	private Quickbar quickbar;
	
	public PlayerInventory() {
		super(PLAYER_INVENTORY_COUNT);
		this.quickbar = new Quickbar();
	}

	public PlayerInventory(int size) {
		super(size);
	}
	
	@Override
	public ItemStack getItemStack(int slot) {
		if(slot < 0 || slot >= size)
			return null;
		else if(slot < quickbar.getCount())
			return quickbar.getItemStack(slot);
		else
			return slots[slot];
	}
	
	@Override
	public boolean setItemStack(int slot, ItemStack item) {
		if(slot >= 0 && slot < quickbar.getCount())
			return quickbar.setItemStack(slot, item);
		return super.setItemStack(slot, item);
	}
	
	public Quickbar getQuickbar() {
		return quickbar;
	}

}
