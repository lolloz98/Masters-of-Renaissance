package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.client.localmodel.LocalPlayer;
import it.polimi.ingsw.client.localmodel.LocalTrack;
import it.polimi.ingsw.server.AnswerListener;
import it.polimi.ingsw.server.controller.exception.ControllerException;
import it.polimi.ingsw.server.controller.exception.UnexpectedControllerException;
import it.polimi.ingsw.server.model.ConverterToLocalModel;
import it.polimi.ingsw.server.model.exception.EmptyDeckException;
import it.polimi.ingsw.server.model.game.SinglePlayer;
import it.polimi.ingsw.server.model.player.Board;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class ControllerActionsSingle extends ControllerActions<SinglePlayer> {
    private static final Logger logger = LogManager.getLogger(ControllerActionsSingle.class);

    public ControllerActionsSingle(SinglePlayer game, int id, AnswerListener answerListener) throws EmptyDeckException {
        super(game, id, answerListener);
        setGameState(State.PREPARATION);
        game.distributeLeader();
    }

    @Override
    public synchronized boolean checkToGamePlayState() {
        Board board=game.getPlayer().getBoard();
        return board.getInitialRes() == 0 && board.getLeaderCards().size() == 2;
    }


    /**
     * this method does nothing
     */
    @Override
    public synchronized void removeLeadersEffect() throws UnexpectedControllerException {

    }

    /**
     * this method does nothing
     */
    @Override
    public synchronized void applyLeadersEffect() throws UnexpectedControllerException {

    }

    /**
     * @return null if the winner is lorenzo, returns the player otherwise
     */
    @Override
    public ArrayList<LocalPlayer> getWinners() throws UnexpectedControllerException {
        ArrayList<LocalPlayer> localWinners=new ArrayList<>();

        if(!game.isGameOver()){
            logger.error("calling getWinners while the game is not over");
            throw new UnexpectedControllerException("The game is not over: you cannot know the winner!");
        }

        if(game.getHasPlayerWon()){
            LocalPlayer winner=ConverterToLocalModel.convert(game.getPlayer(),game.getPlayer().getPlayerId());
            localWinners.add(winner);
            return localWinners;
        }
        else
            return null;

    }
}
