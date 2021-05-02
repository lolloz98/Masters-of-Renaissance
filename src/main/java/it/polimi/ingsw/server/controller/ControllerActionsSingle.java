package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.model.game.Game;
import it.polimi.ingsw.server.model.game.SinglePlayer;

public class ControllerActionsSingle extends ControllerActions<SinglePlayer> {


    public ControllerActionsSingle(Game game, int id) {
        super((SinglePlayer) game, id);
    }

    /**
     * method that distributes to the players the resources at the beginning of the match
     */
    protected synchronized void distributeBeginningRes(){
        //this method does nothing
    }
}
