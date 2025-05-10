package net.minecraft.network.v8;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet105 extends Packet {
	public int field_20032_a;
	public int field_20031_b;
	public int field_20033_c;

	public void readPacketData(DataInputStream var1) throws IOException {
		this.field_20032_a = var1.readByte();
		this.field_20031_b = var1.readShort();
		this.field_20033_c = var1.readShort();
	}

	public void writePacketData(DataOutputStream var1) throws IOException {
		var1.writeByte(this.field_20032_a);
		var1.writeShort(this.field_20031_b);
		var1.writeShort(this.field_20033_c);
	}

	public int getPacketSize() {
		return 5;
	}
}
