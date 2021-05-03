package it.polimi.ingsw.messages.requests.leader;

/**
 * Request to discard leader
 */
public class DiscardLeaderMessage extends LeaderMessage {
    private static final long serialVersionUID = 105L;

    public DiscardLeaderMessage(int gameId, int playerId, int leaderId) {
        super(gameId, playerId, leaderId);
    }
}
