package net.betaProxy.utils;

import java.nio.ByteBuffer;

import net.minecraft.network.local.LoginPacket;
import net.minecraft.network.local.ServerPacket;

public class ClientServerProtocolMatcher {
	
	public boolean hasMatched = false;
	public boolean isError = false;
	private int attempts = 0;
	public boolean outdatedServer;
	public boolean outdatedClient;
	public boolean hasSent = false;
	
	public ClientServerProtocolMatcher() {
		
	}

	public void attemptMatch(ByteBuffer buf) {
		if(hasMatched) {
			return;
		}
		if(isError) {
			return;
		}
		++attempts;
		
		ByteBuffer pkt = buf.duplicate().position(0);
		byte[] data = new byte[pkt.remaining()];
		pkt.get(data);
		ServerPacket SPKT = ServerPacket.readPacketData(data);
		if(SPKT != null && SPKT instanceof LoginPacket) {
			if(SPKT.isDataConsistant()) {
				hasMatched = true;
				return;
			} else {
				isError = true;
				int pvn = ((LoginPacket)SPKT).pvn;
				if(pvn > SupportedProtocolVersionInfo.getServerPVN()) {
					outdatedServer = true;
				} else {
					outdatedClient = true;
				}
			}
		}
		if(attempts >= 2) {
			isError = true;
		}
	}

}
