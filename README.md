# Beta Proxy

Beta Proxy is a custom TCP to WebSocket proxy I built specifically for all of my Golden Age Minecraft web ports.

This project does not contain any of Minecraft's intellectual property, the only code used in this project from Eaglercraft is lax's LOG4J port because I already had that downloaded and I was too lazy to add the actual log4j dependency to the repo even tho it would have literally taken like less than 30 seconds to do. However, this project does use Minecraft's networking protocol (which is publicly documented btw) for a bunch of old alpha/beta versions. 

**This proxy does not manipulate the contents of any packets, it only reads packet data and pieces together packet fragments from the server to be sent over WebSockets. This proxy was not made with the intent of performing any malicious acts and does not purposely allow someone to do such.**

# Why is this a thing?

For all of my old ports, people were stuck using a raw TCP to WebSocket proxy. TCP has no concept of frames, so packets would be sent to the client in fragments and they would have to be pieced together client-side. This was horrible for performance and would sometimes break. Beta Proxy pieces together the packet fragments before they are sent to the client to ensure a full packet is sent for every frame. This eliminates the need for clients to have to peice them together which will greatly improve client-side performance and reduces the chances of an error.

Beta Proxy also supports IP banning directly from the proxy since it does not have IP forwarding yet which makes it impossible to ban people from the minecraft server directly, IP forwarding will be added in the near future.

# Will this ever be useful?

Yes, as of right now only one of my ports uses this but Beta Proxy supports multiple PVNs and it's relatively easy to add support for new versions so hopefully it can be used for more ports in the future.

**Note: This will most likely become depreciated whenever I decide to make an EaglerXRewind-based protocol translator**

# How do I use this?

### Running
  - Running the server is extremely simple and self explanitory. Make sure you have at least Java 8 installed, download the latest JAR from [releases](https://github.com/PeytonPlayz595/betaProxy/releases) and run the jar file from within a terminal `java -jar betaProxy.jar` 

### Server config (`server.properties`):
  - `minecraft_host`: The IP address of the Minecraft server you wish to proxy to a WebSocket connection.
  - `minecraft_pvn`: The Protocol Version Number the Minecraft server uses.
  - `websocket_host`: The IP address where the WebSocket is hosted (This is what eagler players use to connect to the server).
  - `whitelist_enabled`: Decides if the proxy uses a whitelist or not
  - `timeout`: The amount of time (in seconds) the proxy goes without receiving data before it times out the connection (must be in rage of 5-60 seconds)

Note: I recommend using the default timeout which is 15 seconds

# Server PVN

The server properties file has to have a "minecraft_pvn" value set. The Server PVN is essentially the protocol version number that the client/server uses, it is an integer used to check for incompatibilities between the client and the server. This proxy needs to know what the PVN is because it fully reads each packet received and it uses the packet classes from the Minecraft client to do so. Packets are different for each Minecraft version so if it doesn't know what the PVN is then it will use the wrong packet classes and read the wrong data from the packet.

# Is my server supported?

If you are unaware of what PVNs this proxy supports or what PVN your Minecraft server uses then look [here](PVN_MAPPINGS.txt) for a list of supported versions along with what PVN they use.
