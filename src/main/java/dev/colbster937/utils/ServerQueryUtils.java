package dev.colbster937.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import org.java_websocket.WebSocket;

import org.json.JSONArray;
import org.json.JSONObject;

import net.betaProxy.server.Server;
import net.betaProxy.server.ServerVersion;
import net.betaProxy.utils.ServerProtocolVersion;
import net.betaProxy.websocket.WebsocketNetworkManager;

public class ServerQueryUtils {
  private static final String uuid = UUID.randomUUID().toString();
  private static final String hex = "0123456789abcdef";

  private static Server server;
  private static ServerProtocolVersion spv;
  private static ServerQueryThread queryThread;

  private static boolean initialized = false;
  private static String serverListConfirmCode = "";
  private static String cachedMOTD = "";
  private static int cachedOnlinePlayers = 0;
  private static int cachedMaxPlayers = 0;

  public static void initData(Server server, ServerProtocolVersion spv) {
    if (initialized)
      if (isMOTDSupported())
        queryThread.shutdown();
    ServerQueryUtils.server = server;
    ServerQueryUtils.spv = spv;
    initialized = true;
    if (isMOTDSupported()) {
      queryThread = new ServerQueryThread(true);
      queryThread.start();
    }
  }

  public static String buildEaglerInfo(String motd, int online, int max) {
    JSONObject obj = new JSONObject();
    obj.put("name", ServerVersion.NAME);
    obj.put("brand", ServerVersion.NAME.toLowerCase());
    obj.put("vers", ServerVersion.NAME + "/" + ServerVersion.VERSION);
    obj.put("plaf", "standalone");
    obj.put("cracked", true);
    obj.put("time", System.currentTimeMillis());
    obj.put("uuid", uuid);
    obj.put("type", "motd");

    JSONObject dataObj = new JSONObject();
    dataObj.put("cache", true);

    JSONArray motdArr = new JSONArray();
    motdArr.put(motd);

    dataObj.put("motd", motdArr);
    dataObj.put("icon", false);
    dataObj.put("online", online);
    dataObj.put("max", max);
    dataObj.put("players", new JSONArray());

    obj.put("data", dataObj);

    return obj.toString();
  }

  public static void setServerListConfirmCode(String code) {
    serverListConfirmCode = hash2string(sha1(code.getBytes(StandardCharsets.US_ASCII)));
  }

  public static boolean testServerListConfirmCode(String code) {
    if (code.equals(serverListConfirmCode.toLowerCase())) {
      serverListConfirmCode = null;
      return true;
    }
    return false;
  }

  public static boolean accept(WebSocket webSocket, String message) {
    WebsocketNetworkManager mngr = ((WebsocketNetworkManager) webSocket.getAttachment());
    mngr.setQuerySocket();
    String acc = message.toLowerCase();
    if (acc.startsWith("accept:")) {
      server.getConnections().remove(webSocket);
      acc = acc.substring(7).trim();
      if ("motd".equals(acc)) {
        if (isMOTDSupported()) {
          webSocket.send(buildEaglerInfo(cachedMOTD, cachedOnlinePlayers, cachedMaxPlayers));
        } else {
          int players = server.getConnections().size() - 1;
          webSocket.send(buildEaglerInfo("A " + ServerVersion.NAME + " Server", players, players + 1));
        }
        webSocket.close();
        queryThread.send();
        return true;
      } else if (testServerListConfirmCode(acc)) {
        webSocket.send("OK");
      }
      webSocket.close();
      return true;
    }
    return false;
  }

  public static void pingBackend(boolean verifyResponsePacket) throws IOException {
    Socket socket = null;
    DataInputStream socketInputStream = null;
    DataOutputStream socketOutputStream = null;
    try {
      socket = new Socket();
      socket.setSoTimeout(3000);
      socket.setTcpNoDelay(true);
      socket.setTrafficClass(18);
      socket.connect(server.getMinecraftSocketAddress());
      socketInputStream = new DataInputStream(socket.getInputStream());
      socketOutputStream = new DataOutputStream(socket.getOutputStream());
      pingBackend(socketInputStream, socketOutputStream, verifyResponsePacket);
    } finally {
      try {
        socketInputStream.close();
      } catch (Throwable t) {
      }

      try {
        socketOutputStream.close();
      } catch (Throwable t) {
      }

      try {
        socket.close();
      } catch (Throwable t) {
      }
    }
  }

  private static void pingBackend(DataInputStream socketInputStream, DataOutputStream socketOutputStream,
      boolean verifyResponsePacket) throws IOException {
    socketOutputStream.write(254);
    socketOutputStream.flush();
    if (verifyResponsePacket && socketInputStream.read() != 255)
      throw new IOException("Bad message");
    String pingData[] = new String(socketInputStream.readAllBytes(), StandardCharsets.UTF_16BE).replace("\u0017", "")
        .split("\u00a7");
    String motd = pingData[0];
    int online = cachedOnlinePlayers;
    int max = cachedMaxPlayers;

    try {
      online = Integer.parseInt(pingData[1]);
    } catch (Exception e) {
    }

    try {
      max = Integer.parseInt(pingData[2]);
    } catch (Exception e) {
    }

    cachedMOTD = motd;
    cachedOnlinePlayers = online;
    cachedMaxPlayers = max;
  }

  private static boolean isMOTDSupported() {
    return spv.getServerPVN() >= 17 || spv.isAutoDetectPVN();
  }

  private static byte[] sha1(byte[] input) {
    try {
      return MessageDigest.getInstance("SHA-1").digest(input);
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("SHA-1 is not supported on this JRE!", e);
    }
  }

  private static String hash2string(byte[] b) {
    char[] ret = new char[b.length * 2];
    for (int i = 0; i < b.length; ++i) {
      int bb = (int) b[i] & 0xFF;
      ret[i * 2] = hex.charAt((bb >> 4) & 0xF);
      ret[i * 2 + 1] = hex.charAt(bb & 0xF);
    }
    return new String(ret);
  }
}
