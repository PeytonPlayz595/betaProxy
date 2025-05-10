package net.minecraft.network.v6;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet20NamedEntitySpawn extends Packet {
	public int entityId;
	public String name;
	public int xPosition;
	public int yPosition;
	public int zPosition;
	public byte rotation;
	public byte pitch;
	public int currentItem;

	public Packet20NamedEntitySpawn() {
	}

	public void readPacketData(DataInputStream var1) throws IOException {
		this.entityId = var1.readInt();
		this.name = var1.readUTF();
		this.xPosition = var1.readInt();
		this.yPosition = var1.readInt();
		this.zPosition = var1.readInt();
		this.rotation = var1.readByte();
		this.pitch = var1.readByte();
		this.currentItem = var1.readShort();
	}

	public void writePacketData(DataOutputStream var1) throws IOException {
		var1.writeInt(this.entityId);
		var1.writeUTF(this.name);
		var1.writeInt(this.xPosition);
		var1.writeInt(this.yPosition);
		var1.writeInt(this.zPosition);
		var1.writeByte(this.rotation);
		var1.writeByte(this.pitch);
		var1.writeShort(this.currentItem);
	}

	public int getPacketSize() {
		return 28;
	}
}
