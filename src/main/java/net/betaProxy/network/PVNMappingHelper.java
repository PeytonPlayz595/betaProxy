package net.betaProxy.network;

public class PVNMappingHelper {
	
	private static int pvn;
	
	public static void setPNVVersion(int pvn) {
		PVNMappingHelper.pvn = pvn;
	}
	
	public static int getServerPVN() {
		return pvn;
	}
	
	public static String getSupportedVersionNames() {
		switch(pvn) {
		case 14:
			return "Beta 1.7 -> Beta 1.7.3";
		case 8:
			return "Beta 1.1_02 -> Beta 1.2_02";
		case 7:
			return "Beta 1.0 -> Beta 1.1_01";
		case 6:
			return "Alpha v1.2.3_05 -> Alpha v1.2.6";
		case 2:
			return "Aplha v1.1.0 -> Alpha v1.1.2_01";
		default:
			throw new RuntimeException("Unsupported server protocol version: '" + pvn + "'");
		}
	}

}
