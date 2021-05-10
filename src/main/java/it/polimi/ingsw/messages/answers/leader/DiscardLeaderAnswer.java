package it.polimi.ingsw.messages.answers.leader;

import it.polimi.ingsw.messages.answers.Answer;

public class DiscardLeaderAnswer extends Answer {

    private final int leaderId;

    public DiscardLeaderAnswer(int gameId, int playerId, int leaderId) {
        super(gameId, playerId);
        this.leaderId = leaderId;
    }




}
