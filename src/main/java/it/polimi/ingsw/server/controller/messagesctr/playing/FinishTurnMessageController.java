package it.polimi.ingsw.server.controller.messagesctr.playing;

import it.polimi.ingsw.client.localmodel.LocalDevelopmentGrid;
import it.polimi.ingsw.client.localmodel.LocalPlayer;
import it.polimi.ingsw.client.localmodel.LocalTrack;
import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.messages.answers.gameendedanswer.EndGameAnswer;
import it.polimi.ingsw.messages.answers.mainactionsanswer.FinishTurnMultiAnswer;
import it.polimi.ingsw.messages.answers.mainactionsanswer.FinishTurnSingleAnswer;
import it.polimi.ingsw.messages.requests.FinishTurnMessage;
import it.polimi.ingsw.server.controller.ControllerActions;
import it.polimi.ingsw.server.controller.exception.ControllerException;
import it.polimi.ingsw.server.controller.exception.UnexpectedControllerException;
import it.polimi.ingsw.server.model.ConverterToLocalModel;
import it.polimi.ingsw.server.model.cards.lorenzo.LorenzoCard;
import it.polimi.ingsw.server.model.exception.*;
import it.polimi.ingsw.server.model.game.*;
import it.polimi.ingsw.server.model.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

/**
 * class that handles the passage to the next turn, hence the switch of the current player
 */
public class FinishTurnMessageController extends PlayingMessageController {

    private static final Logger logger = LogManager.getLogger(FinishTurnMessageController.class);

    public FinishTurnMessageController(FinishTurnMessage clientMessage) {
        super(clientMessage);
    }

    @Override
    protected Answer doActionNoChecks(ControllerActions<?> controllerActions) throws ControllerException {
        Game<?> game = controllerActions.getGame();

        controllerActions.removeLeadersEffect();
        nextTurn(game);

        Turn newTurn = controllerActions.getGame().getTurn();

        if (newTurn.getIsPlayable()) {

            controllerActions.applyLeadersEffect();

            if (game instanceof MultiPlayer) {

                LocalDevelopmentGrid localGrid = ConverterToLocalModel.convert(game.getDecksDevelop());
                Player newCurrentPlayer = ((TurnMulti) newTurn).getCurrentPlayer();
                return new FinishTurnMultiAnswer(getClientMessage().getGameId(), getClientMessage().getPlayerId(), localGrid, newCurrentPlayer.getPlayerId());

            }

            if (game instanceof SinglePlayer) {

                SinglePlayer singlePlayer = (SinglePlayer) game;

                if ((singlePlayer.getTurn()).isLorenzoPlaying()) {//it must be always true

                    try {
                        singlePlayer.getLorenzo().performLorenzoAction(singlePlayer);
                    } catch (EmptyDeckException e) {
                        logger.warn("something wrong happened, the lorenzo deck is empty. it should never be empty");
                        throw new UnexpectedControllerException("lorenzo deck is empty");
                    } catch (FigureAlreadyDiscardedException e) {
                        logger.warn("something wrong happened while performing lorenzo action, the vatican figure has already been discarded");
                        throw new UnexpectedControllerException("the vatican figure has already been discarded");
                    } catch (FigureAlreadyActivatedException e) {
                        logger.warn("something wrong happened while performing lorenzo action, the vatican figure has already been activated");
                        throw new UnexpectedControllerException("the vatican figure has already been activated");
                    } catch (InvalidStepsException e) {
                        logger.warn("something wrong happened while performing lorenzo action, we are trying to pass a negative step parameter to the method move in faithtrack");
                        throw new UnexpectedControllerException("you are trying to go backward on the faith track");
                    } catch (EndAlreadyReachedException e) {
                        logger.warn("something wrong happened while performing lorenzo action, the end has already been reached");
                        throw new UnexpectedControllerException("you have already reached the end");
                    }

                    nextTurn(singlePlayer);

                    newTurn = singlePlayer.getTurn();

                    if (!newTurn.getIsPlayable())
                        return handleEndGame(controllerActions);
                    else {
                        LocalDevelopmentGrid localGrid = ConverterToLocalModel.convert(game.getDecksDevelop());
                        LocalTrack localTrack = ConverterToLocalModel.convert(singlePlayer.getPlayer().getBoard().getFaithtrack());
                        LorenzoCard lorenzoCard;
                        try {
                            lorenzoCard = singlePlayer.getLorenzoDeck().getTopCard();
                        } catch (EmptyDeckException e) {
                            logger.warn("something wrong happened, the lorenzo deck is empty. it should never be empty");
                            throw new UnexpectedControllerException("lorenzo deck is empty");
                        }

                        return new FinishTurnSingleAnswer(getClientMessage().getGameId(), getClientMessage().getPlayerId(), localGrid, lorenzoCard, localTrack);
                    }

                } else {
                    logger.error("something unexpected happened at " + this.getClass() + ": lorenzo is playing");
                    throw new UnexpectedControllerException("lorenzo is playing");
                }
            }
        } else
            return handleEndGame(controllerActions);
        return null;
    }

    /**
     * method that creates the endGameAnswer for the player and changes the state of the game
     * @param controllerActions
     * @return
     */
    private Answer handleEndGame(ControllerActions controllerActions) throws ControllerException {
        controllerActions.toEndGameState();
        ArrayList<LocalPlayer> winner=controllerActions.getWinners();
        return new EndGameAnswer(getClientMessage().getGameId(), getClientMessage().getPlayerId(), winner);
    }

    private void nextTurn(Game<?> game) throws ControllerException {
        try {
            game.nextTurn();
        } catch (GameIsOverException e) {
            logger.error("the game is already finished and is called next turn");
            throw new UnexpectedControllerException("the game is already finished, you cannot have the next turn");
        } catch (MarketTrayNotEmptyException e) {
            throw new ControllerException("the market is not empty! you have to choose the resources to obtain");
        } catch (ProductionsResourcesNotFlushedException e) {
            throw new ControllerException("you have some resource to flush before finish the turn");
        } catch (MainActionNotOccurredException e) {
            throw new ControllerException("you have not done any main action yet");
        }
    }
}
