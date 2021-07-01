# Description Sequence Diagram
## Create Game Multiplayer
- The client connects to the server and the server grants the connection
- The client sends a request to create a new game, specifying the number of players (this numbers is not 1) and his/her nickname.
    - The server sends back to the client a CreateGameAnswer with the id of the created game (actually the game is not created yet, but its controllerAction is created) and an id to identify the player
    - The server sends back an ErrorAnswer: something went wrong (the creation of the game was not possible)
    
## Create Game SinglePlayer
- The client connects to the server and the server grants the connection
- The client sends a request to create a new game, specifying the number of players (this number is 1) and his/her nickname.
    - The server sends back to the client a GameStatusAnswer with the id of the created game, an id to identify the player and the just created game
    - The server sends back an ErrorAnswer: something went wrong (the creation of the game was not possible)

## Join Game
- The client connects to the server and the server grants the connection
- The client sends a request to join a game, specifying the game_id and his/her nickname:
    - If it is not the last player which should join: the server sends back **to all the clients** connected for that game a JoinGameAnswer with the names and ids of all the players already connected (there is also the new client in it, associated to its new id), the id of the just added player and the id of the game
    - If it is the last player which should join (the number of players reach the specified number of players for the game): the server sends back **to all the clients** connected for that game a GameStatusAnswer with the just created game, the id of the game and the id of the just added player
    - Else, the server sends back to the client an ErrorAnswer: something went wrong
