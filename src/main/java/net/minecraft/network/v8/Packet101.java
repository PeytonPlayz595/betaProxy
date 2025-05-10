package net.minecraft.network.v8;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet101 extends Packet {
	public int field_20034_a;

	public Packet101() {
	}

	public Packet101(int var1) {
		this.field_20034_a = var1;
	}

	public void readPacketData(DataInputStream var1) throws IOException {
		this.field_20034_a = var1.readByte();
	}

	public void writePacketData(DataOutputStream var1) throws IOException {
		var1.writeByte(this.field_20034_a);
	}

	public int getPacketSize() {
		return 1;
	}
}
