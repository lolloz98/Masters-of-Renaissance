package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.client.localmodel.LocalPlayer;
import it.polimi.ingsw.server.AnswerListener;
import it.polimi.ingsw.server.controller.exception.UnexpectedControllerException;
import it.polimi.ingsw.server.model.ConverterToLocalModel;
import it.polimi.ingsw.server.model.exception.EmptyDeckException;
import it.polimi.ingsw.server.model.game.SinglePlayer;
import it.polimi.ingsw.server.model.player.Board;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

/**
 * class to be used in both controllerActionsSingleServer and Local.
 * This class contains the base implementation of some methods useful to both local and remote controllers.
 * It can be used via composition.
 */
public class ControllerActionsSingleHelper {
    private static final Logger logger = LogManager.getLogger(ControllerActionsSingleHelper.class);

    public void constructor(ControllerActionsBase<?> controllerActionsBase) throws EmptyDeckException {
        controllerActionsBase.setGameState(State.PREPARATION);
        controllerActionsBase.game.distributeLeader();
    }

    public synchronized boolean checkToGamePlayState(SinglePlayer game) {
        Board board = game.getPlayer().getBoard();
        return board.getInitialRes() == 0 && board.getLeaderCards().size() == 2;
    }


    /**
     * this method does nothing
     */
    public synchronized void removeLeadersEffect() throws UnexpectedControllerException {

    }

    /**
     * this method does nothing
     */
    public synchronized void applyLeadersEffect() throws UnexpectedControllerException {

    }

    /**
     * @return null if the winner is lorenzo, returns the player otherwise.
     */
    public synchronized ArrayList<LocalPlayer> getWinners(SinglePlayer game) throws UnexpectedControllerException {
        ArrayList<LocalPlayer> localWinners = new ArrayList<>();

        if (!game.isGameOver()) {
            logger.error("calling getWinners while the game is not over");
            throw new UnexpectedControllerException("The game is not over: you cannot know the winner!");
        }

        if (game.getHasPlayerWon()) {
            LocalPlayer winner = ConverterToLocalModel.convert(game.getPlayer(), game.getPlayer().getPlayerId());
            localWinners.add(winner);
            return localWinners;
        } else
            return null;

    }
}
