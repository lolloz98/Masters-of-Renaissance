package it.polimi.ingsw.messages.requests.leader;

import it.polimi.ingsw.messages.requests.ClientMessage;

/**
 * Request that involves a leaderCard
 */
public abstract class LeaderMessage extends ClientMessage {
    private static final long serialVersionUID = 54L;

    private final int leaderId;

    public LeaderMessage(int gameId, int playerId, int leaderId) {
        super(gameId, playerId);
        this.leaderId = leaderId;
    }

    public int getLeaderId() {
        return leaderId;
    }
}
