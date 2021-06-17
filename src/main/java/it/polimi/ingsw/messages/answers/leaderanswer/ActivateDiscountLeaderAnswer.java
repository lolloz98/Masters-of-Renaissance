package it.polimi.ingsw.messages.answers.leaderanswer;

import it.polimi.ingsw.client.localmodel.LocalDevelopmentGrid;
import it.polimi.ingsw.client.localmodel.localcards.LocalLeaderCard;

public class ActivateDiscountLeaderAnswer extends LeaderAnswer {
    /**
     * contains the cost of the top cards to update
     */
    private final LocalDevelopmentGrid localGrid;


    public ActivateDiscountLeaderAnswer(int gameId, int playerId, LocalLeaderCard leader, LocalDevelopmentGrid localGrid) {
        super(gameId, playerId, leader);
        this.localGrid=localGrid;
    }

    public LocalDevelopmentGrid getLocalGrid() {
        return localGrid;
    }
}
