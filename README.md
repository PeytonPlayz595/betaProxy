# Beta Proxy

Beta Proxy is a custom TCP to WebSocket proxy I built specifically for my web port of Eaglercraft Beta 1.1_02.

# Why is this a thing?

For all of my old ports, people were stuck using a raw TCP to WebSocket proxy. TCP has no concept of frames, so packets would be send to the client in fragments and would have to be pieced together client-side. This was horrible for performance and would sometimes break. Beta Proxy pieces together the packet fragments before they are sent to the client to ensure a full packet is sent for every frame. This eliminates the need to buffer and keep re-reading packet fragments until a full packet is recieved. This greatly improves client-side performance and reduces the chances of an error.

Beta Proxy also has packet compression to reduce the size of packets being sent over the network. This is extremely helpful, especially with people hosting on slower network connections.

# Will this ever be useful?

Right now it's only useful if you plan on hosting a Eaglercraft Beta 1.1_02 server, currently Beta Proxy only supports Beta 1.1_02 but in the near future I plan on adding support for some of my other web ports like Alpha v1.2.6 and Beta 1.7.3
I also plan on adding a way to "intercept" and change packet data, adding the ability to make plugins specifically for eagler players.

# How do I use this?

### Running
  - Running the server is extremely simple and self explanitory, you need at least Java 11 installed and you just run the jar file from a terminal `java -jar betaProxy.jar` 

### Server Config (`server_properties.txt`):
  - `minecraft-address`: The IP address of the Minecraft server you wish to proxy to a WebSocket connection, default value is `0.0.0.0:25565`
  - `websocket-address`: The IP address where the WebSocket is hosted (This is what eagler players use to connect to the server), default value is `0.0.0.0:8080`
  - `enable-compression`: Enables packet compression (if supported by the client) this reduces the size of the packets sent over the network, default value is `true`
  - `enable-whitelist`: enables a whitelist for the proxy, if enabled only whitelisted players can join using the proxy, default value is `false`

### Server commands:
  - `ban`: bans an player by username
  - `ipban`: bans a player by ip
  - `kick`: kicks a player by username from the server
  - `whitelist`: Adds a player by username to the whitelist
  - `whitelist_ip`: Adds a player by IP address to the whitelist
