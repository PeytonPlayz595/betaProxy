package net.minecraft.network.v6;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet8 extends Packet {
	public int healthMP;

	public void readPacketData(DataInputStream var1) throws IOException {
		this.healthMP = var1.readByte();
	}

	public void writePacketData(DataOutputStream var1) throws IOException {
		var1.writeByte(this.healthMP);
	}

	public int getPacketSize() {
		return 1;
	}
}
