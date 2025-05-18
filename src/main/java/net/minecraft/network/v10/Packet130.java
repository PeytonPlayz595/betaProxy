package net.minecraft.network.v10;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet130 extends Packet {
	public int xPosition;
	public int yPosition;
	public int zPosition;
	public String[] signLines;

	public Packet130() {
		this.isChunkDataPacket = true;
	}

	public Packet130(int var1, int var2, int var3, String[] var4) {
		this.isChunkDataPacket = true;
		this.xPosition = var1;
		this.yPosition = var2;
		this.zPosition = var3;
		this.signLines = var4;
	}

	public void readPacketData(DataInputStream var1) throws IOException {
		this.xPosition = var1.readInt();
		this.yPosition = var1.readShort();
		this.zPosition = var1.readInt();
		this.signLines = new String[4];

		for(int var2 = 0; var2 < 4; ++var2) {
			this.signLines[var2] = var1.readUTF();
		}

	}

	public void writePacketData(DataOutputStream var1) throws IOException {
		var1.writeInt(this.xPosition);
		var1.writeShort(this.yPosition);
		var1.writeInt(this.zPosition);

		for(int var2 = 0; var2 < 4; ++var2) {
			var1.writeUTF(this.signLines[var2]);
		}

	}

	public int getPacketSize() {
		int var1 = 0;

		for(int var2 = 0; var2 < 4; ++var2) {
			var1 += this.signLines[var2].length();
		}

		return var1;
	}
}
