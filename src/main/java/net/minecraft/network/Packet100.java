package net.minecraft.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet100 extends Packet {
	public int field_20038_a;
	public int field_20037_b;
	public String field_20040_c;
	public int field_20039_d;

	public void readPacketData(DataInputStream var1) throws IOException {
		this.field_20038_a = var1.readByte();
		this.field_20037_b = var1.readByte();
		this.field_20040_c = var1.readUTF();
		this.field_20039_d = var1.readByte();
	}

	public void writePacketData(DataOutputStream var1) throws IOException {
		var1.writeByte(this.field_20038_a);
		var1.writeByte(this.field_20037_b);
		var1.writeUTF(this.field_20040_c);
		var1.writeByte(this.field_20039_d);
	}

	public int getPacketSize() {
		return 3 + this.field_20040_c.length();
	}
}
