# Description Sequence Diagram
## Create Game
- The client connects to the server, and the server grants the connection
- The client sends a request to create a new game, specifying the number of players.
    - The server sends back to the client the id of the created game and an id to identify the player
    - The server sends back a failure message: something went wrong (the creation of the game was not possible)
    

## Join Game
- The client connects to the server, and the server grants the connection
- The client sends a request to join a game, specifying the game_id
    - The server sends back to the client the id to identify the player 
    - The server sends back a failure message: something went wrong

## Game Created
- When the number of players reaches the number specified in Create Game, the server sends to all clients (players of that game), the initialized game.
