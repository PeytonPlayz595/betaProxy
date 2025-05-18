package net.minecraft.network.v10;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet255KickDisconnect extends Packet {
	public String reason;

	public Packet255KickDisconnect() {
	}

	public Packet255KickDisconnect(String var1) {
		this.reason = var1;
	}

	public void readPacketData(DataInputStream var1) throws IOException {
		this.reason = var1.readUTF();
	}

	public void writePacketData(DataOutputStream var1) throws IOException {
		var1.writeUTF(this.reason);
	}

	public int getPacketSize() {
		return this.reason.length();
	}
}
