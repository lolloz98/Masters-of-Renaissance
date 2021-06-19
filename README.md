# ing-sw-2021-Carpaneto-DeSantis-Innocenti
## Group members
Lorenzo Carpaneto, Aniello De Santis, Lorenzo Innocenti

## Implemented features
|Feature|Description|
---|---
|Complete rule set|The game works for both the single-player and multi-player mode (up to 4 players)|
|Multiple games|It is possible to play multiple games simultaneously. To each game is associated a gameId: to join a game the player needs to specify its gameId.|
|Persistence|The game status is saved on the disk. If, during a game, the server should, for some reasons, shutdown, the players (if they do not close the application) will be able to rejoin the game when the server will start running again|
|Local game|It is possible to play a single-player game without connecting to the server|

All the features are implemented for both **CLI** and **GUI**.

## How to execute jars
### Client - CLI
Execute from wsl or linux terminal with command: `java -jar <path_to_cli.jar>`.

If shell is opened in the folder containing cli.jar:
```
java -jar ./cli.jar
```

### Client - GUI
Execute from shell (tested option: cmd) with command: `java -jar <path_to_gui.jar>`.

If shell is opened in the folder containing gui.jar:
```
java -jar gui.jar
```

### Server
Execute from shell (tested option: cmd) with command: `java -jar <path_to_server.jar>`.

If shell is opened in the folder containing server.jar:
```
java -jar server.jar
```

If you want to specify the port on which to run the server, pass as argument the port number `java -jar <path_to_server.jar> <port_number>`. 

E.g. If shell is opened in the folder containing server.jar:
```
java -jar server.jar 44000
```

Otherwise the server will run on port: `16509`.

Please check to run the server on an available port, otherwise it will not start.
