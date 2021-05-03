package it.polimi.ingsw.server.requests.actions;

import it.polimi.ingsw.server.model.game.Resource;
import it.polimi.ingsw.server.model.player.WarehouseType;
import it.polimi.ingsw.server.requests.ClientMessage;

import java.util.TreeMap;

/**
 * Request of application of a production
 */
public class ApplyProductionMessage extends ClientMessage {
    private static final long serialVersionUID = 109L;

    /**
     * 0 -> normal production
     * 1 -- 3 -> develop-card production
     * 4 -- 5 -> leader-card production
     */
    private final int whichProd;
    /**
     * payment for applying the production
     */
    private final TreeMap<WarehouseType, TreeMap<Resource, Integer>> resToGive;
    /**
     * resources to gain after the application of the production
     */
    private final TreeMap<Resource, Integer> resToGain;

    public int getWhichProd() {
        return whichProd;
    }

    public TreeMap<WarehouseType, TreeMap<Resource, Integer>> getResToGive() {
        return resToGive;
    }

    public TreeMap<Resource, Integer> getResToGain() {
        return resToGain;
    }

    public ApplyProductionMessage(int gameId, int playerId, int whichProd, TreeMap<WarehouseType, TreeMap<Resource, Integer>> resToGive, TreeMap<Resource, Integer> resToGain) {
        super(gameId, playerId);
        this.whichProd = whichProd;
        this.resToGive = resToGive;
        this.resToGain = resToGain;
    }
}
