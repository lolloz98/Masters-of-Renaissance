package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.AnswerListener;
import it.polimi.ingsw.server.controller.states.PrepareGameState;
import it.polimi.ingsw.server.model.game.Game;
import it.polimi.ingsw.server.model.game.SinglePlayer;
import it.polimi.ingsw.server.model.player.Board;

public class ControllerActionsSingle extends ControllerActions<SinglePlayer> {

    public ControllerActionsSingle(SinglePlayer game, int id, AnswerListener answerListener) {
        super(game, id, answerListener);
        setGameState(new PrepareGameState());
    }

    @Override
    public synchronized boolean checkToGamePlayState() {
        Board board=game.getPlayer().getBoard();
        return board.getInitialRes() == 0 && board.getLeaderCards().size() == 2;
    }
}
