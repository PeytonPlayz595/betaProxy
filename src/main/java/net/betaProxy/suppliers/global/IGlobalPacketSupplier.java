package net.betaProxy.suppliers.global;

public interface IGlobalPacketSupplier<T extends IGlobalPacketInterface> {
	T supplyGlobalPacket();
}
