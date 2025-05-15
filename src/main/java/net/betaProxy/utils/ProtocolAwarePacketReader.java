package net.betaProxy.utils;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;

import net.betaProxy.server.Server;

public class ProtocolAwarePacketReader {
	
	private Server server;
	protected int pvn;
	
	public ProtocolAwarePacketReader(Server server, int pvn) {
		this.server = server;
		this.pvn = pvn;
	}
	
	public byte[] defragment(DataInputStream is) throws IOException {
		is.mark(4096);
		
		byte[] data = null;
		try {
			data = readPacket(is);
		} catch(EOFException e) {
			is.reset();
		} catch(IOException e) {
			is.reset();
			int i = is.read();
			server.getLogger().error("Received invalid packet with ID '" + i + "'!");
		}
		
		return data;
	}
	
	private byte[] readPacket(DataInputStream is) throws IOException {
		if(SupportedProtocolVersionInfo.isAutoDetectPVN()) {
			/*
			 * TODO:
			 * Login and handshake packets should be the same for
			 * all currently supported protocols but change in
			 * protocol 14 so this will need to be rewritten
			 * as support for new protocols get added.
			 */
			return net.minecraft.network.v8.Packet.readPacket(is);
		}
		switch(pvn) {
		case 8:
			return net.minecraft.network.v8.Packet.readPacket(is);
		case 7:
			//Packets didn't have any changes from 7-8
			return net.minecraft.network.v8.Packet.readPacket(is);
		case 6:
			return net.minecraft.network.v6.Packet.readPacket(is);
		case 2:
			return net.minecraft.network.v2.Packet.readPacket(is);
		default:
			return null;
		}
	}
	
}
