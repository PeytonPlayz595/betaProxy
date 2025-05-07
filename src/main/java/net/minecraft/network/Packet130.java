package net.minecraft.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet130 extends Packet {
	public int field_20020_a;
	public int field_20019_b;
	public int field_20022_c;
	public String[] field_20021_d;

	public Packet130() {
	}

	public void readPacketData(DataInputStream var1) throws IOException {
		this.field_20020_a = var1.readInt();
		this.field_20019_b = var1.readShort();
		this.field_20022_c = var1.readInt();
		this.field_20021_d = new String[4];

		for(int var2 = 0; var2 < 4; ++var2) {
			this.field_20021_d[var2] = var1.readUTF();
		}

	}

	public void writePacketData(DataOutputStream var1) throws IOException {
		var1.writeInt(this.field_20020_a);
		var1.writeShort(this.field_20019_b);
		var1.writeInt(this.field_20022_c);

		for(int var2 = 0; var2 < 4; ++var2) {
			var1.writeUTF(this.field_20021_d[var2]);
		}

	}

	public int getPacketSize() {
		int var1 = 0;

		for(int var2 = 0; var2 < 4; ++var2) {
			var1 += this.field_20021_d[var2].length();
		}

		return var1;
	}
}
