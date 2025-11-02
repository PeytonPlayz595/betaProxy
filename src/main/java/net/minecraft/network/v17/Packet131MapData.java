package net.minecraft.network.v17;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet131MapData extends Packet {
	public short itemID;
	public short uniqueID;
	public byte[] itemData;

	public Packet131MapData() {
		this.isChunkDataPacket = true;
	}

	public Packet131MapData(short var1, short var2, byte[] var3) {
		this.isChunkDataPacket = true;
		this.itemID = var1;
		this.uniqueID = var2;
		this.itemData = var3;
	}

	public void readPacketData(DataInputStream var1) throws IOException {
		this.itemID = var1.readShort();
		this.uniqueID = var1.readShort();
		this.itemData = new byte[var1.readByte() & 255];
		var1.readFully(this.itemData);
	}

	public void writePacketData(DataOutputStream var1) throws IOException {
		var1.writeShort(this.itemID);
		var1.writeShort(this.uniqueID);
		var1.writeByte(this.itemData.length);
		var1.write(this.itemData);
	}

	public int getPacketSize() {
		return 4 + this.itemData.length;
	}
}
