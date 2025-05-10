package net.minecraft.network.v6;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet59ComplexEntity extends Packet {
	public int xPosition;
	public int yPosition;
	public int zPosition;
	public byte[] entityData;

	public Packet59ComplexEntity() {
		this.isChunkDataPacket = true;
	}

	public void readPacketData(DataInputStream var1) throws IOException {
		this.xPosition = var1.readInt();
		this.yPosition = var1.readShort();
		this.zPosition = var1.readInt();
		int var2 = var1.readShort() & '\uffff';
		this.entityData = new byte[var2];
		var1.readFully(this.entityData);
	}

	public void writePacketData(DataOutputStream var1) throws IOException {
		var1.writeInt(this.xPosition);
		var1.writeShort(this.yPosition);
		var1.writeInt(this.zPosition);
		var1.writeShort((short)this.entityData.length);
		var1.write(this.entityData);
	}

	public int getPacketSize() {
		return this.entityData.length + 2 + 10;
	}
}
