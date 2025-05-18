package net.betaProxy.utils;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;

import net.betaProxy.server.Server;

public class ProtocolAwarePacketReader {
	
	private Server server;
	protected ServerProtocolVersion spv;
	
	public ProtocolAwarePacketReader(Server server, ServerProtocolVersion spv) {
		this.server = server;
		this.spv = spv;
	}
	
	public byte[] defragment(DataInputStream is) throws IOException {
		is.mark(16384); //16384 is what v9 uses
		
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
		if(spv.isAutoDetectPVN()) {
			try {
				//Tries to read packets received before the PVN is detected
				//as a v9 packet but catches EOFExceptions and uses v8 as a
				//fallback since the reason for the exception is most likely
				//due to the client using a pvn lower than 9
				return net.minecraft.network.v9.Packet.readPacket(is);
			}catch(EOFException e) {
				is.reset();
				is.mark(16384); //16384 is what v9 uses
				return net.minecraft.network.v8.Packet.readPacket(is);
			}
		}
		switch(spv.getServerPVN()) {
		case 9:
			return net.minecraft.network.v9.Packet.readPacket(is);
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
