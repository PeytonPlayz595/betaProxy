package net.minecraft.network.local;

import java.io.DataInputStream;
import java.io.IOException;

import net.betaProxy.utils.ServerProtocolVersion;

public class LoginPacket extends ServerPacket {
	
	public int pvn;

	@Override
	public ServerPacket readPacketData(DataInputStream dis) throws IOException {
		this.pvn = dis.readInt();
		return this;
	}

	@Override
	public boolean isDataConsistant(ServerProtocolVersion spv) {
		return this.pvn == spv.getServerPVN();
	}

}
