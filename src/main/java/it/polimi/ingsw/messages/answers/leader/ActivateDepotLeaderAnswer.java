package it.polimi.ingsw.messages.answers.leader;

import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.server.model.cards.Deck;
import it.polimi.ingsw.server.model.cards.leader.DepotLeaderCard;

public class ActivateDepotLeaderAnswer extends Answer {
    private final int leaderId;

    public ActivateDepotLeaderAnswer(int gameId, int playerId, int leaderId) {
        super(gameId, playerId);
        this.leaderId=leaderId;
    }

    public int getLeaderId() {
        return leaderId;
    }

}
