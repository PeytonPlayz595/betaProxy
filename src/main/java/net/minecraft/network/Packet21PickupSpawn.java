package net.minecraft.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet21PickupSpawn extends Packet {
	public int entityId;
	public int xPosition;
	public int yPosition;
	public int zPosition;
	public byte rotation;
	public byte pitch;
	public byte roll;
	public int itemId;
	public int count;

	public Packet21PickupSpawn() {
	}

	public void readPacketData(DataInputStream var1) throws IOException {
		this.entityId = var1.readInt();
		this.itemId = var1.readShort();
		this.count = var1.readByte();
		this.xPosition = var1.readInt();
		this.yPosition = var1.readInt();
		this.zPosition = var1.readInt();
		this.rotation = var1.readByte();
		this.pitch = var1.readByte();
		this.roll = var1.readByte();
	}

	public void writePacketData(DataOutputStream var1) throws IOException {
		var1.writeInt(this.entityId);
		var1.writeShort(this.itemId);
		var1.writeByte(this.count);
		var1.writeInt(this.xPosition);
		var1.writeInt(this.yPosition);
		var1.writeInt(this.zPosition);
		var1.writeByte(this.rotation);
		var1.writeByte(this.pitch);
		var1.writeByte(this.roll);
	}

	public int getPacketSize() {
		return 22;
	}
}
