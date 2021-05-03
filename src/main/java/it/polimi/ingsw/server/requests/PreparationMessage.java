package it.polimi.ingsw.server.requests;

import it.polimi.ingsw.server.model.cards.leader.LeaderCard;
import it.polimi.ingsw.server.model.game.Resource;

import java.util.ArrayList;
import java.util.TreeMap;

public class PreparationMessage extends ClientMessage{
    private TreeMap<Resource,Integer> resToKeep;
    private ArrayList<LeaderCard> leadersToDiscard;

    public PreparationMessage(int gameId, int playerId, TreeMap<Resource,Integer> resToKeep, ArrayList<LeaderCard> leadersToDiscard) {
        super(gameId, playerId);
        this.resToKeep=resToKeep;
        this.leadersToDiscard=leadersToDiscard;
    }
}
