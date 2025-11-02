package net.minecraft.network.v17;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet0KeepAlive extends Packet {
	public int field_35126_a;

	public Packet0KeepAlive() {
	}

	public Packet0KeepAlive(int var1) {
		this.field_35126_a = var1;
	}

	public void readPacketData(DataInputStream var1) throws IOException {
		this.field_35126_a = var1.readInt();
	}

	public void writePacketData(DataOutputStream var1) throws IOException {
		var1.writeInt(this.field_35126_a);
	}

	public int getPacketSize() {
		return 4;
	}
}
