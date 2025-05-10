package net.minecraft.network.v8;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet5PlayerInventory extends Packet {
	public int type;
	public int stacks;
	public int field_20044_c;

	public void readPacketData(DataInputStream var1) throws IOException {
		this.type = var1.readInt();
		this.stacks = var1.readShort();
		this.field_20044_c = var1.readShort();
	}

	public void writePacketData(DataOutputStream var1) throws IOException {
		var1.writeInt(this.type);
		var1.writeShort(this.stacks);
		var1.writeShort(this.field_20044_c);
	}

	public int getPacketSize() {
		return 8;
	}
}
