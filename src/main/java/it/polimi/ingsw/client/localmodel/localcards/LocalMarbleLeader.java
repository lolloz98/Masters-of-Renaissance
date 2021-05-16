package it.polimi.ingsw.client.localmodel.localcards;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.Resource;
import java.util.TreeMap;

public class LocalMarbleLeader extends LocalLeaderCard{
    private final Resource marbleResource;
    private final TreeMap<Color,Integer> prodRequirement;

    public LocalMarbleLeader(int id, int victoryPoints, boolean isActive, boolean isDiscarded, Resource marbleResource, TreeMap<Color, Integer> prodRequirement) {
        super(id, victoryPoints, isActive, isDiscarded);
        this.marbleResource = marbleResource;
        this.prodRequirement = prodRequirement;
    }

    public Resource getMarbleResource() {
        return marbleResource;
    }

    public TreeMap<Color, Integer> getProdRequirement() {
        return prodRequirement;
    }
}
