package it.polimi.ingsw.messages.requests.actions;

import it.polimi.ingsw.messages.requests.ClientMessage;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.Resource;
import it.polimi.ingsw.enums.WarehouseType;

import java.util.TreeMap;

/**
 * Request for buying a develop card
 */
public class BuyDevelopCardMessage extends ClientMessage {
    private static final long serialVersionUID = 111L;
    public final int level;
    public final Color color;
    /**
     * on which production slot we want to put the card
     */
    public final int whichSlotToStore;



    /**
     *resources to pay in order to obtain the desired develop card
     */
    public final TreeMap<WarehouseType, TreeMap<Resource,Integer>> toPay;

    public BuyDevelopCardMessage(int gameId, int playerId, int level, Color color, int whichSlotToStore, TreeMap<WarehouseType, TreeMap<Resource, Integer>> toPay) {
        super(gameId, playerId);
        this.level = level;
        this.color = color;
        this.whichSlotToStore = whichSlotToStore;
        this.toPay = toPay;
    }

    public int getLevel() {
        return level;
    }

    public Color getColor() {
        return color;
    }

    public TreeMap<WarehouseType, TreeMap<Resource, Integer>> getToPay() {
        return toPay;
    }

    public int getWhichSlotToStore() {
        return whichSlotToStore;
    }
}
