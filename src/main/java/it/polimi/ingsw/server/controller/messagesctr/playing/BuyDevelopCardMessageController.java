package it.polimi.ingsw.server.controller.messagesctr.playing;

import it.polimi.ingsw.client.localmodel.LocalBoard;
import it.polimi.ingsw.client.localmodel.LocalDevelopmentGrid;
import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.messages.answers.mainactionsanswer.BuyDevelopCardAnswer;
import it.polimi.ingsw.messages.requests.actions.BuyDevelopCardMessage;
import it.polimi.ingsw.server.controller.ControllerActions;
import it.polimi.ingsw.server.controller.exception.*;
import it.polimi.ingsw.server.model.ConverterToLocalModel;
import it.polimi.ingsw.server.model.exception.*;
import it.polimi.ingsw.server.model.player.Board;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class BuyDevelopCardMessageController extends PlayingMessageController {

    private static final Logger logger = LogManager.getLogger(BuyDevelopCardMessageController.class);

    public BuyDevelopCardMessageController(BuyDevelopCardMessage clientMessage) {
        super(clientMessage);
    }

    /**
     * handles the purchase of a develop card
     *
     * @param controllerActions controller action of current game
     * @return an answer to the client an update of the board and of the development grid
     */
    @Override
    protected Answer doActionNoChecks(ControllerActions<?> controllerActions) throws InvalidActionControllerException, WrongPlayerIdControllerException, UnexpectedControllerException, InvalidArgumentControllerException {

        if(controllerActions.getGame().getTurn().cannotSetMainActionOccurred())
            throw new InvalidActionControllerException("You cannot perform another main action at this time");

        Board board = getPlayerFromId(controllerActions).getBoard();

        BuyDevelopCardMessage clientMessage = (BuyDevelopCardMessage) getClientMessage();
        try {
            board.buyDevelopCard(controllerActions.getGame(), clientMessage.getColor(), clientMessage.getLevel(), clientMessage.whichSlotToStore, clientMessage.toPay);
        }  catch (InvalidResourceQuantityToDepotException | ResourceNotDiscountableException | NotEnoughResourcesException | EmptyDeckException | FullDevelopSlotException | InvalidDevelopCardToSlotException | InvalidArgumentException e) {
            throw new InvalidArgumentControllerException(e.getMessage(), 0);
        }

        try {
            controllerActions.getGame().getTurn().setMainActionOccurred();
        } catch (ModelException e) {
            // Since we already controlled if it could be set to true marketActivated, we are confident we are never coming here
            logger.error("setMainActionOccurred threw an unexpected exception after checks: " + e);
            throw new UnexpectedControllerException("Something unexpected happened. However, you managed to buy the develop card.");
        }

        LocalBoard localBoard = ConverterToLocalModel.convert(board, true);
        LocalDevelopmentGrid localGrid = ConverterToLocalModel.convert(controllerActions.getGame().getDecksDevelop());
        return new BuyDevelopCardAnswer(getClientMessage().getGameId(), getClientMessage().getPlayerId(), localBoard, localGrid, clientMessage.whichSlotToStore);
    }
}
