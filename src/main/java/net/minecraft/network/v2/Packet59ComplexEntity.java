package net.minecraft.network.v2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet59ComplexEntity extends Packet {
	public int xCoord;
	public int yCoord;
	public int zCoord;
	public byte[] compressedNBT;

	public Packet59ComplexEntity() {
		this.isChunkDataPacket = true;
	}

	public void readPacketData(DataInputStream var1) throws IOException {
		this.xCoord = var1.readInt();
		this.yCoord = var1.readShort();
		this.zCoord = var1.readInt();
		int var2 = var1.readShort() & '\uffff';
		this.compressedNBT = new byte[var2];
		var1.readFully(this.compressedNBT);
	}

	public void writePacket(DataOutputStream var1) throws IOException {
		var1.writeInt(this.xCoord);
		var1.writeShort(this.yCoord);
		var1.writeInt(this.zCoord);
		var1.writeShort((short)this.compressedNBT.length);
		var1.write(this.compressedNBT);
	}

	public int getPacketSize() {
		return this.compressedNBT.length + 2 + 10;
	}
}
