package net.minecraft.network.v17;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet70Bed extends Packet {
	public static final String[] bedChat = new String[] { "tile.bed.notValid", null, null, "gameMode.changed" };
	public int bedState;
	public int field_35112_c;

	public Packet70Bed() {
	}

	public Packet70Bed(int var1, int var2) {
		this.bedState = var1;
		this.field_35112_c = var2;
	}

	public void readPacketData(DataInputStream var1) throws IOException {
		this.bedState = var1.readByte();
		this.field_35112_c = var1.readByte();
	}

	public void writePacketData(DataOutputStream var1) throws IOException {
		var1.writeByte(this.bedState);
		var1.writeByte(this.field_35112_c);
	}

	public int getPacketSize() {
		return 2;
	}
}
