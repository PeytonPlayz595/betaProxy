package net.minecraft.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet2Handshake extends Packet {
	public String username;

	public Packet2Handshake() {
	}

	public Packet2Handshake(String var1) {
		this.username = var1;
	}

	public void readPacketData(DataInputStream var1) throws IOException {
		this.username = var1.readUTF();
	}

	public void writePacketData(DataOutputStream var1) throws IOException {
		var1.writeUTF(this.username);
	}

	public int getPacketSize() {
		return 4 + this.username.length() + 4;
	}
}
