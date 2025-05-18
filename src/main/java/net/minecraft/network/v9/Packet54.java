package net.minecraft.network.v9;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet54 extends Packet {
	public int xLocation;
	public int yLocation;
	public int zLocation;
	public int instrumentType;
	public int pitch;

	public void readPacketData(DataInputStream var1) throws IOException {
		this.xLocation = var1.readInt();
		this.yLocation = var1.readShort();
		this.zLocation = var1.readInt();
		this.instrumentType = var1.read();
		this.pitch = var1.read();
	}

	public void writePacketData(DataOutputStream var1) throws IOException {
		var1.writeInt(this.xLocation);
		var1.writeShort(this.yLocation);
		var1.writeInt(this.zLocation);
		var1.write(this.instrumentType);
		var1.write(this.pitch);
	}

	public int getPacketSize() {
		return 12;
	}
}
