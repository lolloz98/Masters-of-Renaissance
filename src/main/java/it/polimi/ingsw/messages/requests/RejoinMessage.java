package it.polimi.ingsw.messages.requests;

/**
 * If the server encounters shuts down, the players can continue the game by sending this message
 */
public class RejoinMessage extends ClientMessage{
    public RejoinMessage(int gameId, int playerId) {
        super(gameId, playerId);
    }
}
