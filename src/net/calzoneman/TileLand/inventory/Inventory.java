package net.calzoneman.TileLand.inventory;

public class Inventory {
	protected int size;
	protected ItemStack[] slots;
	
	public Inventory(int size) {
		this.slots = new ItemStack[size];
		this.size = size;
	}
	
	public boolean addItem(Item it) {
		if(it == null)
			return false;
		// Try to add this to an existing ItemStack
		for(int i = 0; i < size; i++) {
			ItemStack current = getItemStack(i);
			if(current == null)
				continue;
			if(current.getItem().equals(it) && current.getCount() < ItemStack.MAX_STACK_SIZE) {
				current.setCount(current.getCount() + 1);
				return true;
			}
		}
		
		// Ok, try to add it to a new ItemStack
		ItemStack stack = new ItemStack(it, 1);
		for(int i = 0; i < size; i++) {
			if(getItemStack(i) == null) {
				setItemStack(i, stack);
				return true;
			}
		}
		
		// Ok then never mind
		return false;
	}
	
	public boolean removeOneItem(int slot) {
		if(getItemStack(slot) == null)
			return false;
		if(getItemStack(slot).getCount() == 1) {
			setItemStack(slot, null);
			return true;
		}
		else {
			getItemStack(slot).setCount(getItemStack(slot).getCount()-1);
			return false;
		}
	}
	
	public ItemStack addItemStack(ItemStack it) {
		if(it == null)
			return null;
		// Don't screw up the original
		it = it.clone();
		// Check for empty slots first
		for(int i = 0; i < size; i++) {
			ItemStack current = getItemStack(i);
			if(current == null) {
				setItemStack(i, it);
				return null;
			}
		}
		
		// No?  Well try filling up other slots
		for(int i = 0; i < size; i++) {
			ItemStack current = getItemStack(i);
			if(current == null)
				continue;
			if(current.getItem().equals(it.getItem())) {
				int count = ItemStack.MAX_STACK_SIZE - current.getCount();
				if(count > 0) {
					if(it.getCount() <= count) {
						current.setCount(current.getCount() + it.getCount());
						return null;
					}
					else {
						current.setCount(ItemStack.MAX_STACK_SIZE);
						it.setCount(it.getCount() - count);
					}
				}
			}
		}
		
		// Well, we filled everything up and there's still some left over
		return it;
	}
	
	public ItemStack getItemStack(int slot) {
		if(slot < 0 || slot >= size)
			return null;
		return slots[slot];
	}
	
	public boolean setItemStack(int slot, ItemStack item) {
		if(slot < 0 || slot >= size)
			return false;
		slots[slot] = item;
		return true;
	}
	
	public int getSize() {
		return this.size;
	}
}
