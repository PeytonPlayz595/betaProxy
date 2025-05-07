package net.minecraft.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet39 extends Packet {
	public int field_6365_a;
	public int field_6364_b;

	public int getPacketSize() {
		return 8;
	}

	public void readPacketData(DataInputStream var1) throws IOException {
		this.field_6365_a = var1.readInt();
		this.field_6364_b = var1.readInt();
	}

	public void writePacketData(DataOutputStream var1) throws IOException {
		var1.writeInt(this.field_6365_a);
		var1.writeInt(this.field_6364_b);
	}

}
