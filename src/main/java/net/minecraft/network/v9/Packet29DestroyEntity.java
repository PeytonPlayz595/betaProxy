package net.minecraft.network.v9;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet29DestroyEntity extends Packet {
	public int entityId;

	public void readPacketData(DataInputStream var1) throws IOException {
		this.entityId = var1.readInt();
	}

	public void writePacketData(DataOutputStream var1) throws IOException {
		var1.writeInt(this.entityId);
	}

	public int getPacketSize() {
		return 4;
	}
}
