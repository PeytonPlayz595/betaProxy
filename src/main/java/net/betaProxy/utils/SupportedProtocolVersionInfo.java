package net.betaProxy.utils;

public class SupportedProtocolVersionInfo {
	
	private static int pvn;
	private static boolean isAutoDetect = false;
	
	public static void setPNVVersion(Integer pvn) {
		if(pvn == null) {
			isAutoDetect = true;
		}
		SupportedProtocolVersionInfo.pvn = pvn.intValue();
	}
	
	public static int getServerPVN() {
		return pvn;
	}
	
	public static boolean isAutoDetectPVN() {
		return isAutoDetect;
	}
	
	public static void matchedClientPVN() {
		isAutoDetect = false;
	}
	
	public static String getSupportedVersionNames() {
		switch(pvn) {
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
