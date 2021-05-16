package it.polimi.ingsw.messages.requests;

import it.polimi.ingsw.enums.Resource;

import java.util.TreeMap;
@Deprecated
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
