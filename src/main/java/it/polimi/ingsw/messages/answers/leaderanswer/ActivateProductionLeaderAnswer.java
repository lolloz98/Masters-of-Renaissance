package it.polimi.ingsw.messages.answers.leaderanswer;

import it.polimi.ingsw.messages.answers.Answer;

public class ActivateProductionLeaderAnswer extends LeaderAnswer {
    private final int whichLeaderProd;

    public ActivateProductionLeaderAnswer(int gameId, int playerId, int leaderId, int whichLeaderProd) {
        super(gameId, playerId,leaderId);
        this.whichLeaderProd=whichLeaderProd;
    }

    public int getWhichLeaderProd() {
        return whichLeaderProd;
    }
}
