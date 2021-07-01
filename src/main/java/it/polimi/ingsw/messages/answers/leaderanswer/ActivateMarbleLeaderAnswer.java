package it.polimi.ingsw.messages.answers.leaderanswer;

import it.polimi.ingsw.client.localmodel.localcards.LocalLeaderCard;

public class ActivateMarbleLeaderAnswer extends LeaderAnswer {
    private static final long serialVersionUID = 29L;

    public ActivateMarbleLeaderAnswer(int gameId, int playerId, LocalLeaderCard leader) {
        super(gameId, playerId,leader);
    }

}
