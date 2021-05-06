package it.polimi.ingsw.messages.requests;
@Deprecated
public abstract class ServerMessage {
    private int playerId;

    public ServerMessage(int playerId) {
        this.playerId = playerId;
    }

    public int getPlayerId() {
        return playerId;
    }
}
