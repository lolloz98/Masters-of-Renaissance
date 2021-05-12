package it.polimi.ingsw.client.localmodel.localcards;

import it.polimi.ingsw.server.model.cards.Color;
import it.polimi.ingsw.server.model.game.Resource;
import java.util.TreeMap;

public class LocalProductionLeader extends LocalLeaderCard{
    private final TreeMap<Resource, Integer> resToGive;
    private final TreeMap<Resource, Integer> resToGain;
    private TreeMap<Resource, Integer> resToFlush;
    private final Color colorRequirement;

    public LocalProductionLeader(int id, int victoryPoints, TreeMap<Resource, Integer> resToGive, TreeMap<Resource, Integer> resToGain, Color colorRequirement) {
        super(id, victoryPoints);
        this.resToGive = resToGive;
        this.resToGain = resToGain;
        this.colorRequirement = colorRequirement;
    }

    public synchronized TreeMap<Resource, Integer> getResToFlush() {
        return resToFlush;
    }

    public synchronized void setResToFlush(TreeMap<Resource, Integer> resToFlush) {
        this.resToFlush = resToFlush;
        notifyObserver();
    }

    public TreeMap<Resource, Integer> getToGive() {
        return resToGive;
    }

    public TreeMap<Resource, Integer> getToGain() {
        return resToGain;
    }

    public Color getColorRequirement() {
        return colorRequirement;
    }
}
