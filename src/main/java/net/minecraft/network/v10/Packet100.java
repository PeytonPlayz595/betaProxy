package net.minecraft.network.v10;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet100 extends Packet {
	public int windowId;
	public int inventoryType;
	public String windowTitle;
	public int slotsCount;

	public void readPacketData(DataInputStream var1) throws IOException {
		this.windowId = var1.readByte();
		this.inventoryType = var1.readByte();
		this.windowTitle = var1.readUTF();
		this.slotsCount = var1.readByte();
	}

	public void writePacketData(DataOutputStream var1) throws IOException {
		var1.writeByte(this.windowId);
		var1.writeByte(this.inventoryType);
		var1.writeUTF(this.windowTitle);
		var1.writeByte(this.slotsCount);
	}

	public int getPacketSize() {
		return 3 + this.windowTitle.length();
	}
}
