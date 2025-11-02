# Beta Proxy

Beta Proxy is a custom TCP <--> WebSocket bridge built specifically for Minecraft Golden Age web ports.

This project does not contain any of Minecraft's intellectual property. This project does use the legacy Minecraft networking protocol. 

> [!WARNING]
> **This proxy does not manipulate the contents of any packets. It reads packet data and pieces together fragments from the server to be sent over WebSockets. This proxy does not perform any malicious acts and was not made with the intent of performing any malicious acts.**

# Why was this made?

Older Minecraft browser ports used a raw TCP to WebSocket proxy. TCP has no concept of frames, so packets would be sent to the client in fragments and they would have to be pieced together client-side. This was horrible for performance and would sometimes break. Beta Proxy pieces together the packet fragments before they are sent to the client to ensure a full packet is sent for every frame. This eliminates the need for clients to have to peice them together which will greatly improve client-side performance and reduces the chances of an error.

# How do I use this?

### Running
  - Running the server is extremely simple and self explanitory. Make sure you have at least Java 8 installed, download the latest JAR from [releases](https://github.com/PeytonPlayz595/betaProxy/releases), and run the jar file from within a terminal `java -jar betaProxy.jar`

### Server config (`server.properties`):
  - `minecraft_host`: The IP address of the Minecraft server you wish to proxy to a WebSocket connection..
  - `minecraft_pvn`: The Protocol Version Number the Minecraft server uses.
  - `websocket_host`: The IP address where the WebSocket is hosted.
  - `whitelist_enabled`: Decides if the proxy uses a whitelist or not.
  - `timeout`: The amount of time in seconds the proxy goes without receiving data before it times out the connection.
  - `experimental_auto_detect_pvn`: Decides whether the proxy should enable the experiemental auto pvn detection.
  - `experimental_ip_forwarding`: Decides whether the proxy should enable the experiemental proxy-protocol based ip forwarding.

> [!NOTE]
> It is recommended to use the default timeout of 15 seconds

### Server PVN

The server properties file has to have a "minecraft_pvn" value set. The Server PVN is essentially the protocol version number that the client/server uses. This proxy needs to know what the PVN is as it uses the packet classes from the Minecraft client to fully read each packet received. Packets are different for each Minecraft version so if it doesn't know what the PVN is then it will use the wrong packet classes and read the wrong data.

# Is my server supported?

If you are unaware of what PVNs this proxy supports or what PVN your Minecraft server uses then look [here](PVN_MAPPINGS.txt) for a list of supported versions along with what PVN they use.
