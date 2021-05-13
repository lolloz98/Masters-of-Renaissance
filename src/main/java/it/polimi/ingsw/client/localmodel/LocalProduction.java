package it.polimi.ingsw.client.localmodel;

import it.polimi.ingsw.server.model.game.Resource;

import java.io.Serializable;
import java.util.TreeMap;

public class LocalProduction implements Serializable {
    private final TreeMap<Resource, Integer> resToGive;
    private final TreeMap<Resource, Integer> resToGain;
    private TreeMap<Resource, Integer> resToFlush;

    public synchronized void setResToFlush(TreeMap<Resource, Integer> resToFlush) {
        this.resToFlush = resToFlush;
    }

    public synchronized TreeMap<Resource, Integer> getResToGive() {
        return resToGive;
    }

    public synchronized TreeMap<Resource, Integer> getResToGain() {
        return resToGain;
    }

    public synchronized TreeMap<Resource, Integer> getResToFlush() {
        return resToFlush;
    }

    public LocalProduction(TreeMap<Resource, Integer> resToGive, TreeMap<Resource, Integer> resToGain, TreeMap<Resource, Integer> resToFlush) {
        this.resToGive = resToGive;
        this.resToGain = resToGain;
        this.resToFlush = resToFlush;
    }
}
