package net.minecraft.network.v9;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet1Login extends Packet {
	public int protocolVersion;
	public String username;
	public String password;
	public long mapSeed;
	public byte dimension;

	public Packet1Login() {
	}

	public Packet1Login(String var1, String var2, int var3) {
		this.username = var1;
		this.password = var2;
		this.protocolVersion = var3;
	}

	public void readPacketData(DataInputStream var1) throws IOException {
		this.protocolVersion = var1.readInt();
		this.username = var1.readUTF();
		this.password = var1.readUTF();
		this.mapSeed = var1.readLong();
		this.dimension = var1.readByte();
	}

	public void writePacketData(DataOutputStream var1) throws IOException {
		var1.writeInt(this.protocolVersion);
		var1.writeUTF(this.username);
		var1.writeUTF(this.password);
		var1.writeLong(this.mapSeed);
		var1.writeByte(this.dimension);
	}

	public int getPacketSize() {
		return 4 + this.username.length() + this.password.length() + 4 + 5;
	}
}
