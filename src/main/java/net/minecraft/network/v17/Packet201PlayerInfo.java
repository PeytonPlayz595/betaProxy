package net.minecraft.network.v17;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet201PlayerInfo extends Packet {
	public String field_35111_a;
	public boolean field_35109_b;
	public int field_35110_c;

	public Packet201PlayerInfo() {
	}

	public Packet201PlayerInfo(String var1, boolean var2, int var3) {
		this.field_35111_a = var1;
		this.field_35109_b = var2;
		this.field_35110_c = var3;
	}

	public void readPacketData(DataInputStream var1) throws IOException {
		this.field_35111_a = var1.readUTF();
		this.field_35109_b = var1.readByte() != 0;
		this.field_35110_c = var1.readShort();
	}

	public void writePacketData(DataOutputStream var1) throws IOException {
		var1.writeUTF(this.field_35111_a);
		var1.writeByte(this.field_35109_b ? 1 : 0);
		var1.writeShort(this.field_35110_c);
	}

	public int getPacketSize() {
		return this.field_35111_a.length() + 2 + 1 + 2;
	}
}
