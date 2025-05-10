package net.peyton.suppliers;

public interface IPacketSupplier<T extends IGlobalPacketInterface> {
//	T supplyPacketV8();
//	
//	K supplyPacketV6();
	
	T supplyGlobalPacket();
}
