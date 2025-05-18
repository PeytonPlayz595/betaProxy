package net.minecraft.network.v9;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet28 extends Packet {
	public int entityId;
	public int motionX;
	public int motionY;
	public int motionZ;

	public Packet28() {
	}

	public Packet28(int var1, double var2, double var4, double var6) {
		this.entityId = var1;
		double var8 = 3.9D;
		if(var2 < -var8) {
			var2 = -var8;
		}

		if(var4 < -var8) {
			var4 = -var8;
		}

		if(var6 < -var8) {
			var6 = -var8;
		}

		if(var2 > var8) {
			var2 = var8;
		}

		if(var4 > var8) {
			var4 = var8;
		}

		if(var6 > var8) {
			var6 = var8;
		}

		this.motionX = (int)(var2 * 8000.0D);
		this.motionY = (int)(var4 * 8000.0D);
		this.motionZ = (int)(var6 * 8000.0D);
	}

	public void readPacketData(DataInputStream var1) throws IOException {
		this.entityId = var1.readInt();
		this.motionX = var1.readShort();
		this.motionY = var1.readShort();
		this.motionZ = var1.readShort();
	}

	public void writePacketData(DataOutputStream var1) throws IOException {
		var1.writeInt(this.entityId);
		var1.writeShort(this.motionX);
		var1.writeShort(this.motionY);
		var1.writeShort(this.motionZ);
	}

	public int getPacketSize() {
		return 10;
	}
}
