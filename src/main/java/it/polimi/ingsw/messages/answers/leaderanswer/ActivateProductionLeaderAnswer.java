package it.polimi.ingsw.messages.answers.leaderanswer;

import it.polimi.ingsw.client.localmodel.localcards.LocalLeaderCard;
import it.polimi.ingsw.messages.answers.Answer;

public class ActivateProductionLeaderAnswer extends LeaderAnswer {
    private final int whichLeaderProd;

    public ActivateProductionLeaderAnswer(int gameId, int playerId, LocalLeaderCard leader, int whichLeaderProd) {
        super(gameId, playerId,leader);
        this.whichLeaderProd=whichLeaderProd;
    }

    public int getWhichLeaderProd() {
        return whichLeaderProd;
    }
}
