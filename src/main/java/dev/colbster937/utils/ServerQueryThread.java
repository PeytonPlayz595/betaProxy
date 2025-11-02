package dev.colbster937.utils;

import java.net.SocketException;

public class ServerQueryThread extends Thread {
  private final boolean verifyResponsePacket;

  private volatile boolean running = true;
  private volatile boolean send = false;

  public ServerQueryThread(boolean verifyResponsePacket) {
    super("ServerQueryThread");
    this.verifyResponsePacket = verifyResponsePacket;
  }

  @Override
  public void run() {
    while (running) {
      try {
        if (send)
          send = false;
        try {
          ServerQueryUtils.pingBackend(this.verifyResponsePacket);
        } catch (SocketException e) {
        } catch (Throwable t) {
          t.printStackTrace();
        }

        synchronized (this) {
          this.wait(5000L);
        }
      } catch (InterruptedException e) {
        this.running = false;
      }
    }
  }

  public void shutdown() {
    this.running = false;
    this.interrupt();
  }

  public void send() {
    synchronized (this) {
      this.send = true;
      this.notify();
    }
  }
}
