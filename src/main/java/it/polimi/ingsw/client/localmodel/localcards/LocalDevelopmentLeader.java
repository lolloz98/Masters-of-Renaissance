package it.polimi.ingsw.client.localmodel.localcards;

import it.polimi.ingsw.server.model.cards.Color;
import it.polimi.ingsw.server.model.game.Resource;
import java.util.TreeMap;

public class LocalDevelopmentLeader extends LocalLeaderCard{
    private TreeMap<Resource, Integer> resToGive;
    private TreeMap<Resource, Integer> resToGain;
    private Color colorRequirement;
    private int numberOfRes;

    public LocalDevelopmentLeader(int id, int victoryPoints, TreeMap<Resource, Integer> resToGive, TreeMap<Resource, Integer> resToGain, Color colorRequirement) {
        super(id, victoryPoints);
        this.resToGive = resToGive;
        this.resToGain = resToGain;
        this.colorRequirement = colorRequirement;
    }

    public synchronized int getNumberOfRes() {
        return numberOfRes;
    }

    public synchronized void setNumberOfRes(int numberOfRes) {
        this.numberOfRes = numberOfRes;
    }

    public synchronized TreeMap<Resource, Integer> getToGive() {
        return resToGive;
    }

    public synchronized void setToGive(TreeMap<Resource, Integer> toGive) {
        this.resToGive = toGive;
    }

    public synchronized TreeMap<Resource, Integer> getToGain() {
        return resToGain;
    }

    public synchronized void setToGain(TreeMap<Resource, Integer> toGain) {
        this.resToGain = toGain;
    }

    public synchronized Color getColorRequirement() {
        return colorRequirement;
    }

    public synchronized void setColorRequirement(Color colorRequirement) {
        this.colorRequirement = colorRequirement;
    }
}
