package net.minecraft.network.local;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.betaProxy.suppliers.local.ILocalPacketSupplier;
import net.betaProxy.utils.ByteBufferInputStream;

/*
 * This is used for mapping packet data
 * across multiple different protocols or
 * to verify that the data received in a
 * packet is consistant with the servers
 * protocol version.
 */
public abstract class ServerPacket {
	
	private static Map<Integer, ILocalPacketSupplier<ServerPacket>> idToSupplier = new ConcurrentHashMap<Integer, ILocalPacketSupplier<ServerPacket>>();
	private static Map<Class<? extends ServerPacket>, Integer> classToId = new HashMap<Class<? extends ServerPacket>, Integer>();
	
	static void addMapping(int id, Class<? extends ServerPacket> clazz, ILocalPacketSupplier<ServerPacket> supplier) {
		if(idToSupplier.containsKey(Integer.valueOf(id))) {
			throw new IllegalArgumentException("Duplicate BetaProxy packet with id: " + id);
		} if(classToId.containsKey(clazz)) {
			throw new IllegalArgumentException("Duplicate BetaProxy packet with class name: " + clazz.getSimpleName());
		} else {
			idToSupplier.put(Integer.valueOf(id), supplier);
			classToId.put(clazz, Integer.valueOf(id));
		}
	}
	
	public static ServerPacket readPacketData(byte[] data) {
		try(DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data))) {
			int id = dis.read();
			
			ServerPacket pkt;
			if(idToSupplier.containsKey(id)) {
				pkt = idToSupplier.get(id).supplyLocalPacket();
			} else {
				return null;
			}
			
			if(pkt == null) {
				return null;
			}
			
			pkt.readPacketData(dis);
			return pkt;
		} catch(Exception e) {
			return null;
		}
	}
	
	public abstract ServerPacket readPacketData(DataInputStream dis) throws IOException;
	
	public abstract boolean isDataConsistant();
	
	static {
		addMapping(1, LoginPacket.class, LoginPacket::new);
	}

}
