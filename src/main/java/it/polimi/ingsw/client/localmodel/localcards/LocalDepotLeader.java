package it.polimi.ingsw.client.localmodel.localcards;

import it.polimi.ingsw.server.model.game.Resource;

public class LocalDepotLeader extends LocalLeaderCard{
    private final Resource resType;
    private final Resource resRequirement;
    private int numberOfRes;
    private final int reqQuantity;

    public LocalDepotLeader(int id, int victoryPoints, Resource resType, Resource resRequirement, int reqQuantity) {
        super(id, victoryPoints);
        this.resType = resType;
        this.resRequirement = resRequirement;
        this.reqQuantity = reqQuantity;
    }

    public int getReqQuantity() {
        return reqQuantity;
    }

    public synchronized int getNumberOfRes() {
        return numberOfRes;
    }

    public synchronized void setNumberOfRes(int numberOfRes) {
        this.numberOfRes = numberOfRes;
        notifyObserver();
    }

    public Resource getResType() {
        return resType;
    }

    public Resource getResRequirement() {
        return resRequirement;
    }
}
