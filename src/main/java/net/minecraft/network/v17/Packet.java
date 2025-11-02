package net.minecraft.network.v17;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.betaProxy.suppliers.global.IGlobalPacketInterface;
import net.betaProxy.suppliers.global.IGlobalPacketSupplier;

public abstract class Packet implements IGlobalPacketInterface {
	private static Map<Integer, IGlobalPacketSupplier<Packet>> packetIdToSupplierMap = new HashMap<Integer, IGlobalPacketSupplier<Packet>>();
	private static Map<Class<? extends Packet>, Integer> packetClassToIdMap = new HashMap<Class<? extends Packet>, Integer>();
	public final long creationTimeMillis = System.currentTimeMillis();
	public boolean isChunkDataPacket = false;

	static void addIdClassMapping(int var0, Class<? extends Packet> var1, IGlobalPacketSupplier<Packet> supplier) {
		if (packetIdToSupplierMap.containsKey(Integer.valueOf(var0))) {
			throw new IllegalArgumentException("Duplicate packet id:" + var0);
		} else if (packetClassToIdMap.containsKey(var1)) {
			throw new IllegalArgumentException("Duplicate packet class:" + var1);
		} else {
			packetIdToSupplierMap.put(Integer.valueOf(var0), supplier);
			packetClassToIdMap.put(var1, Integer.valueOf(var0));
		}
	}

	public static Packet getNewPacket(int var0) {
		try {
			IGlobalPacketSupplier<Packet> var1 = packetIdToSupplierMap.get(Integer.valueOf(var0));
			return var1 == null ? null : (Packet) var1.supplyGlobalPacket();
		} catch (Exception var2) {
			var2.printStackTrace();
			System.out.println("Skipping packet with id " + var0);
			return null;
		}
	}

	public final int getPacketId() {
		return ((Integer) packetClassToIdMap.get(this.getClass())).intValue();
	}

