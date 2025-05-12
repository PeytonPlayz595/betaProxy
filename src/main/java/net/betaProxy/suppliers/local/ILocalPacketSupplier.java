package net.betaProxy.suppliers.local;

import net.minecraft.network.local.ServerPacket;

public interface ILocalPacketSupplier<T extends ServerPacket> {
	T supplyLocalPacket();
}
