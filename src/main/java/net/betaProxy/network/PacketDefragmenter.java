package net.betaProxy.network;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;

import net.betaProxy.server.ProxyServer;
import net.minecraft.network.v8.Packet;

public class PacketDefragmenter {
	
	public static byte[] defragment(DataInputStream is) throws IOException {
		is.mark(4096);
		
		byte[] data = null;
		try {
			data = readPacket(is);
		} catch(EOFException e) {
			is.reset();
		} catch(IOException e) {
			is.reset();
			int i = is.read(); //Skip packet
			ProxyServer.getLogger().error("Received invalid packet with ID '" + i + "'!");
		}
		
		return data;
	}
	
	private static byte[] readPacket(DataInputStream is) throws IOException {
		switch(PVNMappingHelper.getServerPVN()) {
		case 8:
			return net.minecraft.network.v8.Packet.readPacket(is);
		case 7:
			//Client-side packets didn't have any changes from 7-8
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
