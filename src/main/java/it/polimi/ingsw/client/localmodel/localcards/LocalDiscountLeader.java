package it.polimi.ingsw.client.localmodel.localcards;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.Resource;

import java.util.TreeMap;

public class LocalDiscountLeader extends LocalLeaderCard{
    private final Resource discountedRes;
    private final TreeMap<Color,Integer> prodRequirement;
    private final int quantityToDiscount;

    public LocalDiscountLeader(int id, int victoryPoints, boolean isActive, boolean isDiscarded, TreeMap<Color,Integer> prodRequirement, Resource discountedRes, int quantityToDiscount) {
        super(id, victoryPoints, isActive, isDiscarded);
        this.discountedRes = discountedRes;
        this.prodRequirement = prodRequirement;
        this.quantityToDiscount = quantityToDiscount;
    }

    public Resource getDiscountedRes() {
        return discountedRes;
    }

    public TreeMap<Color, Integer> getProdRequirement() {
        return prodRequirement;
    }

    public int getQuantityToDiscount() {
        return quantityToDiscount;
    }
}