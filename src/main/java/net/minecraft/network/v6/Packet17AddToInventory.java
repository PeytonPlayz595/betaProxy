package net.minecraft.network.v6;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet17AddToInventory extends Packet {
	public int id;
	public int count;
	public int durability;

	public void readPacketData(DataInputStream var1) throws IOException {
		this.id = var1.readShort();
		this.count = var1.readByte();
		this.durability = var1.readShort();
	}

	public void writePacketData(DataOutputStream var1) throws IOException {
		var1.writeShort(this.id);
		var1.writeByte(this.count);
		var1.writeShort(this.durability);
	}

	public int getPacketSize() {
		return 5;
	}
}
