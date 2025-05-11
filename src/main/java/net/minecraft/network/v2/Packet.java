package net.minecraft.network.v2;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.betaProxy.suppliers.global.IGlobalPacketInterface;
import net.betaProxy.suppliers.global.IGlobalPacketSupplier;

public abstract class Packet implements IGlobalPacketInterface {
	private static Map<Integer, IGlobalPacketSupplier<Packet>> packetIdToSupplierMap = new HashMap<Integer, IGlobalPacketSupplier<Packet>>();
	private static Map<Class<? extends Packet>, Integer> packetClassToIdMap = new HashMap<Class<? extends Packet>, Integer>();
	public boolean isChunkDataPacket = false;

	static void addIdClassMapping(int var0, Class<? extends Packet> var1, IGlobalPacketSupplier<Packet> var2) {
		if(packetIdToSupplierMap.containsKey(Integer.valueOf(var0))) {
			throw new IllegalArgumentException("Duplicate packet id:" + var0);
		} else if(packetClassToIdMap.containsKey(var1)) {
			throw new IllegalArgumentException("Duplicate packet class:" + var1);
		} else {
			packetIdToSupplierMap.put(Integer.valueOf(var0), var2);
			packetClassToIdMap.put(var1, Integer.valueOf(var0));
		}
	}

	public static Packet getNewPacket(int var0) {
		try {
			IGlobalPacketSupplier<Packet> var1 = packetIdToSupplierMap.get(Integer.valueOf(var0));
			return var1 == null ? null : (Packet)var1.supplyGlobalPacket();
		} catch (Exception var2) {
			var2.printStackTrace();
			System.out.println("Skipping packet with id " + var0);
			return null;
		}
	}

	public final int getPacketId() throws IOException {
		return ((Integer)packetClassToIdMap.get(this.getClass())).intValue();
	}

	public static byte[] readPacket(DataInputStream var0) throws IOException {
		int var1 = var0.read();
		if(var1 == -1) {
			return null;
		} else {
			Packet var2 = getNewPacket(var1);
			if(var2 == null) {
				throw new IOException("Bad packet id " + var1);
			} else {
				byte[] data = null;
				var2.readPacketData(var0);
				try(ByteArrayOutputStream baos = new ByteArrayOutputStream(); DataOutputStream dos = new DataOutputStream(baos)) {
					writePacket(var2, dos);
					dos.flush();
					data = baos.toByteArray();
				}
				return data;
			}
		}
	}

	public static void writePacket(Packet var0, DataOutputStream var1) throws IOException {
		var1.write(var0.getPacketId());
		var0.writePacket(var1);
	}

	public abstract void readPacketData(DataInputStream var1) throws IOException;

	public abstract void writePacket(DataOutputStream var1) throws IOException;

	public abstract int getPacketSize();

	static {
		addIdClassMapping(0, Packet0KeepAlive.class, Packet0KeepAlive::new);
		addIdClassMapping(1, Packet1Login.class, Packet1Login::new);
		addIdClassMapping(2, Packet2Handshake.class, Packet2Handshake::new);
		addIdClassMapping(3, Packet3Chat.class, Packet3Chat::new);
		addIdClassMapping(4, Packet4UpdateTime.class, Packet4UpdateTime::new);
		addIdClassMapping(5, Packet5PlayerInventory.class, Packet5PlayerInventory::new);
		addIdClassMapping(6, Packet6SpawnPosition.class, Packet6SpawnPosition::new);
		addIdClassMapping(10, Packet10Flying.class, Packet10Flying::new);
		addIdClassMapping(11, Packet11PlayerPosition.class, Packet11PlayerPosition::new);
		addIdClassMapping(12, Packet12PlayerLook.class, Packet12PlayerLook::new);
		addIdClassMapping(13, Packet13PlayerLookMove.class, Packet13PlayerLookMove::new);
		addIdClassMapping(14, Packet14BlockDig.class, Packet14BlockDig::new);
		addIdClassMapping(15, Packet15Place.class, Packet15Place::new);
		addIdClassMapping(16, Packet16BlockItemSwitch.class, Packet16BlockItemSwitch::new);
		addIdClassMapping(17, Packet17AddToInventory.class, Packet17AddToInventory::new);
		addIdClassMapping(18, Packet18ArmAnimation.class, Packet18ArmAnimation::new);
		addIdClassMapping(20, Packet20NamedEntitySpawn.class, Packet20NamedEntitySpawn::new);
		addIdClassMapping(21, Packet21PickupSpawn.class, Packet21PickupSpawn::new);
		addIdClassMapping(22, Packet22Collect.class, Packet22Collect::new);
		addIdClassMapping(23, Packet23VehicleSpawn.class, Packet23VehicleSpawn::new);
		addIdClassMapping(24, Packet24MobSpawn.class, Packet24MobSpawn::new);
		addIdClassMapping(29, Packet29DestroyEntity.class, Packet29DestroyEntity::new);
		addIdClassMapping(30, Packet30Entity.class, Packet30Entity::new);
		addIdClassMapping(31, Packet31RelEntityMove.class, Packet31RelEntityMove::new);
		addIdClassMapping(32, Packet32EntityLook.class, Packet32EntityLook::new);
		addIdClassMapping(33, Packet33RelEntityMoveLook.class, Packet33RelEntityMoveLook::new);
		addIdClassMapping(34, Packet34EntityTeleport.class, Packet34EntityTeleport::new);
		addIdClassMapping(50, Packet50PreChunk.class, Packet50PreChunk::new);
		addIdClassMapping(51, Packet51MapChunk.class, Packet51MapChunk::new);
		addIdClassMapping(52, Packet52MultiBlockChange.class, Packet52MultiBlockChange::new);
		addIdClassMapping(53, Packet53BlockChange.class, Packet53BlockChange::new);
		addIdClassMapping(59, Packet59ComplexEntity.class, Packet59ComplexEntity::new);
		addIdClassMapping(255, Packet255KickDisconnect.class, Packet255KickDisconnect::new);
	}
}
