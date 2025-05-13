package net.betaProxy.utils;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;

import net.betaProxy.server.Server;

public class ProtocolAwarePacketReader {
	
	private Server server;
	private int pvn;
	
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
