package net.minecraft.network.v17;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet26EntityExpOrb extends Packet {
	public int field_35125_a;
	public int field_35123_b;
	public int field_35124_c;
	public int field_35121_d;
	public int field_35122_e;

	public Packet26EntityExpOrb() {
	}

	public void readPacketData(DataInputStream var1) throws IOException {
		this.field_35125_a = var1.readInt();
		this.field_35123_b = var1.readInt();
		this.field_35124_c = var1.readInt();
		this.field_35121_d = var1.readInt();
		this.field_35122_e = var1.readShort();
	}

	public void writePacketData(DataOutputStream var1) throws IOException {
		var1.writeInt(this.field_35125_a);
		var1.writeInt(this.field_35123_b);
		var1.writeInt(this.field_35124_c);
		var1.writeInt(this.field_35121_d);
		var1.writeShort(this.field_35122_e);
	}

	public int getPacketSize() {
		return 18;
	}
}
