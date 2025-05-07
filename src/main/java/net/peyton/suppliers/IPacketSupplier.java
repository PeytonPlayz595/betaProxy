package net.peyton.suppliers;

import net.minecraft.network.Packet;

public interface IPacketSupplier<T extends Packet> {
	T supplyPacket();
}
