package net.minecraft.network.v9;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.utils.ItemStack;

public class Packet102 extends Packet {
	public int window_Id;
	public int inventorySlot;
	public int mouseClick;
	public short action;
	public ItemStack itemStack;

	public Packet102() {
	}

	public Packet102(int var1, int var2, int var3, ItemStack var4, short var5) {
		this.window_Id = var1;
		this.inventorySlot = var2;
		this.mouseClick = var3;
		this.itemStack = var4;
		this.action = var5;
	}

	public void readPacketData(DataInputStream var1) throws IOException {
		this.window_Id = var1.readByte();
		this.inventorySlot = var1.readShort();
		this.mouseClick = var1.readByte();
		this.action = var1.readShort();
		short var2 = var1.readShort();
		if(var2 >= 0) {
			byte var3 = var1.readByte();
			short var4 = var1.readShort();
			this.itemStack = new ItemStack(var2, var3, var4);
		} else {
			this.itemStack = null;
		}

	}

	public void writePacketData(DataOutputStream var1) throws IOException {
		var1.writeByte(this.window_Id);
		var1.writeShort(this.inventorySlot);
		var1.writeByte(this.mouseClick);
		var1.writeShort(this.action);
		if(this.itemStack == null) {
			var1.writeShort(-1);
		} else {
			var1.writeShort(this.itemStack.itemID);
			var1.writeByte(this.itemStack.stackSize);
			var1.writeShort(this.itemStack.itemDamage);
		}

	}

	public int getPacketSize() {
		return 11;
	}
}
