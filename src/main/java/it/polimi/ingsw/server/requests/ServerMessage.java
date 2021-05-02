package it.polimi.ingsw.server.requests;

public abstract class ServerMessage {
    private int playerId;

    public ServerMessage(int playerId) {
        this.playerId = playerId;
    }

    public int getPlayerId() {
        return playerId;
    }
}
