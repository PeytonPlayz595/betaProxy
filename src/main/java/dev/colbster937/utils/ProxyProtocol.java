package dev.colbster937.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

public class ProxyProtocol {
  private static final byte[] SIG = new byte[] {
      0x0D, 0x0A, 0x0D, 0x0A,
      0x00, 0x0D, 0x0A, 0x51,
      0x55, 0x49, 0x54, 0x0A
  };

  public static byte[] buildProxyHeader(String srcIP, int srcPort, String dstIP, int dstPort) {
    boolean v6 = isV6(srcIP) || isV6(dstIP);
    int addrLen = v6 ? 36 : 12;

    ByteBuffer h = ByteBuffer.allocate(16 + addrLen);
    h.put(SIG);
    h.put((byte) 0x21);
    h.put((byte) (v6 ? 0x21 : 0x11));
    h.putShort((short) addrLen);

    if (v6) {
      h.put(to16(srcIP));
      h.put(to16(dstIP));
      h.putShort((short) (srcPort & 0xFFFF));
      h.putShort((short) (dstPort & 0xFFFF));
    } else {
      h.put(to4(srcIP));
      h.put(to4(dstIP));
      h.putShort((short) (srcPort & 0xFFFF));
      h.putShort((short) (dstPort & 0xFFFF));
    }

    return h.array();
  }

  private static boolean isV6(String ip) {
    return ip.indexOf(':') >= 0;
  }

  private static byte[] to4(String ip) {
    try {
      byte[] a = InetAddress.getByName(ip).getAddress();
      if (a.length == 4)
        return a;
      if (a.length == 16 && (a[10] == (byte) 0xff && a[11] == (byte) 0xff))
        return new byte[] { a[12], a[13], a[14], a[15] };
      throw new IllegalArgumentException("not IPv4: " + ip);
    } catch (UnknownHostException e) {
      throw new IllegalArgumentException("bad ip: " + ip, e);
    }
  }

  private static byte[] to16(String ip) {
    try {
      byte[] a = InetAddress.getByName(ip).getAddress();
      if (a.length == 16)
        return a;
      if (a.length == 4) {
        byte[] m = new byte[16];
        m[10] = (byte) 0xff;
        m[11] = (byte) 0xff;
        m[12] = a[0];
        m[13] = a[1];
        m[14] = a[2];
        m[15] = a[3];
        return m;
      }
      throw new IllegalArgumentException("bad ip: " + ip);
    } catch (UnknownHostException e) {
      throw new IllegalArgumentException("bad ip: " + ip, e);
    }
  }
}
