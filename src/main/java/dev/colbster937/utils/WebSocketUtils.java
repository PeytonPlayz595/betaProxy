package dev.colbster937.utils;

import java.util.HashMap;
import java.util.Map;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;

public class WebSocketUtils {
  public static Map<String, String> getProxyUserInfo(WebSocket webSocket, ClientHandshake handshake) {
    String ip_w = handshake.getFieldValue("x-webmc-ip");
    String ip_c = handshake.getFieldValue("cf-connecting-ip");
    String ip_f = handshake.getFieldValue("x-forwarded-for");
    String ip_r = handshake.getFieldValue("x-real-ip");
    String ip_d = webSocket.getRemoteSocketAddress().getHostString();

    String port_h = handshake.getFieldValue("x-forwarded-port");
    String port_r = handshake.getFieldValue("x-real-port");
    String port_d = String.valueOf(webSocket.getRemoteSocketAddress().getPort());

    String url_h = handshake.getFieldValue("Host");
    String url_d = handshake.getResourceDescriptor();

    String ip = firstNonNull(ip_w, ip_c, ip_f, ip_r, ip_d);
    if (ip == null)
      ip = "127.0.0.1";
    else if (ip.contains(","))
      ip = ip.split(",")[0].trim();

    String port = firstNonNull(port_h, port_r, port_d);
    String url = firstNonNull(url_h, url_d);

    Map<String, String> info = new HashMap<>();
    info.put("ip", ip);
    info.put("port", port);
    info.put("url", url);
    return info;
  }

  private static String firstNonNull(String... values) {
    for (String v : values) {
      if (v != null && !v.isEmpty())
        return v;
    }
    return null;
  }
}
