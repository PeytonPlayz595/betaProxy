package net.minecraft.network.v6;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet18ArmAnimation extends Packet {
	public int entityId;
	public int animate;

	public Packet18ArmAnimation() {
	}

	public void readPacketData(DataInputStream var1) throws IOException {
		this.entityId = var1.readInt();
		this.animate = var1.readByte();
	}

	public void writePacketData(DataOutputStream var1) throws IOException {
		var1.writeInt(this.entityId);
		var1.writeByte(this.animate);
	}

	public int getPacketSize() {
		return 5;
	}
}
