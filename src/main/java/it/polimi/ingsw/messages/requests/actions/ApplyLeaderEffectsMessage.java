package it.polimi.ingsw.messages.requests.actions;

import it.polimi.ingsw.messages.requests.ClientMessage;

public class ApplyLeaderEffectsMessage extends ClientMessage {
    public ApplyLeaderEffectsMessage(int gameId, int playerId) {
        super(gameId, playerId);
    }
}
