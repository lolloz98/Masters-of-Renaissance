package it.polimi.ingsw.client.localmodel.localcards;

import it.polimi.ingsw.client.localmodel.LocalProduction;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.Resource;
import java.util.TreeMap;

public class LocalProductionLeader extends LocalLeaderCard{
    private static final long serialVersionUID = 8L;

    private final LocalProduction production;
    private final Color colorRequirement;
    private final Integer levelReq;
    private final int whichProd;

    public LocalProductionLeader(int id, int victoryPoints, boolean isActive, boolean isDiscarded, TreeMap<Resource, Integer> resToGive, TreeMap<Resource, Integer> resToGain, TreeMap<Resource, Integer> resToFlush, Color colorRequirement, Integer levelReq, int whichProd) {
        super(id, victoryPoints, isActive, isDiscarded);
        production = new LocalProduction(resToGive, resToGain, resToFlush);
        this.colorRequirement = colorRequirement;
        this.levelReq = levelReq;
        this.whichProd = whichProd;
    }

    public synchronized void setResToFlush(TreeMap<Resource, Integer> resToFlush) {
        this.production.setResToFlush(resToFlush);
    }

    public Color getColorRequirement() {
        return colorRequirement;
    }

    public Integer getLevelReq() {
        return levelReq;
    }

    public synchronized LocalProduction getProduction() {
        return production;
    }

    public int getWhichProd() {
        return whichProd;
    }
}
