package net.minecraft.network.v2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet17AddToInventory extends Packet {
	public int itemID;
	public int count;
	public int itemDamage;

	public void readPacketData(DataInputStream var1) throws IOException {
		this.itemID = var1.readShort();
		this.count = var1.readByte();
		this.itemDamage = var1.readShort();
	}

	public void writePacket(DataOutputStream var1) throws IOException {
		var1.writeShort(this.itemID);
		var1.writeByte(this.count);
		var1.writeShort(this.itemDamage);
	}

	public int getPacketSize() {
		return 5;
	}
}
