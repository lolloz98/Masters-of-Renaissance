package it.polimi.ingsw.server.requests.leader;

/**
 * Request to remove a leader card at the beginning of the game
 */
public class RemoveLeaderMessage extends LeaderMessage{
    private static final long serialVersionUID = 104L;

    public RemoveLeaderMessage(int gameId, int playerId, int leaderId) {
        super(gameId, playerId, leaderId);
    }
}
