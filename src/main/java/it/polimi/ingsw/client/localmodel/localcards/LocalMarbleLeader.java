package it.polimi.ingsw.client.localmodel.localcards;

import it.polimi.ingsw.server.model.cards.Color;
import it.polimi.ingsw.server.model.game.Resource;
import java.util.TreeMap;

public class LocalMarbleLeader extends LocalLeaderCard{
    private Resource marbleResource;
    private TreeMap<Color,Integer> prodRequirement;

    public LocalMarbleLeader(int victoryPoints, Resource marbleResource, TreeMap<Color, Integer> prodRequirement) {
        super(victoryPoints);
        this.marbleResource = marbleResource;
        this.prodRequirement = prodRequirement;
    }

    public synchronized Resource getMarbleResource() {
        return marbleResource;
    }

    public synchronized void setMarbleResource(Resource marbleResource) {
        this.marbleResource = marbleResource;
    }

    public synchronized TreeMap<Color, Integer> getProdRequirement() {
        return prodRequirement;
    }

    public synchronized void setProdRequirement(TreeMap<Color, Integer> prodRequirement) {
        this.prodRequirement = prodRequirement;
    }
}
