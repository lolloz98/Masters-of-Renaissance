package it.polimi.ingsw.client.localmodel.localcards;

import it.polimi.ingsw.enums.Resource;

public class LocalDepotLeader extends LocalLeaderCard{
    private final Resource resType;
    private final Resource resRequirement;
    private int numberOfRes;
    private final int reqQuantity;

    public LocalDepotLeader(int id, int victoryPoints, boolean isActive, boolean isDiscarded, Resource resType, Resource resRequirement, int reqQuantity) {
        super(id, victoryPoints, isActive, isDiscarded);
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
        notifyObservers();
    }

    public Resource getResType() {
        return resType;
    }

    public Resource getResRequirement() {
        return resRequirement;
    }
}
