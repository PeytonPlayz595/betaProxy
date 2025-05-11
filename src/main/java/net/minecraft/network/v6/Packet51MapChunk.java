package net.minecraft.network.v6;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

public class Packet51MapChunk extends Packet {
	public int xPosition;
	public int yPosition;
	public int zPosition;
	public int xSize;
	public int ySize;
	public int zSize;
	public byte[] chunk;
	private int chunkSize;

	public Packet51MapChunk() {
		this.isChunkDataPacket = true;
	}

	public void readPacketData(DataInputStream var1) throws IOException {
		this.xPosition = var1.readInt();
		this.yPosition = var1.readShort();
		this.zPosition = var1.readInt();
		this.xSize = var1.read() + 1;
		this.ySize = var1.read() + 1;
		this.zSize = var1.read() + 1;
		this.chunkSize = var1.readInt();
		byte[] var3 = new byte[this.chunkSize];
		var1.readFully(var3);
		this.chunk = var3;
	}

	public void writePacketData(DataOutputStream var1) throws IOException {
		var1.writeInt(this.xPosition);
		var1.writeShort(this.yPosition);
		var1.writeInt(this.zPosition);
		var1.write(this.xSize - 1);
		var1.write(this.ySize - 1);
		var1.write(this.zSize - 1);
		var1.writeInt(this.chunkSize);
		var1.write(this.chunk);
	}

	public int getPacketSize() {
		return 17 + this.chunkSize;
	}
}
