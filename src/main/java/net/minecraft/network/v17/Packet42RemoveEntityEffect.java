package net.minecraft.network.v17;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet42RemoveEntityEffect extends Packet {
	public int field_35128_a;
	public byte field_35127_b;

	public Packet42RemoveEntityEffect() {
	}

	public void readPacketData(DataInputStream var1) throws IOException {
		this.field_35128_a = var1.readInt();
		this.field_35127_b = var1.readByte();
	}

	public void writePacketData(DataOutputStream var1) throws IOException {
		var1.writeInt(this.field_35128_a);
		var1.writeByte(this.field_35127_b);
	}

	public int getPacketSize() {
		return 5;
	}
}
