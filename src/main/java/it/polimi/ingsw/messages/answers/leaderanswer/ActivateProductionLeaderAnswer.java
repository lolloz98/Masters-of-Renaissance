package it.polimi.ingsw.messages.answers.leaderanswer;

import it.polimi.ingsw.client.localmodel.localcards.LocalLeaderCard;
import it.polimi.ingsw.messages.answers.Answer;

public class ActivateProductionLeaderAnswer extends LeaderAnswer {

    public ActivateProductionLeaderAnswer(int gameId, int playerId, LocalLeaderCard leader) {
        super(gameId, playerId,leader);
    }

}
