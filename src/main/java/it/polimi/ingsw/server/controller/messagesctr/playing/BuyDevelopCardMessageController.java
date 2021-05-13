package it.polimi.ingsw.server.controller.messagesctr.playing;

import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.messages.requests.ClientMessage;
import it.polimi.ingsw.messages.requests.actions.BuyDevelopCardMessage;
import it.polimi.ingsw.server.controller.ControllerActions;
import it.polimi.ingsw.server.controller.ControllerActionsMulti;
import it.polimi.ingsw.server.controller.exception.ControllerException;
import it.polimi.ingsw.server.controller.exception.NotCurrentPlayerException;
import it.polimi.ingsw.server.controller.exception.UnexpectedControllerException;
import it.polimi.ingsw.server.model.exception.*;
import it.polimi.ingsw.server.model.player.Board;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//todo
public class BuyDevelopCardMessageController extends PlayingMessageController{

    private static final Logger logger = LogManager.getLogger(BuyDevelopCardMessageController.class);

    public BuyDevelopCardMessageController(BuyDevelopCardMessage clientMessage) {
        super(clientMessage);
    }

    @Override
    protected Answer doActionNoChecks(ControllerActions<?> controllerActions) throws ControllerException {
        // todo
        try {
            controllerActions.getGame().getTurn().setMainActionOccurred();
        } catch (MarketTrayNotEmptyException e) {
            logger.error("the market tray is not empty while trying to buy a develop card");
            throw new UnexpectedControllerException(e.getMessage());
        } catch (MainActionAlreadyOccurredException e) {
            throw new ControllerException(e.getMessage());
        } catch (ProductionsResourcesNotFlushedException e) {
            logger.error("the produced resources are not flushed while trying to buy a develop card");
            throw new UnexpectedControllerException(e.getMessage());
        }

        Board board=getPlayerFromId(controllerActions).getBoard();

        //todo:handle de exceptions
        BuyDevelopCardMessage clientMessage=(BuyDevelopCardMessage)getClientMessage();
        try {
            board.buyDevelopCard(controllerActions.getGame(),clientMessage.getColor(),clientMessage.getLevel(),clientMessage.whichSlotToStore,clientMessage.toPay );
        } catch (ResourceNotDiscountableException e) {
            e.printStackTrace();
        } catch (NotEnoughResourcesException e) {
            e.printStackTrace();
        } catch (EmptyDeckException e) {
            e.printStackTrace();
        } catch (FullDevelopSlotException e) {
            e.printStackTrace();
        } catch (InvalidDevelopCardToSlotException e) {
            e.printStackTrace();
        } catch (InvalidArgumentException e) {
            e.printStackTrace();
        } catch (InvalidResourceQuantityToDepotException e) {
            e.printStackTrace();
        }
        return null;
    }
}
