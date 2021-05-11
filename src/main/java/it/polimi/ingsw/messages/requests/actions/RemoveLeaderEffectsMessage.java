package it.polimi.ingsw.messages.requests.actions;

import it.polimi.ingsw.messages.requests.ClientMessage;

public class RemoveLeaderEffectsMessage extends ClientMessage {
    public RemoveLeaderEffectsMessage(int gameId, int playerId) {
        super(gameId, playerId);
    }
}
