package net.betaProxy.utils;

public class ServerProtocolVersion {
	
	private int pvn;
	private boolean isAutoDetect = false;
	
	public ServerProtocolVersion(Integer pvn) {
		if(pvn == Integer.valueOf(-1)) {
			isAutoDetect = true;
			this.pvn = Integer.valueOf(-1);
		} else {
			this.pvn = pvn.intValue();
		}
	}
	
	public void setPNVVersion(Integer pvn) {
		if(pvn == -1) {
			isAutoDetect = true;
		}
		this.pvn = pvn.intValue();
	}
	
	public int getServerPVN() {
		return pvn;
	}
	
	public boolean isAutoDetectPVN() {
		return isAutoDetect;
	}
	
	public void matchedClientPVN() {
		isAutoDetect = false;
	}
	
	public String getSupportedVersionNames() {
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
