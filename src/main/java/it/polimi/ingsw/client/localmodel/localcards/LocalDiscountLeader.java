package it.polimi.ingsw.client.localmodel.localcards;

import it.polimi.ingsw.server.model.cards.Color;
import it.polimi.ingsw.server.model.game.Resource;

import java.util.TreeMap;

public class LocalDiscountLeader extends LocalLeaderCard{
    private Resource discountedRes;
    private TreeMap<Color,Integer> prodRequirement;

    public LocalDiscountLeader(int id, int victoryPoints, Resource resRequirement, Resource discountedRes) {
        super(id, victoryPoints);
        this.discountedRes = discountedRes;
        this.prodRequirement = prodRequirement;
    }


    public synchronized Resource getDiscountedRes() {
        return discountedRes;
    }

    public synchronized void setDiscountedRes(Resource discountedRes) {
        this.discountedRes = discountedRes;
    }

    public synchronized TreeMap<Color, Integer> getProdRequirement() {
        return prodRequirement;
    }

    public synchronized void setProdRequirement(TreeMap<Color, Integer> prodRequirement) {
        this.prodRequirement = prodRequirement;
    }
}