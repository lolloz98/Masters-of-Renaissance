package it.polimi.ingsw.client.localmodel.localcards;

import it.polimi.ingsw.enums.Resource;

public class LocalDepotLeader extends LocalLeaderCard{
    private final Resource resType;
    private final Resource resRequirement;
    private int numberOfRes;
    private final int reqQuantity;

    public LocalDepotLeader(int id, int victoryPoints, boolean isActive, boolean isDiscarded, Resource resType, int numberOfRes, Resource resRequirement, int reqQuantity) {
        super(id, victoryPoints, isActive, isDiscarded);
        this.resType = resType;
        this.resRequirement = resRequirement;
        this.reqQuantity = reqQuantity;
        this.numberOfRes = numberOfRes;
    }

    public int getReqQuantity() {
        return reqQuantity;
    }

    public synchronized int getNumberOfRes() {
        return numberOfRes;
    }

    public synchronized void setNumberOfRes(int numberOfRes) {
        this.numberOfRes = numberOfRes;
    }

    public Resource getResType() {
        return resType;
    }

    public Resource getResRequirement() {
        return resRequirement;
    }
}
