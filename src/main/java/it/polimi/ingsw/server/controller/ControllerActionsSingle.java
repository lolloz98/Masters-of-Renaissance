package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.model.game.Game;
import it.polimi.ingsw.server.model.game.SinglePlayer;

public class ControllerActionsSingle extends ControllerActions<SinglePlayer> {

    public ControllerActionsSingle(SinglePlayer game, int id) {
        super((SinglePlayer) game, id);
    }
}
