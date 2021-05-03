package it.polimi.ingsw.server.requests.leader;

import it.polimi.ingsw.server.requests.ClientMessage;

/**
 * Request that involves a leaderCard
 */
public abstract class LeaderMessage extends ClientMessage {
    private static final long serialVersionUID = 103L;

    private final int leaderId;

    public LeaderMessage(int gameId, int playerId, int leaderId) {
        super(gameId, playerId);
        this.leaderId = leaderId;
    }

    public int getLeaderId() {
        return leaderId;
    }
}
