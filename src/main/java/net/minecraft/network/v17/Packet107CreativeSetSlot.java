package net.minecraft.network.v17;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet107CreativeSetSlot extends Packet {
	public int field_35108_a;
	public int field_35106_b;
	public int field_35107_c;
	public int field_35105_d;

	public void readPacketData(DataInputStream var1) throws IOException {
		this.field_35108_a = var1.readShort();
		this.field_35106_b = var1.readShort();
		this.field_35107_c = var1.readShort();
		this.field_35105_d = var1.readShort();
	}

	public void writePacketData(DataOutputStream var1) throws IOException {
		var1.writeShort(this.field_35108_a);
		var1.writeShort(this.field_35106_b);
		var1.writeShort(this.field_35107_c);
		var1.writeShort(this.field_35105_d);
	}

	public int getPacketSize() {
		return 8;
	}
}
