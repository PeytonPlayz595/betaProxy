package net.minecraft.network.v9;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet22Collect extends Packet {
	public int collectedEntityId;
	public int collectorEntityId;

	public void readPacketData(DataInputStream var1) throws IOException {
		this.collectedEntityId = var1.readInt();
		this.collectorEntityId = var1.readInt();
	}

	public void writePacketData(DataOutputStream var1) throws IOException {
		var1.writeInt(this.collectedEntityId);
		var1.writeInt(this.collectorEntityId);
	}

	public int getPacketSize() {
		return 8;
	}
}
