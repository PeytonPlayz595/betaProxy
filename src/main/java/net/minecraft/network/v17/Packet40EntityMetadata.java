package net.minecraft.network.v17;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import net.minecraft.utils.v9.DataWatcher;

public class Packet40EntityMetadata extends Packet {
	public int entityId;
	private List metadata;

	public Packet40EntityMetadata() {
	}

	public void readPacketData(DataInputStream var1) throws IOException {
		this.entityId = var1.readInt();
		this.metadata = DataWatcher.readWatchableObjects(var1);
	}

	public void writePacketData(DataOutputStream var1) throws IOException {
		var1.writeInt(this.entityId);
		DataWatcher.writeObjectsInListToStream(this.metadata, var1);
	}

	public int getPacketSize() {
		return 5;
	}
}
