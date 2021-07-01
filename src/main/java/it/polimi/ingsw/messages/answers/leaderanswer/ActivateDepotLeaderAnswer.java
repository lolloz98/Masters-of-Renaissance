package it.polimi.ingsw.messages.answers.leaderanswer;

import it.polimi.ingsw.client.localmodel.localcards.LocalLeaderCard;
import it.polimi.ingsw.messages.answers.Answer;

public class ActivateDepotLeaderAnswer extends LeaderAnswer {
    private static final long serialVersionUID = 27L;

    public ActivateDepotLeaderAnswer(int gameId, int playerId, LocalLeaderCard leader) {
        super(gameId, playerId,leader);
    }

}
