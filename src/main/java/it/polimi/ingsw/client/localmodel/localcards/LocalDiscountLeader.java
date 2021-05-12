package it.polimi.ingsw.client.localmodel.localcards;

import it.polimi.ingsw.server.model.cards.Color;
import it.polimi.ingsw.server.model.game.Resource;

import java.util.TreeMap;

public class LocalDiscountLeader extends LocalLeaderCard{
    private final Resource discountedRes;
    private final TreeMap<Color,Integer> prodRequirement;

    public LocalDiscountLeader(int id, int victoryPoints, TreeMap<Color,Integer> prodRequirement, Resource discountedRes) {
        super(id, victoryPoints);
        this.discountedRes = discountedRes;
        this.prodRequirement = prodRequirement;
    }

    public Resource getDiscountedRes() {
        return discountedRes;
    }

    public TreeMap<Color, Integer> getProdRequirement() {
        return prodRequirement;
    }
}