package it.polimi.ingsw.messages.answers.leaderanswer;

import it.polimi.ingsw.server.model.cards.DevelopCard;
import it.polimi.ingsw.server.model.game.Resource;

import java.util.TreeMap;

public class ActivateDiscountLeaderAnswer extends LeaderAnswer {
    /**
     * contains the cost of the top card
     */
    private final TreeMap<Resource,Integer>[][] newCosts;


    public ActivateDiscountLeaderAnswer(int gameId, int playerId, int leaderId, TreeMap<Resource,Integer>[][] newCosts) {
        super(gameId, playerId, leaderId);
        this.newCosts=newCosts;
    }

}
