package net.minecraft.network.v8;

public class ItemStack {
	
	public int stackSize;
	public int itemID;
	public int itemDamage;
	
	public ItemStack(int var1, int var2, int var3) {
		this.stackSize = 0;
		this.itemID = var1;
		this.stackSize = var2;
		this.itemDamage = var3;
	}

}