	public static byte[] readPacket(DataInputStream var0) throws IOException {
		int var1 = var0.read();
		if (var1 == -1) {
			return null;
		} else {
			Packet var2 = getNewPacket(var1);
			if (var2 == null) {
				throw new IOException("Bad packet id " + var1);
			} else {
				byte[] data = null;
				var2.readPacketData(var0);
				try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
						DataOutputStream dos = new DataOutputStream(baos)) {
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
		var0.writePacketData(var1);
	}

	public abstract void readPacketData(DataInputStream var1) throws IOException;

	public abstract void writePacketData(DataOutputStream var1) throws IOException;

	public abstract int getPacketSize();

	static {
		addIdClassMapping(0, Packet0KeepAlive.class, Packet0KeepAlive::new);
		addIdClassMapping(1, Packet1Login.class, Packet1Login::new);
		addIdClassMapping(2, Packet2Handshake.class, Packet2Handshake::new);
		addIdClassMapping(3, Packet3Chat.class, Packet3Chat::new);
		addIdClassMapping(4, Packet4UpdateTime.class, Packet4UpdateTime::new);
		addIdClassMapping(5, Packet5PlayerInventory.class, Packet5PlayerInventory::new);
		addIdClassMapping(6, Packet6SpawnPosition.class, Packet6SpawnPosition::new);
		addIdClassMapping(7, Packet7UseEntity.class, Packet7UseEntity::new);
		addIdClassMapping(8, Packet8UpdateHealth.class, Packet8UpdateHealth::new);
		addIdClassMapping(9, Packet9Respawn.class, Packet9Respawn::new);
		addIdClassMapping(10, Packet10Flying.class, Packet10Flying::new);
		addIdClassMapping(11, Packet11PlayerPosition.class, Packet11PlayerPosition::new);
		addIdClassMapping(12, Packet12PlayerLook.class, Packet12PlayerLook::new);
		addIdClassMapping(13, Packet13PlayerLookMove.class, Packet13PlayerLookMove::new);
		addIdClassMapping(14, Packet14BlockDig.class, Packet14BlockDig::new);
		addIdClassMapping(15, Packet15Place.class, Packet15Place::new);
		addIdClassMapping(16, Packet16BlockItemSwitch.class, Packet16BlockItemSwitch::new);
		addIdClassMapping(17, Packet17Sleep.class, Packet17Sleep::new);
		addIdClassMapping(18, Packet18Animation.class, Packet18Animation::new);
		addIdClassMapping(19, Packet19EntityAction.class, Packet19EntityAction::new);
		addIdClassMapping(20, Packet20NamedEntitySpawn.class, Packet20NamedEntitySpawn::new);
		addIdClassMapping(21, Packet21PickupSpawn.class, Packet21PickupSpawn::new);
		addIdClassMapping(22, Packet22Collect.class, Packet22Collect::new);
		addIdClassMapping(23, Packet23VehicleSpawn.class, Packet23VehicleSpawn::new);
		addIdClassMapping(24, Packet24MobSpawn.class, Packet24MobSpawn::new);
		addIdClassMapping(25, Packet25EntityPainting.class, Packet25EntityPainting::new);
		addIdClassMapping(26, Packet26EntityExpOrb.class, Packet26EntityExpOrb::new);
		addIdClassMapping(27, Packet27Position.class, Packet27Position::new);
		addIdClassMapping(28, Packet28EntityVelocity.class, Packet28EntityVelocity::new);
		addIdClassMapping(29, Packet29DestroyEntity.class, Packet29DestroyEntity::new);
		addIdClassMapping(30, Packet30Entity.class, Packet30Entity::new);
		addIdClassMapping(31, Packet31RelEntityMove.class, Packet31RelEntityMove::new);
		addIdClassMapping(32, Packet32EntityLook.class, Packet32EntityLook::new);
		addIdClassMapping(33, Packet33RelEntityMoveLook.class, Packet33RelEntityMoveLook::new);
		addIdClassMapping(34, Packet34EntityTeleport.class, Packet34EntityTeleport::new);
		addIdClassMapping(38, Packet38EntityStatus.class, Packet38EntityStatus::new);
		addIdClassMapping(39, Packet39AttachEntity.class, Packet39AttachEntity::new);
		addIdClassMapping(40, Packet40EntityMetadata.class, Packet40EntityMetadata::new);
		addIdClassMapping(41, Packet41EntityEffect.class, Packet41EntityEffect::new);
		addIdClassMapping(42, Packet42RemoveEntityEffect.class, Packet42RemoveEntityEffect::new);
		addIdClassMapping(43, Packet43Experience.class, Packet43Experience::new);
		addIdClassMapping(50, Packet50PreChunk.class, Packet50PreChunk::new);
		addIdClassMapping(51, Packet51MapChunk.class, Packet51MapChunk::new);
		addIdClassMapping(52, Packet52MultiBlockChange.class, Packet52MultiBlockChange::new);
		addIdClassMapping(53, Packet53BlockChange.class, Packet53BlockChange::new);
		addIdClassMapping(54, Packet54PlayNoteBlock.class, Packet54PlayNoteBlock::new);
		addIdClassMapping(60, Packet60Explosion.class, Packet60Explosion::new);
		addIdClassMapping(61, Packet61DoorChange.class, Packet61DoorChange::new);
		addIdClassMapping(70, Packet70Bed.class, Packet70Bed::new);
		addIdClassMapping(71, Packet71Weather.class, Packet71Weather::new);
		addIdClassMapping(100, Packet100OpenWindow.class, Packet100OpenWindow::new);
		addIdClassMapping(101, Packet101CloseWindow.class, Packet101CloseWindow::new);
		addIdClassMapping(102, Packet102WindowClick.class, Packet102WindowClick::new);
		addIdClassMapping(103, Packet103SetSlot.class, Packet103SetSlot::new);
		addIdClassMapping(104, Packet104WindowItems.class, Packet104WindowItems::new);
		addIdClassMapping(105, Packet105UpdateProgressbar.class, Packet105UpdateProgressbar::new);
		addIdClassMapping(106, Packet106Transaction.class, Packet106Transaction::new);
		addIdClassMapping(107, Packet107CreativeSetSlot.class, Packet107CreativeSetSlot::new);
		addIdClassMapping(130, Packet130UpdateSign.class, Packet130UpdateSign::new);
		addIdClassMapping(131, Packet131MapData.class, Packet131MapData::new);
		addIdClassMapping(200, Packet200Statistic.class, Packet200Statistic::new);
		addIdClassMapping(201, Packet201PlayerInfo.class, Packet201PlayerInfo::new);
		addIdClassMapping(255, Packet255KickDisconnect.class, Packet255KickDisconnect::new);
	}
}
