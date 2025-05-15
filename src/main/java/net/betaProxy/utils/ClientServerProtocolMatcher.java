package net.betaProxy.utils;

import java.nio.ByteBuffer;

import net.betaProxy.websocket.WebsocketNetworkManager;
import net.minecraft.network.local.LoginPacket;
import net.minecraft.network.local.ServerPacket;

public class ClientServerProtocolMatcher {
	
	public boolean hasMatched = false;
	public boolean isError = false;
	private int attempts = 0;
	public boolean outdatedServer;
	public boolean outdatedClient;
	public boolean hasSent = false;
	
	private WebsocketNetworkManager mngr;
	
	public ClientServerProtocolMatcher(WebsocketNetworkManager mngr) {
		this.mngr = mngr;
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
			int pvn = ((LoginPacket)SPKT).pvn;
			if(SPKT.isDataConsistant()) {
				hasMatched = true;
				SupportedProtocolVersionInfo.matchedClientPVN();
				SupportedProtocolVersionInfo.setPNVVersion(pvn);
				this.mngr.packetReader.pvn = pvn;
				return;
			} else {
				isError = true;
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
