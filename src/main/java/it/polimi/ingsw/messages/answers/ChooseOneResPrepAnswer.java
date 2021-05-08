package it.polimi.ingsw.messages.answers;

import it.polimi.ingsw.server.model.game.Resource;

public class ChooseOneResPrepAnswer extends Answer{
    private final Resource res;

    public Resource getRes() {
        return res;
    }

    public ChooseOneResPrepAnswer(int gameId, int playerId, Resource res) {
        super(gameId, playerId);
        this.res = res;
    }
}
