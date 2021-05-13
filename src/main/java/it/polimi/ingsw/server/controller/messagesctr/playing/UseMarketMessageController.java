package it.polimi.ingsw.server.controller.messagesctr.playing;

import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.messages.answers.mainactionsanswer.UseMarketAnswer;
import it.polimi.ingsw.messages.requests.actions.UseMarketMessage;
import it.polimi.ingsw.server.controller.ControllerActions;
import it.polimi.ingsw.server.controller.exception.ControllerException;
import it.polimi.ingsw.server.controller.exception.UnexpectedControllerException;
import it.polimi.ingsw.server.model.exception.MainActionAlreadyOccurredException;
import it.polimi.ingsw.server.model.exception.MarketTrayNotEmptyException;
import it.polimi.ingsw.server.model.exception.MatrixIndexOutOfBoundException;
import it.polimi.ingsw.server.model.exception.ProductionsResourcesNotFlushedException;
import it.polimi.ingsw.server.model.game.Game;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class UseMarketMessageController extends PlayingMessageController{

    private static final Logger logger = LogManager.getLogger(UseMarketMessageController.class);

    public UseMarketMessageController(UseMarketMessage clientMessage) {
        super(clientMessage);
    }

    @Override
    protected Answer doActionNoChecks(ControllerActions<?> controllerActions) throws ControllerException {
        Game<?> game= controllerActions.getGame();

        try {
            game.getTurn().setMarketActivated(true);
        } catch (MainActionAlreadyOccurredException e) {
            throw new ControllerException("you have already done your main action");
        } catch (MarketTrayNotEmptyException e) {
            logger.error("the market tray is not empty and i'm trying to send "+this.getClass());
            throw new UnexpectedControllerException("the market is not empty");
        } catch (ProductionsResourcesNotFlushedException e) {
            logger.error("there is resources not flushed while sending "+this.getClass());
            throw new UnexpectedControllerException("the productions are not flushed");
        }

        UseMarketMessage clientMessage=(UseMarketMessage) getClientMessage();
        try {
            game.getMarketTray().pushMarble(clientMessage.isOnRow(),clientMessage.getIndex());
        } catch (MarketTrayNotEmptyException e) {
            logger.error("the market is not empty and i'm trying to push the marble ");
            throw new UnexpectedControllerException("the market is not empty");
        } catch (MatrixIndexOutOfBoundException e) {
            throw new ControllerException("you have put a wrong matrix index, try again");
        }

        return new UseMarketAnswer(clientMessage.getGameId(),clientMessage.getPlayerId(),game.getMarketTray().getResCombinations());
    }
}
