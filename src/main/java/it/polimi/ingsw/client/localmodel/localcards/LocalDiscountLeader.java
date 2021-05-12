package it.polimi.ingsw.client.localmodel.localcards;

import it.polimi.ingsw.server.model.game.Resource;

public class LocalDiscountLeader extends LocalLeaderCard{
    private Resource resRequirement;
    private Resource discountedRes;

    public LocalDiscountLeader(int id, int victoryPoints, Resource resRequirement, Resource discountedRes) {
        super(id, victoryPoints);
        this.resRequirement = resRequirement;
        this.discountedRes = discountedRes;
    }

    public synchronized Resource getResRequirement() {
        return resRequirement;
    }

    public synchronized void setResRequirement(Resource resRequirement) {
        this.resRequirement = resRequirement;
    }

    public synchronized Resource getDiscountedRes() {
        return discountedRes;
    }

    public synchronized void setDiscountedRes(Resource discountedRes) {
        this.discountedRes = discountedRes;
    }
}
