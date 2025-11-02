package net.minecraft.network.v17;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet3Chat extends Packet {
	public String message;

	public Packet3Chat() {
	}

	public Packet3Chat(String var1) {
		if (var1.length() > 119) {
			var1 = var1.substring(0, 119);
		}

		this.message = var1;
	}

	public void readPacketData(DataInputStream var1) throws IOException {
		this.message = var1.readUTF();
	}

	public void writePacketData(DataOutputStream var1) throws IOException {
		var1.writeUTF(this.message);
	}

	public int getPacketSize() {
		return this.message.length();
	}
}
