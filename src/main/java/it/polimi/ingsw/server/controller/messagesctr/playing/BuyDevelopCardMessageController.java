package it.polimi.ingsw.server.controller.messagesctr.playing;

import it.polimi.ingsw.client.localmodel.LocalBoard;
import it.polimi.ingsw.client.localmodel.LocalDevelopmentGrid;
import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.messages.answers.mainactionsanswer.BuyDevelopCardAnswer;
import it.polimi.ingsw.messages.requests.actions.BuyDevelopCardMessage;
import it.polimi.ingsw.server.controller.ControllerActions;
import it.polimi.ingsw.server.controller.exception.ControllerException;
import it.polimi.ingsw.server.controller.exception.UnexpectedControllerException;
import it.polimi.ingsw.server.model.ConverterToLocalModel;
import it.polimi.ingsw.server.model.exception.*;
import it.polimi.ingsw.server.model.player.Board;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class BuyDevelopCardMessageController extends PlayingMessageController{

    private static final Logger logger = LogManager.getLogger(BuyDevelopCardMessageController.class);

    public BuyDevelopCardMessageController(BuyDevelopCardMessage clientMessage) {
        super(clientMessage);
    }

    /**
     * handles the purchase of a develop card
     * @param controllerActions controller action of current game
     * @return an answer to the client an update of the board and of the development grid
     * @throws ControllerException
     */
    @Override
    protected Answer doActionNoChecks(ControllerActions<?> controllerActions) throws ControllerException {

        try {
            controllerActions.getGame().getTurn().setMainActionOccurred();
        } catch (MarketTrayNotEmptyException e) {
            throw new ControllerException("you have already done the market action");
        } catch (MainActionAlreadyOccurredException e) {
            throw new ControllerException(e.getMessage());
        } catch (ProductionsResourcesNotFlushedException e) {
            throw new ControllerException("you have already done the production action");
        }

        Board board=getPlayerFromId(controllerActions).getBoard();

        BuyDevelopCardMessage clientMessage=(BuyDevelopCardMessage)getClientMessage();
        try {
            board.buyDevelopCard(controllerActions.getGame(),clientMessage.getColor(),clientMessage.getLevel(),clientMessage.whichSlotToStore,clientMessage.toPay );
        } catch (ResourceNotDiscountableException e) {
            logger.error("something wrong happened, in the to pay of " + getClientMessage().getClass() + "have been put some not discountable resource");
            throw new UnexpectedControllerException(e.getMessage());
        } catch (NotEnoughResourcesException|EmptyDeckException|FullDevelopSlotException|InvalidDevelopCardToSlotException|InvalidArgumentException e) {
            throw new ControllerException(e.getMessage());
        } catch (InvalidResourceQuantityToDepotException e) {
            logger.error("we are trying to spend the resources on the depot even if we have not enough. before this it must had been lunched NotEnoughResourcesException");
            throw new UnexpectedControllerException(e.getMessage());
        }

        LocalBoard localBoard= ConverterToLocalModel.convert(board,true);
        LocalDevelopmentGrid localGrid=ConverterToLocalModel.convert(controllerActions.getGame().getDecksDevelop());
        return new BuyDevelopCardAnswer(getClientMessage().getGameId(),getClientMessage().getPlayerId(),localBoard,localGrid);
    }
}
