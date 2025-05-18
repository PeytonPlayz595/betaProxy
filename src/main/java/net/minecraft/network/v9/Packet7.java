package net.minecraft.network.v9;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet7 extends Packet {
	public int playerEntityId;
	public int targetEntity;
	public int isLeftClick;

	public Packet7() {
	}

	public Packet7(int var1, int var2, int var3) {
		this.playerEntityId = var1;
		this.targetEntity = var2;
		this.isLeftClick = var3;
	}

	public void readPacketData(DataInputStream var1) throws IOException {
		this.playerEntityId = var1.readInt();
		this.targetEntity = var1.readInt();
		this.isLeftClick = var1.readByte();
	}

	public void writePacketData(DataOutputStream var1) throws IOException {
		var1.writeInt(this.playerEntityId);
		var1.writeInt(this.targetEntity);
		var1.writeByte(this.isLeftClick);
	}

	public int getPacketSize() {
		return 9;
	}
}
