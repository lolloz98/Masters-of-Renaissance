package it.polimi.ingsw.messages.answers.leaderanswer;

import it.polimi.ingsw.client.localmodel.localcards.LocalLeaderCard;
import it.polimi.ingsw.server.model.cards.DevelopCard;
import it.polimi.ingsw.server.model.game.Resource;

import java.util.TreeMap;

public class ActivateDiscountLeaderAnswer extends LeaderAnswer {
    /**
     * contains the cost of the top card
     */
    private final TreeMap<Resource,Integer>[][] newCosts;


    public ActivateDiscountLeaderAnswer(int gameId, int playerId, LocalLeaderCard leader, TreeMap<Resource,Integer>[][] newCosts) {
        super(gameId, playerId, leader);
        this.newCosts=newCosts;
    }

}
