package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.client.localmodel.LocalTrack;
import it.polimi.ingsw.server.AnswerListener;
import it.polimi.ingsw.server.controller.exception.ControllerException;
import it.polimi.ingsw.server.controller.states.PrepareGameState;
import it.polimi.ingsw.server.model.ConverterToLocalModel;
import it.polimi.ingsw.server.model.game.Game;
import it.polimi.ingsw.server.model.game.SinglePlayer;
import it.polimi.ingsw.server.model.player.Board;

import java.util.ArrayList;

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

    @Override
    public synchronized ArrayList<LocalTrack> getFaithTracks() throws ControllerException {
        ArrayList<LocalTrack> localTracks=new ArrayList<>();
        LocalTrack localTrack= ConverterToLocalModel.convert(game.getPlayer().getBoard().getFaithtrack());
        localTracks.add(localTrack);
        return localTracks;
    }

    /**
     * this method does nothing
     * @throws ControllerException
     */
    @Override
    public synchronized void removeLeadersEffect() throws ControllerException {

    }

    /**
     * this method does nothing
     * @throws ControllerException
     */
    @Override
    public synchronized void applyLeadersEffect() throws ControllerException {

    }
}
