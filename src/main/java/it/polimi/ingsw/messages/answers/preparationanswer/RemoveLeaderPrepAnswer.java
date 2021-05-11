package it.polimi.ingsw.messages.answers.preparationanswer;

import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.server.model.cards.leader.LeaderCard;

import java.util.ArrayList;

public class RemoveLeaderPrepAnswer extends Answer {
    private final ArrayList<LeaderCard<?>> toRemove;

    public RemoveLeaderPrepAnswer(int gameId, int playerId, ArrayList<LeaderCard<?>> toRemove) {
        super(gameId,playerId);
        this.toRemove = toRemove;
    }

    public ArrayList<LeaderCard<?>> getToRemove() {
        return new ArrayList<>(toRemove);
    }
}
