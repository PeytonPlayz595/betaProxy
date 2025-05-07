package net.betaProxy.network;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;

import net.betaProxy.server.ProxyServer;
import net.minecraft.network.Packet;

public class PacketDefragmenter {
	
	public static byte[] defragment(DataInputStream is) throws IOException {
		is.mark(4096);
		
		byte[] data = null;
		try {
			data = Packet.readPacket(is);
		} catch(EOFException e) {
			is.reset();
		} catch(IOException e) {
			is.reset();
			int i = is.read(); //Skip packet
			ProxyServer.getLogger().error("Received invalid packet with ID '" + i + "'!");
		}
		
		return data;
	}
	
}
