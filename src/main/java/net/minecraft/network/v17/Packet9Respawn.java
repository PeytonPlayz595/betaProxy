package net.minecraft.network.v17;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet9Respawn extends Packet {
	public long field_35120_a;
	public int respawnDimension;
	public int field_35119_c;
	public int field_35117_d;
	public int field_35118_e;

	public Packet9Respawn() {
	}

	public Packet9Respawn(byte var1, byte var2, long var3, int var5, int var6) {
		this.respawnDimension = var1;
		this.field_35119_c = var2;
		this.field_35120_a = var3;
		this.field_35117_d = var5;
		this.field_35118_e = var6;
	}

	public void readPacketData(DataInputStream var1) throws IOException {
		this.respawnDimension = var1.readByte();
		this.field_35119_c = var1.readByte();
		this.field_35118_e = var1.readByte();
		this.field_35117_d = var1.readShort();
		this.field_35120_a = var1.readLong();
	}

	public void writePacketData(DataOutputStream var1) throws IOException {
		var1.writeByte(this.respawnDimension);
		var1.writeByte(this.field_35119_c);
		var1.writeByte(this.field_35118_e);
		var1.writeShort(this.field_35117_d);
		var1.writeLong(this.field_35120_a);
	}

	public int getPacketSize() {
		return 13;
	}
}
