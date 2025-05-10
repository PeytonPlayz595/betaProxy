package net.minecraft.network.v8;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet34EntityTeleport extends Packet {
	public int entityId;
	public int xPosition;
	public int yPosition;
	public int zPosition;
	public byte yaw;
	public byte pitch;

	public Packet34EntityTeleport() {
	}

	public void readPacketData(DataInputStream var1) throws IOException {
		this.entityId = var1.readInt();
		this.xPosition = var1.readInt();
		this.yPosition = var1.readInt();
		this.zPosition = var1.readInt();
		this.yaw = (byte)var1.read();
		this.pitch = (byte)var1.read();
	}

	public void writePacketData(DataOutputStream var1) throws IOException {
		var1.writeInt(this.entityId);
		var1.writeInt(this.xPosition);
		var1.writeInt(this.yPosition);
		var1.writeInt(this.zPosition);
		var1.write(this.yaw);
		var1.write(this.pitch);
	}

	public int getPacketSize() {
		return 34;
	}
}
