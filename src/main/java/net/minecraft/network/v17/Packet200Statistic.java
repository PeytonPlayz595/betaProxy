package net.minecraft.network.v17;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet200Statistic extends Packet {
	public int statisticId;
	public int amount;

	public Packet200Statistic() {
	}

	public Packet200Statistic(int var1, int var2) {
		this.statisticId = var1;
		this.amount = var2;
	}

	public void readPacketData(DataInputStream var1) throws IOException {
		this.statisticId = var1.readInt();
		this.amount = var1.readByte();
	}

	public void writePacketData(DataOutputStream var1) throws IOException {
		var1.writeInt(this.statisticId);
		var1.writeByte(this.amount);
	}

	public int getPacketSize() {
		return 6;
	}
}
