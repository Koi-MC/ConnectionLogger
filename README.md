# Connection Logger

### What is it?
ConnectionLogger is a simple plugin which aims to provide Bukkit/Spigot/Paper server admins with a set of quick, clean, and simple incoming player connection logs. It was originally designed on Minecraft 1.21 and will not be backported. There are no plans to port to Fabric or Forge.

### Why?
- Gameserver providers that do not operate as a VPS often will not let you run bash/python/whatever scripting languages are needed to parse huge Minecraft log dumps which contain way more than just incoming player connections
- Combat bot and griefer accounts via logging their connection attempts to whitelisted servers
- Get an easily readable overview of the total player traffic a server is experiencing

### Config
[config.yml](https://github.com/Koi-MC/ConnectionLogger/blob/master/src/main/resources/config.yml) contains the following adjustable variables:

```
timezone: "America/New_York"
printToConsole: true
logAllConnections: true
logAllConnectionsFileName: "CL_AllConnections.txt"
logNotWhitelisted: true
logNotWhitelistedFileName: "CL_NotWhitelisted.txt"
```
- Timezone displays what timezone you would like the connections to log as for easy of readability
- Print to Console allows you to toggle if the connection logger will print the connections to console or not
- Log All Connections will let you choose if you want every incoming connection logged to a file
- Log All Connections File Name is the log file which will store all incoming connection attempts
- Log Not Whitelisted will let you choose if you want to log all non-whitelisted player connection attempts to a different/second log file
- Log Not Whitelisted File Name is the log file which will store all connections made that are from non-whitelisted players

### Commands
By default, ONLY server Ops will have access to the logger commands:
```
/connectionlogger printToConsole <true/false>
/connectionlogger logAllConnections <true/false>
/connectionlogger logNotWhitelisted <true/false>
/connectionlogger reload
/connectionlogger version
```
These commands will change the true/false value of the config.yml, then reload the config for you, without having to restart the server.
Note that "/cl" is an alias which will work in place of "/connectionlogger"
