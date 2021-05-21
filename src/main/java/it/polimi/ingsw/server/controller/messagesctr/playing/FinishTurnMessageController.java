package it.polimi.ingsw.server.controller.messagesctr.playing;

import it.polimi.ingsw.client.localmodel.LocalDevelopmentGrid;
import it.polimi.ingsw.client.localmodel.LocalPlayer;
import it.polimi.ingsw.client.localmodel.LocalTrack;
import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.messages.answers.endgameanswer.EndGameAnswer;
import it.polimi.ingsw.messages.answers.mainactionsanswer.FinishTurnMultiAnswer;
import it.polimi.ingsw.messages.answers.mainactionsanswer.FinishTurnSingleAnswer;
import it.polimi.ingsw.messages.requests.FinishTurnMessage;
import it.polimi.ingsw.server.controller.ControllerActionsBase;
import it.polimi.ingsw.server.controller.ControllerActionsServer;
import it.polimi.ingsw.server.controller.exception.*;
import it.polimi.ingsw.server.model.ConverterToLocalModel;
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
    protected Answer doActionNoChecks(ControllerActionsBase<?> controllerActions) throws InvalidActionControllerException, UnexpectedControllerException {
        Game<?> game = controllerActions.getGame();

        // added if statement just for readability
        if (game instanceof MultiPlayer) controllerActions.removeLeadersEffect();

        nextTurn(game);

        Turn newTurn = controllerActions.getGame().getTurn();

        if (newTurn.getIsPlayable()) {
            if (game instanceof MultiPlayer) {
                controllerActions.applyLeadersEffect();
                LocalDevelopmentGrid localGrid = ConverterToLocalModel.convert(game.getDecksDevelop());
                Player newCurrentPlayer = ((TurnMulti) newTurn).getCurrentPlayer();
                return new FinishTurnMultiAnswer(getClientMessage().getGameId(), getClientMessage().getPlayerId(), localGrid, newCurrentPlayer.getPlayerId());
            }

            if (game instanceof SinglePlayer) {
                // we have to manage the turn of lorenzo
                SinglePlayer singlePlayer = (SinglePlayer) game;

                try {
                    performLorenzoAction(singlePlayer);
                } catch (GameIsOverException e) {
                    // we should never execute this code
                    logger.error("Error while performing Lorenzo's action: the game is already over! Returning right answer anyway");
                    return handleEndGame(controllerActions);
                }

                try {
                    singlePlayer.getTurn().setMainActionOccurred();
                } catch (MarketTrayNotEmptyException | ProductionsResourcesNotFlushedException | MainActionAlreadyOccurredException e) {
                    logger.error("We cannot have this after a Lorenzo action: " + e + " - Continuing normal execution");
                }

                try {
                    game.nextTurn();
                } catch (GameIsOverException e) {
                    return handleEndGame(controllerActions);
                } catch (MarketTrayNotEmptyException | MainActionNotOccurredException | ProductionsResourcesNotFlushedException e) {
                    logger.error("We cannot have this after a Lorenzo action: " + e + " - Continuing normal execution");
                }

                newTurn = singlePlayer.getTurn();

                if (!newTurn.getIsPlayable())
                    return handleEndGame(controllerActions);
                else {
                    //build local grid
                    LocalDevelopmentGrid localGrid = ConverterToLocalModel.convert(game.getDecksDevelop());
                    //build player track
                    LocalTrack localPlayerTrack = ConverterToLocalModel.convert(singlePlayer.getPlayer().getBoard().getFaithtrack());
                    //build lorenzo track
                    LocalTrack localLorenzoTrack = ConverterToLocalModel.convert(singlePlayer.getLorenzo().getFaithTrack());
                    return new FinishTurnSingleAnswer(getClientMessage().getGameId(), getClientMessage().getPlayerId(), localGrid, localPlayerTrack, localLorenzoTrack);
                }

            }

        } else//the game is over
            return handleEndGame(controllerActions);
        return null;
    }

    /**
     * method that creates the endGameAnswer for the player and changes the state of the game
     */
    private Answer handleEndGame(ControllerActionsBase<?> controllerActions) throws UnexpectedControllerException {
        controllerActions.toEndGameState();
        ArrayList<LocalPlayer> winners;
        winners = controllerActions.getWinners();
        return new EndGameAnswer(getClientMessage().getGameId(), getClientMessage().getPlayerId(), winners);
    }

    private void performLorenzoAction(SinglePlayer singlePlayer) throws UnexpectedControllerException, GameIsOverException {
        try {
            singlePlayer.getLorenzo().performLorenzoAction(singlePlayer);
        } catch (EndAlreadyReachedException e) {
            logger.warn("The end of the faith track was reached already: keep going on normal execution: " + e);
        } catch (NotLorenzoTurnException e) {
            // we should never execute this code
            logger.error("After player turn is not the turn of Lorenzo. The game status could be corrupted");
            throw new UnexpectedControllerException("Something unexpected happened. The turn has changed. Try to play or restart the game");
        }
    }

    private void nextTurn(Game<?> game) throws InvalidActionControllerException {
        try {
            game.nextTurn();
        } catch (GameIsOverException e) {
            throw new InvalidActionControllerException("The game is already finished: there is no next turn.");
        } catch (MarketTrayNotEmptyException e) {
            throw new MarketTrayNotEmptyControllerException();
        } catch (ProductionsResourcesNotFlushedException e) {
            throw new ProductionsResourcesNotFlushedControllerException();
        } catch (MainActionNotOccurredException e) {
            throw new MainActionNotOccurredControllerException();
        }
    }
}
