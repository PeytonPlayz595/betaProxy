package net.minecraft.network.v17;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet1Login extends Packet {
	public int protocolVersion;
	public String username;
	public long mapSeed;
	public int field_35131_d;
	public byte field_35132_e;
	public byte field_35129_f;
	public byte field_35130_g;
	public byte field_35133_h;

	public Packet1Login() {
	}

	public Packet1Login(String var1, int var2, long var3, int var5, byte var6, byte var7, byte var8, byte var9) {
		this.username = var1;
		this.protocolVersion = var2;
		this.mapSeed = var3;
		this.field_35132_e = var6;
		this.field_35129_f = var7;
		this.field_35131_d = var5;
		this.field_35130_g = var8;
		this.field_35133_h = var9;
	}

	public void readPacketData(DataInputStream var1) throws IOException {
		this.protocolVersion = var1.readInt();
		this.username = var1.readUTF();
		this.mapSeed = var1.readLong();
		this.field_35131_d = var1.readInt();
		this.field_35132_e = var1.readByte();
		this.field_35129_f = var1.readByte();
		this.field_35130_g = var1.readByte();
		this.field_35133_h = var1.readByte();
	}

	public void writePacketData(DataOutputStream var1) throws IOException {
		var1.writeInt(this.protocolVersion);
		var1.writeUTF(this.username);
		var1.writeLong(this.mapSeed);
		var1.writeInt(this.field_35131_d);
		var1.writeByte(this.field_35132_e);
		var1.writeByte(this.field_35129_f);
		var1.writeByte(this.field_35130_g);
		var1.writeByte(this.field_35133_h);
	}

	public int getPacketSize() {
		return 4 + this.username.length() + 4 + 7 + 4;
	}
}
