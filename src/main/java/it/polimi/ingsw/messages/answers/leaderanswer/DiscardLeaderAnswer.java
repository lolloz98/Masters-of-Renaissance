package it.polimi.ingsw.messages.answers.leaderanswer;

import it.polimi.ingsw.client.localmodel.localcards.LocalLeaderCard;
import it.polimi.ingsw.messages.answers.Answer;

public class DiscardLeaderAnswer extends LeaderAnswer {

    public DiscardLeaderAnswer(int gameId, int playerId, LocalLeaderCard leader) {
        super(gameId, playerId,leader);
    }

}
