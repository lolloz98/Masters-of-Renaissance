package it.polimi.ingsw.client.localmodel.localcards;

import it.polimi.ingsw.client.localmodel.LocalProduction;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.Resource;
import java.util.TreeMap;

public class LocalDevelopCard extends LocalCard{
    private TreeMap<Resource, Integer> cost;
    private final int level;
    private final Color color;
    private final LocalProduction production;
    private final int victoryPoints;

    public synchronized void setResToFlush(TreeMap<Resource, Integer> resToFlush) {
        this.production.setResToFlush(resToFlush);
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

    public synchronized LocalProduction getProduction() {
        return production;
    }

    public LocalDevelopCard(int id, TreeMap<Resource, Integer> cost, int level, Color color, int victoryPoints, TreeMap<Resource, Integer> resToGive, TreeMap<Resource, Integer> resToGain, TreeMap<Resource, Integer> resToFlush) {
        super(id);
        this.cost = cost;
        this.level = level;
        this.color = color;
        production = new LocalProduction(resToGive, resToGain, resToFlush);
        this.victoryPoints = victoryPoints;
    }
}
