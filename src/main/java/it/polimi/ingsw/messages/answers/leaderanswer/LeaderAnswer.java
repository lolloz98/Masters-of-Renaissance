package it.polimi.ingsw.messages.answers.leaderanswer;

import it.polimi.ingsw.client.localmodel.localcards.LocalLeaderCard;
import it.polimi.ingsw.messages.answers.Answer;

public class LeaderAnswer extends Answer {
    private final LocalLeaderCard leader;

    public LeaderAnswer(int gameId, int playerId, LocalLeaderCard leader) {
        super(gameId, playerId);
        this.leader=leader;
    }

    public LocalLeaderCard getLeader() {
        return leader;
    }
}
