package net.minecraft.network.v10;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet10Flying extends Packet {
	public double xPosition;
	public double yPosition;
	public double zPosition;
	public double stance;
	public float yaw;
	public float pitch;
	public boolean onGround;
	public boolean moving;
	public boolean rotating;

	public Packet10Flying() {
	}

	public Packet10Flying(boolean var1) {
		this.onGround = var1;
	}

	public void readPacketData(DataInputStream var1) throws IOException {
		this.onGround = var1.read() != 0;
	}

	public void writePacketData(DataOutputStream var1) throws IOException {
		var1.write(this.onGround ? 1 : 0);
	}

	public int getPacketSize() {
		return 1;
	}
}
