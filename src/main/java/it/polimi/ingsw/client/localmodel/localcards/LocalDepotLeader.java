package it.polimi.ingsw.client.localmodel.localcards;

import it.polimi.ingsw.server.model.cards.Color;
import it.polimi.ingsw.server.model.game.Resource;

import java.util.TreeMap;

public class LocalDepotLeader extends LocalLeaderCard{
    private Resource resType;
    private TreeMap<Color,Integer> prodRequirement;

    public LocalDepotLeader(int victoryPoints, Resource resType, TreeMap<Color, Integer> prodRequirement) {
        super(victoryPoints);
        this.resType = resType;
        this.prodRequirement = prodRequirement;
    }

    public synchronized Resource getResType() {
        return resType;
    }

    public synchronized void setResType(Resource resType) {
        this.resType = resType;
    }

    public synchronized TreeMap<Color, Integer> getProdRequirement() {
        return prodRequirement;
    }

    public synchronized void setProdRequirement(TreeMap<Color, Integer> prodRequirement) {
        this.prodRequirement = prodRequirement;
    }
}
