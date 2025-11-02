package net.minecraft.network.v17;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet19EntityAction extends Packet {
	public int entityId;
	public int state;

	public void readPacketData(DataInputStream var1) throws IOException {
		this.entityId = var1.readInt();
		this.state = var1.readByte();
	}

	public void writePacketData(DataOutputStream var1) throws IOException {
		var1.writeInt(this.entityId);
		var1.writeByte(this.state);
	}

	public int getPacketSize() {
		return 5;
	}
}
