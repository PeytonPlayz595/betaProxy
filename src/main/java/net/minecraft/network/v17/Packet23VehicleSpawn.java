package net.minecraft.network.v17;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet23VehicleSpawn extends Packet {
	public int entityId;
	public int xPosition;
	public int yPosition;
	public int zPosition;
	public int field_28044_e;
	public int field_28043_f;
	public int field_28042_g;
	public int type;
	public int field_28041_i;

	public Packet23VehicleSpawn() {
	}

	public void readPacketData(DataInputStream var1) throws IOException {
		this.entityId = var1.readInt();
		this.type = var1.readByte();
		this.xPosition = var1.readInt();
		this.yPosition = var1.readInt();
		this.zPosition = var1.readInt();
		this.field_28041_i = var1.readInt();
		if (this.field_28041_i > 0) {
			this.field_28044_e = var1.readShort();
			this.field_28043_f = var1.readShort();
			this.field_28042_g = var1.readShort();
		}

	}

	public void writePacketData(DataOutputStream var1) throws IOException {
		var1.writeInt(this.entityId);
		var1.writeByte(this.type);
		var1.writeInt(this.xPosition);
		var1.writeInt(this.yPosition);
		var1.writeInt(this.zPosition);
		var1.writeInt(this.field_28041_i);
		if (this.field_28041_i > 0) {
			var1.writeShort(this.field_28044_e);
			var1.writeShort(this.field_28043_f);
			var1.writeShort(this.field_28042_g);
		}

	}

	public int getPacketSize() {
		return 21 + this.field_28041_i > 0 ? 6 : 0;
	}
}
