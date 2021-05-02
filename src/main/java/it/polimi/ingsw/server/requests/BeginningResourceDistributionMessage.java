package it.polimi.ingsw.server.requests;

import it.polimi.ingsw.server.model.game.Resource;

import java.util.TreeMap;

public class BeginningResourceDistributionMessage extends ServerMessage{
    private TreeMap<Resource,Integer> toGain;

    public BeginningResourceDistributionMessage(TreeMap<Resource, Integer> toGain, int playerId) {
        super(playerId);
        this.toGain = toGain;
    }

    public TreeMap<Resource, Integer> getToGain() {
        return toGain;
    }
}
