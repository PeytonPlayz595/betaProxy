package net.minecraft.network.v17;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet71Weather extends Packet {
	public int entityID;
	public int posX;
	public int posY;
	public int posZ;
	public int isLightningBolt;

	public Packet71Weather() {
	}

	public void readPacketData(DataInputStream var1) throws IOException {
		this.entityID = var1.readInt();
		this.isLightningBolt = var1.readByte();
		this.posX = var1.readInt();
		this.posY = var1.readInt();
		this.posZ = var1.readInt();
	}

	public void writePacketData(DataOutputStream var1) throws IOException {
		var1.writeInt(this.entityID);
		var1.writeByte(this.isLightningBolt);
		var1.writeInt(this.posX);
		var1.writeInt(this.posY);
		var1.writeInt(this.posZ);
	}

	public int getPacketSize() {
		return 17;
	}
}
