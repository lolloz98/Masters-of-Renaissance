package it.polimi.ingsw.messages.requests.actions;

import it.polimi.ingsw.server.model.game.Resource;
import it.polimi.ingsw.server.model.player.WarehouseType;
import it.polimi.ingsw.messages.requests.ClientMessage;

import java.util.TreeMap;

/**
 * Request to flush the gained market resources on the board
 */
public class FlushMarketResMessage extends ClientMessage {
    private static final long serialVersionUID = 108L;

    /**
     * Resources to gain after a push on a market
     */
    private final TreeMap<Resource, Integer> chosenCombination;
    /**
     * Resources to keep in the board while adding the new resources in it
     */
    private final TreeMap<WarehouseType, TreeMap<Resource, Integer>> toKeep;

    public FlushMarketResMessage(int gameId, int playerId, TreeMap<Resource, Integer> chosenCombination, TreeMap<WarehouseType, TreeMap<Resource, Integer>> toKeep) {
        super(gameId, playerId);
        this.chosenCombination = chosenCombination;
        this.toKeep = toKeep;
    }

    public TreeMap<Resource, Integer> getChosenCombination() {
        return chosenCombination;
    }

    public TreeMap<WarehouseType, TreeMap<Resource, Integer>> getToKeep() {
        return toKeep;
    }
}
