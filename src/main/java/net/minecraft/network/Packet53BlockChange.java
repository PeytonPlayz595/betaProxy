package net.minecraft.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet53BlockChange extends Packet {
	public int xPosition;
	public int yPosition;
	public int zPosition;
	public int type;
	public int metadata;

	public Packet53BlockChange() {
	}

	public void readPacketData(DataInputStream var1) throws IOException {
		this.xPosition = var1.readInt();
		this.yPosition = var1.read();
		this.zPosition = var1.readInt();
		this.type = var1.read();
		this.metadata = var1.read();
	}

	public void writePacketData(DataOutputStream var1) throws IOException {
		var1.writeInt(this.xPosition);
		var1.write(this.yPosition);
		var1.writeInt(this.zPosition);
		var1.write(this.type);
		var1.write(this.metadata);
	}

	public int getPacketSize() {
		return 11;
	}
}
