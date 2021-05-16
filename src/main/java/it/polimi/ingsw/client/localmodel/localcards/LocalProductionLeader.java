package it.polimi.ingsw.client.localmodel.localcards;

import it.polimi.ingsw.client.localmodel.LocalProduction;
import it.polimi.ingsw.server.model.cards.Color;
import it.polimi.ingsw.server.model.game.Resource;
import java.util.TreeMap;

public class LocalProductionLeader extends LocalLeaderCard{
    private final LocalProduction production;
    private final Color colorRequirement;
    private final Integer levelReq;

    public LocalProductionLeader(int id, int victoryPoints, boolean isActive, boolean isDiscarded, TreeMap<Resource, Integer> resToGive, TreeMap<Resource, Integer> resToGain, TreeMap<Resource, Integer> resToFlush, Color colorRequirement, Integer levelReq) {
        super(id, victoryPoints, isActive, isDiscarded);
        production = new LocalProduction(resToGive, resToGain, resToFlush);
        this.colorRequirement = colorRequirement;
        this.levelReq = levelReq;
    }

    public synchronized void setResToFlush(TreeMap<Resource, Integer> resToFlush) {
        this.production.setResToFlush(resToFlush);
        notifyObserver();
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
}
