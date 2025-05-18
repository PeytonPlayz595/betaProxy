package net.minecraft.network.v10;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import net.minecraft.utils.v9.DataWatcher;
import net.minecraft.utils.v9.WatchableObject;

public class Packet40 extends Packet {
	public int entityId;
	private List<WatchableObject> field_21048_b;

	public void readPacketData(DataInputStream var1) throws IOException {
		this.entityId = var1.readInt();
		this.field_21048_b = DataWatcher.readWatchableObjects(var1);
	}

	public void writePacketData(DataOutputStream var1) throws IOException {
		var1.writeInt(this.entityId);
		DataWatcher.writeObjectsInListToStream(this.field_21048_b, var1);
	}

	public int getPacketSize() {
		return 5;
	}

	public List<WatchableObject> func_21047_b() {
		return this.field_21048_b;
	}
}
