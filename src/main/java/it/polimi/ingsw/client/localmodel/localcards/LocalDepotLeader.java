package it.polimi.ingsw.client.localmodel.localcards;

import it.polimi.ingsw.server.model.cards.Color;
import it.polimi.ingsw.server.model.game.Resource;
import java.util.TreeMap;

public class LocalDepotLeader extends LocalLeaderCard{
    private Resource resType;
    private Resource resRequirement;
    private int numberOfRes;

    public LocalDepotLeader(int id, int victoryPoints, Resource resType, TreeMap<Color, Integer> prodRequirement) {
        super(id, victoryPoints);
        this.resType = resType;
        this.resRequirement = resRequirement;
    }

    public synchronized int getNumberOfRes() {
        return numberOfRes;
    }

    public synchronized void setNumberOfRes(int numberOfRes) {
        this.numberOfRes = numberOfRes;
    }

    public synchronized Resource getResType() {
        return resType;
    }

    public synchronized void setResType(Resource resType) {
        this.resType = resType;
    }

    public synchronized Resource getResRequirement() {
        return resRequirement;
    }

    public synchronized void setResRequirement(Resource resRequirement) {
        this.resRequirement = resRequirement;
    }

}
