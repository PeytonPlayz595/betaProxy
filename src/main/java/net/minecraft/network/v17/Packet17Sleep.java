package net.minecraft.network.v17;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet17Sleep extends Packet {
	public int entityID;
	public int bedX;
	public int bedY;
	public int bedZ;
	public int field_22042_e;

	public Packet17Sleep() {
	}

	public void readPacketData(DataInputStream var1) throws IOException {
		this.entityID = var1.readInt();
		this.field_22042_e = var1.readByte();
		this.bedX = var1.readInt();
		this.bedY = var1.readByte();
		this.bedZ = var1.readInt();
	}

	public void writePacketData(DataOutputStream var1) throws IOException {
		var1.writeInt(this.entityID);
		var1.writeByte(this.field_22042_e);
		var1.writeInt(this.bedX);
		var1.writeByte(this.bedY);
		var1.writeInt(this.bedZ);
	}

	public int getPacketSize() {
		return 14;
	}
}
