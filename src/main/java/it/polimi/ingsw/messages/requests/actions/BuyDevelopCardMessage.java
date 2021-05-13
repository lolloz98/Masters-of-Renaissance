package it.polimi.ingsw.messages.requests.actions;

import it.polimi.ingsw.messages.requests.ClientMessage;
import it.polimi.ingsw.server.model.cards.Color;
import it.polimi.ingsw.server.model.game.Resource;
import it.polimi.ingsw.server.model.player.WarehouseType;

import java.util.TreeMap;

/**
 * Request for buying a develop card
 */
public class BuyDevelopCardMessage extends ClientMessage {
    private static final long serialVersionUID = 111L;
    public final int level;
    public final Color color;
    /**
     * on which deck do we have to put the card just bought
     */
    public final int whichDeck;

    /**
     * on which production slot we want to put the card
     */
    public final int whichSlotToStore;



    /**
     *resources to pay in order to obtain the desired develop card
     */
    public final TreeMap<WarehouseType, TreeMap<Resource,Integer>> toPay;

    public BuyDevelopCardMessage(int gameId, int playerId, int level, Color color, int whichDeck, int whichSlotToStore, TreeMap<WarehouseType, TreeMap<Resource, Integer>> toPay) {
        super(gameId, playerId);
        this.level = level;
        this.color = color;
        this.whichDeck = whichDeck;
        this.whichSlotToStore = whichSlotToStore;
        this.toPay = toPay;
    }

    public int getWhichDeck() {
        return whichDeck;
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
