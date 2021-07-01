package it.polimi.ingsw.messages.requests.leader;

/**
 * Request to activate a leaderCard
 */
public class ActivateLeaderMessage extends LeaderMessage{
    private static final long serialVersionUID = 52L;

    public ActivateLeaderMessage(int gameId, int playerId, int leaderId) {
        super(gameId, playerId, leaderId);
    }
}
