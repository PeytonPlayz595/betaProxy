package net.minecraft.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet15Place extends Packet {
	public int id;
	public int xPosition;
	public int yPosition;
	public int zPosition;
	public ItemStack direction;

	public Packet15Place() {
	}

	public void readPacketData(DataInputStream var1) throws IOException {
		this.id = var1.readInt();
		this.xPosition = var1.read();
		this.yPosition = var1.readInt();
		this.zPosition = var1.read();
		short var2 = var1.readShort();
		if(var2 >= 0) {
			byte var3 = var1.readByte();
			byte var4 = var1.readByte();
			this.direction = new ItemStack(var2, var3, var4);
		} else {
			this.direction = null;
		}

	}

	public void writePacketData(DataOutputStream var1) throws IOException {
		var1.writeInt(this.id);
		var1.write(this.xPosition);
		var1.writeInt(this.yPosition);
		var1.write(this.zPosition);
		if(this.direction == null) {
			var1.writeShort(-1);
		} else {
			var1.writeShort(this.direction.itemID);
			var1.writeByte(this.direction.stackSize);
			var1.writeByte(this.direction.itemDamage);
		}

	}

	public int getPacketSize() {
		return 14;
	}
}
