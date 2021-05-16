package it.polimi.ingsw.server.controller.messagesctr.playing;

import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.messages.answers.mainactionsanswer.UseMarketAnswer;
import it.polimi.ingsw.messages.requests.actions.UseMarketMessage;
import it.polimi.ingsw.server.controller.ControllerActions;
import it.polimi.ingsw.server.controller.exception.*;
import it.polimi.ingsw.server.model.exception.*;
import it.polimi.ingsw.server.model.game.Game;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class UseMarketMessageController extends PlayingMessageController {

    private static final Logger logger = LogManager.getLogger(UseMarketMessageController.class);

    public UseMarketMessageController(UseMarketMessage clientMessage) {
        super(clientMessage);
    }

    @Override
    protected Answer doActionNoChecks(ControllerActions<?> controllerActions) throws ControllerException {
        Game<?> game = controllerActions.getGame();

        if(game.getTurn().cannotSetMarketActivated(true))
            throw new InvalidActionControllerException("The action selected is invalid for the status of the game.");

        UseMarketMessage clientMessage = (UseMarketMessage) getClientMessage();
        try {
            game.getMarketTray().pushMarble(clientMessage.isOnRow(), clientMessage.getIndex());
        } catch (MarketTrayNotEmptyException e) {
            throw new InvalidActionControllerException("The action selected is not valid: some resources in the market are waiting to be flushed.");
        } catch (MatrixIndexOutOfBoundException e) {
            throw new InvalidArgumentControllerException("You have put a wrong matrix index, try again.");
        }

        try {
            game.getTurn().setMarketActivated(true);
        } catch (ModelException e){
            // Since we already controlled if it could be set to true marketActivated, we are confident we are never coming here
            logger.error("SetMarketActivate threw an unexpected exception after checks: " + e);
            throw new UnexpectedControllerException("Something unexpected happened. The resources are ready to be flushed in the market.");
        }

        return new UseMarketAnswer(clientMessage.getGameId(), clientMessage.getPlayerId(), game.getMarketTray().getResCombinations());
    }
}
