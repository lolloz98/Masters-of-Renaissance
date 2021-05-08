package it.polimi.ingsw.messages.answers;

import it.polimi.ingsw.server.model.cards.leader.LeaderCard;

import java.util.ArrayList;

public class DiscardLeaderPrepAnswer extends Answer{
    private final ArrayList<LeaderCard> toRemove;

    public DiscardLeaderPrepAnswer(int gameId, int playerId, ArrayList<LeaderCard> toRemove) {
        super(gameId,playerId);
        this.toRemove = toRemove;
    }

    public ArrayList<LeaderCard> getToRemove() {
        return new ArrayList<>(toRemove);
    }
}
