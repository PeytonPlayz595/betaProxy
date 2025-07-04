package net.minecraft.network.v9;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet32EntityLook extends Packet30Entity {
	public Packet32EntityLook() {
		this.rotating = true;
	}

	public void readPacketData(DataInputStream var1) throws IOException {
		super.readPacketData(var1);
		this.yaw = var1.readByte();
		this.pitch = var1.readByte();
	}

	public void writePacketData(DataOutputStream var1) throws IOException {
		super.writePacketData(var1);
		var1.writeByte(this.yaw);
		var1.writeByte(this.pitch);
	}

	public int getPacketSize() {
		return 6;
	}
}
