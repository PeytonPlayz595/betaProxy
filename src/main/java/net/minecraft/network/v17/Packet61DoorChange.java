package net.minecraft.network.v17;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet61DoorChange extends Packet {
	public int sfxID;
	public int auxData;
	public int posX;
	public int posY;
	public int posZ;

	public Packet61DoorChange() {
	}

	public Packet61DoorChange(int var1, int var2, int var3, int var4, int var5) {
		this.sfxID = var1;
		this.posX = var2;
		this.posY = var3;
		this.posZ = var4;
		this.auxData = var5;
	}

	public void readPacketData(DataInputStream var1) throws IOException {
		this.sfxID = var1.readInt();
		this.posX = var1.readInt();
		this.posY = var1.readByte();
		this.posZ = var1.readInt();
		this.auxData = var1.readInt();
	}

	public void writePacketData(DataOutputStream var1) throws IOException {
		var1.writeInt(this.sfxID);
		var1.writeInt(this.posX);
		var1.writeByte(this.posY);
		var1.writeInt(this.posZ);
		var1.writeInt(this.auxData);
	}

	public int getPacketSize() {
		return 20;
	}
}
