package it.polimi.ingsw.messages.requests;

import it.polimi.ingsw.enums.Resource;

/**
 * Choose the type of resources given at the beginning of the game
 */
public class ChooseOneResPrepMessage extends ClientMessage {
    private static final long serialVersionUID = 56L;
    private final Resource res;

    public ChooseOneResPrepMessage(int gameId, int playerId, Resource res) {
        super(gameId, playerId);
        this.res = res;
    }

    public Resource getRes() {
        return res;
    }
}
