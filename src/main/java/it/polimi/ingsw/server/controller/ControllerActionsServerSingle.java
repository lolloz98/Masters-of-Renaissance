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

public class ControllerActionsServerSingle extends ControllerActionsServer<SinglePlayer> {
    private static final Logger logger = LogManager.getLogger(ControllerActionsServerSingle.class);
    ControllerActionsSingleHelper controllerActionsSingleHelper = new ControllerActionsSingleHelper();

    public ControllerActionsServerSingle(SinglePlayer game, int id, AnswerListener answerListener) throws EmptyDeckException {
        super(game, id, answerListener);
        controllerActionsSingleHelper.constructor(this);
    }

    @Override
    public synchronized boolean checkToGamePlayState() {
        return controllerActionsSingleHelper.checkToGamePlayState(game);
    }


    /**
     * this method does nothing
     */
    @Override
    public synchronized void removeLeadersEffect() throws UnexpectedControllerException {
        controllerActionsSingleHelper.removeLeadersEffect();
    }

    /**
     * this method does nothing
     */
    @Override
    public synchronized void applyLeadersEffect() throws UnexpectedControllerException {
        controllerActionsSingleHelper.applyLeadersEffect();
    }

    /**
     * @return null if the winner is lorenzo, returns the player otherwise.
     */
    @Override
    public synchronized ArrayList<LocalPlayer> getWinners() throws UnexpectedControllerException {
        return controllerActionsSingleHelper.getWinners(game);
    }
}
