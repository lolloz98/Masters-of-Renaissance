package it.polimi.ingsw.messages.answers.leaderanswer;

import it.polimi.ingsw.messages.answers.Answer;

public class LeaderAnswer extends Answer {
    private final int leaderId;

    public LeaderAnswer(int gameId, int playerId, int leaderId) {
        super(gameId, playerId);
        this.leaderId = leaderId;
    }

    public int getLeaderId() {
        return leaderId;
    }
}
