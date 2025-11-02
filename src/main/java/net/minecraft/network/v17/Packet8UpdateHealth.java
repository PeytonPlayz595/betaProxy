package net.minecraft.network.v17;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet8UpdateHealth extends Packet {
	public int healthMP;
	public int field_35103_b;
	public float field_35104_c;

	public Packet8UpdateHealth() {
	}

	public Packet8UpdateHealth(int var1, int var2, float var3) {
		this.healthMP = var1;
		this.field_35103_b = var2;
		this.field_35104_c = var3;
	}

	public void readPacketData(DataInputStream var1) throws IOException {
		this.healthMP = var1.readShort();
		this.field_35103_b = var1.readShort();
		this.field_35104_c = var1.readFloat();
	}

	public void writePacketData(DataOutputStream var1) throws IOException {
		var1.writeShort(this.healthMP);
		var1.writeShort(this.field_35103_b);
		var1.writeFloat(this.field_35104_c);
	}

	public int getPacketSize() {
		return 8;
	}
}
