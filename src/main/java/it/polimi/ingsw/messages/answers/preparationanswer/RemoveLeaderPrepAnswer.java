package it.polimi.ingsw.messages.answers.preparationanswer;

import it.polimi.ingsw.client.localmodel.LocalGameState;
import it.polimi.ingsw.client.localmodel.localcards.LocalCard;
import it.polimi.ingsw.client.localmodel.localcards.LocalLeaderCard;
import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.server.model.cards.leader.LeaderCard;

import java.util.ArrayList;

public class RemoveLeaderPrepAnswer extends Answer {
    private final ArrayList<LocalLeaderCard> toRemove;
    private final LocalGameState state;

    public RemoveLeaderPrepAnswer(int gameId, int playerId, ArrayList<LocalLeaderCard> toRemove, LocalGameState state) {
        super(gameId,playerId);
        this.toRemove = toRemove;
        this.state = state;
    }

    public ArrayList<LocalLeaderCard> getToRemove() {
        return new ArrayList<>(toRemove);
    }

    public LocalGameState getState() {
        return state;
    }
}
