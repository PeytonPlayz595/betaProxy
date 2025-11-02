package net.minecraft.network.v17;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet41EntityEffect extends Packet {
	public int field_35116_a;
	public byte field_35114_b;
	public byte field_35115_c;
	public short field_35113_d;

	public Packet41EntityEffect() {
	}

	public void readPacketData(DataInputStream var1) throws IOException {
		this.field_35116_a = var1.readInt();
		this.field_35114_b = var1.readByte();
		this.field_35115_c = var1.readByte();
		this.field_35113_d = var1.readShort();
	}

	public void writePacketData(DataOutputStream var1) throws IOException {
		var1.writeInt(this.field_35116_a);
		var1.writeByte(this.field_35114_b);
		var1.writeByte(this.field_35115_c);
		var1.writeShort(this.field_35113_d);
	}

	public int getPacketSize() {
		return 8;
	}
}
