package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.AnswerListener;
import it.polimi.ingsw.server.model.game.Game;
import it.polimi.ingsw.server.model.game.SinglePlayer;

public class ControllerActionsSingle extends ControllerActions<SinglePlayer> {

    public ControllerActionsSingle(SinglePlayer game, int id, AnswerListener answerListener) {
        super((SinglePlayer) game, id, answerListener);
    }
}
