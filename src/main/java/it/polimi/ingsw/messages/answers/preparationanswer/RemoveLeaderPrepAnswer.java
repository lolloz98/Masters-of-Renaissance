package it.polimi.ingsw.messages.answers.preparationanswer;

import it.polimi.ingsw.client.localmodel.LocalGameState;
import it.polimi.ingsw.client.localmodel.localcards.LocalLeaderCard;
import it.polimi.ingsw.messages.answers.Answer;

import java.util.ArrayList;

public class RemoveLeaderPrepAnswer extends Answer {
    private static final long serialVersionUID = 41L;

    private final ArrayList<Integer> removedLeaderIds;
    private final LocalGameState state;

    public RemoveLeaderPrepAnswer(int gameId, int playerId, ArrayList<Integer> removedLeaderId, LocalGameState state) {
        super(gameId,playerId);
        this.removedLeaderIds = removedLeaderId;
        this.state = state;
    }

    public ArrayList<Integer> getRemovedLeaderIds() {
        return new ArrayList<>(removedLeaderIds);
    }

    public LocalGameState getState() {
        return state;
    }
}
