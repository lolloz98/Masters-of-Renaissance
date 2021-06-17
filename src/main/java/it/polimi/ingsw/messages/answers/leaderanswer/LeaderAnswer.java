package it.polimi.ingsw.messages.answers.leaderanswer;

import it.polimi.ingsw.client.localmodel.localcards.LocalCard;
import it.polimi.ingsw.client.localmodel.localcards.LocalLeaderCard;
import it.polimi.ingsw.messages.answers.Answer;

public class LeaderAnswer extends Answer {
    /**
     * leader card to update
     */
    private final LocalCard leader;

    public LeaderAnswer(int gameId, int playerId, LocalCard leader) {
        super(gameId, playerId);
        this.leader=leader;
    }

    public LocalCard getLeader() {
        return leader;
    }
}
