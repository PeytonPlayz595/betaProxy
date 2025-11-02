package net.minecraft.network.v17;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet43Experience extends Packet {
	public int field_35136_a;
	public int field_35134_b;
	public int field_35135_c;

	public Packet43Experience() {
	}

	public Packet43Experience(int var1, int var2, int var3) {
		this.field_35136_a = var1;
		this.field_35134_b = var2;
		this.field_35135_c = var3;
	}

	public void readPacketData(DataInputStream var1) throws IOException {
		this.field_35136_a = var1.readByte();
		this.field_35135_c = var1.readByte();
		this.field_35134_b = var1.readShort();
	}

	public void writePacketData(DataOutputStream var1) throws IOException {
		var1.writeByte(this.field_35136_a);
		var1.writeByte(this.field_35135_c);
		var1.writeShort(this.field_35134_b);
	}

	public int getPacketSize() {
		return 4;
	}
}
