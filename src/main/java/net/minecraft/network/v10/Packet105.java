package net.minecraft.network.v10;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet105 extends Packet {
	public int windowId;
	public int progressBar;
	public int progressBarValue;

	public void readPacketData(DataInputStream var1) throws IOException {
		this.windowId = var1.readByte();
		this.progressBar = var1.readShort();
		this.progressBarValue = var1.readShort();
	}

	public void writePacketData(DataOutputStream var1) throws IOException {
		var1.writeByte(this.windowId);
		var1.writeShort(this.progressBar);
		var1.writeShort(this.progressBarValue);
	}

	public int getPacketSize() {
		return 5;
	}
}
