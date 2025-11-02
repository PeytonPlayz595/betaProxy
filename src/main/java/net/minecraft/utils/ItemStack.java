package net.minecraft.utils;

public class ItemStack {
	
	public int stackSize;
	public int itemID;
	public int itemDamage;
	
	public ItemStack(int var1, int var2, int var3) {
		this.itemID = var1;
		this.stackSize = var2;
		this.itemDamage = var3;
	}

	public int getItemDamage() {
		return this.itemDamage;
	}

	public ItemStack copy() {
		return new ItemStack(this.itemID, this.stackSize, this.itemDamage);
	}

}
