package it.polimi.ingsw.client.localmodel.localcards;

import it.polimi.ingsw.server.model.cards.Color;
import it.polimi.ingsw.server.model.game.Resource;
import java.util.TreeMap;

public class LocalDevelopCard {
    private TreeMap<Resource, Integer> cost;
    private int level;
    private Color color;
    private TreeMap<Resource, Integer> resToGive;
    private TreeMap<Resource, Integer> resToGain;
    private int victoryPoints;

    public int getVictoryPoints() {
        return victoryPoints;
    }

    public void setVictoryPoints(int victoryPoints) {
        this.victoryPoints = victoryPoints;
    }

    public synchronized TreeMap<Resource, Integer> getCost() {
        return cost;
    }

    public synchronized void setCost(TreeMap<Resource, Integer> cost) {
        this.cost = cost;
    }

    public synchronized int getLevel() {
        return level;
    }

    public synchronized void setLevel(int level) {
        this.level = level;
    }

    public synchronized Color getColor() {
        return color;
    }

    public synchronized void setColor(Color color) {
        this.color = color;
    }

    public synchronized TreeMap<Resource, Integer> getResToGive() {
        return resToGive;
    }

    public synchronized void setResToGive(TreeMap<Resource, Integer> resToGive) {
        this.resToGive = resToGive;
    }

    public synchronized TreeMap<Resource, Integer> getResToGain() {
        return resToGain;
    }

    public synchronized void setResToGain(TreeMap<Resource, Integer> resToGain) {
        this.resToGain = resToGain;
    }

    public LocalDevelopCard(TreeMap<Resource, Integer> cost, int level, Color color, int victoryPoints, TreeMap<Resource, Integer> resToGive, TreeMap<Resource, Integer> resToGain) {
        this.cost = cost;
        this.level = level;
        this.color = color;
        this.resToGive = resToGive;
        this.resToGain = resToGain;
        this.victoryPoints = victoryPoints;
    }
}