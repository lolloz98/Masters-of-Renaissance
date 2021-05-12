package it.polimi.ingsw.client.localmodel.localcards;

import it.polimi.ingsw.server.model.cards.Color;
import it.polimi.ingsw.server.model.game.Resource;
import java.util.TreeMap;

public class LocalDevelopCard extends LocalCard{
    private TreeMap<Resource, Integer> cost;
    private final int level;
    private final Color color;
    private final TreeMap<Resource, Integer> resToGive;
    private final TreeMap<Resource, Integer> resToGain;
    private TreeMap<Resource, Integer> resToFlush;
    private final int victoryPoints;

    public synchronized TreeMap<Resource, Integer> getResToFlush() {
        return resToFlush;
    }

    public synchronized void setResToFlush(TreeMap<Resource, Integer> resToFlush) {
        this.resToFlush = resToFlush;
        notifyObserver();
    }

    public int getVictoryPoints() {
        return victoryPoints;
    }

    public synchronized TreeMap<Resource, Integer> getCost() {
        return cost;
    }

    public synchronized void setCost(TreeMap<Resource, Integer> cost) {
        this.cost = cost;
        notifyObserver();
    }

    public int getLevel() {
        return level;
    }

    public Color getColor() {
        return color;
    }

    public TreeMap<Resource, Integer> getResToGive() {
        return resToGive;
    }

    public TreeMap<Resource, Integer> getResToGain() {
        return resToGain;
    }

    public LocalDevelopCard(int id, TreeMap<Resource, Integer> cost, int level, Color color, int victoryPoints, TreeMap<Resource, Integer> resToGive, TreeMap<Resource, Integer> resToGain) {
        super(id);
        this.cost = cost;
        this.level = level;
        this.color = color;
        this.resToGive = resToGive;
        this.resToGain = resToGain;
        this.victoryPoints = victoryPoints;
    }
}
