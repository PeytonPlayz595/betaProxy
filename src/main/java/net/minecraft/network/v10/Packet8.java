package net.minecraft.network.v10;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet8 extends Packet {
	public int healthMP;

	public void readPacketData(DataInputStream var1) throws IOException {
		this.healthMP = var1.readShort();
	}

	public void writePacketData(DataOutputStream var1) throws IOException {
		var1.writeShort(this.healthMP);
	}

	public int getPacketSize() {
		return 2;
	}
}
