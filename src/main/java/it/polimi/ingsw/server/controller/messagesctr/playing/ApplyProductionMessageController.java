package it.polimi.ingsw.server.controller.messagesctr.playing;

import it.polimi.ingsw.client.localmodel.localcards.LocalDepotLeader;
import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.messages.answers.mainactionsanswer.ApplyProductionAnswer;
import it.polimi.ingsw.messages.requests.actions.ApplyProductionMessage;
import it.polimi.ingsw.server.controller.ControllerActions;
import it.polimi.ingsw.server.controller.exception.ControllerException;
import it.polimi.ingsw.server.controller.exception.UnexpectedControllerException;
import it.polimi.ingsw.server.model.ConverterToLocalModel;
import it.polimi.ingsw.server.model.cards.Production;
import it.polimi.ingsw.server.model.cards.leader.DepotLeaderCard;
import it.polimi.ingsw.server.model.exception.*;
import it.polimi.ingsw.server.model.game.Resource;
import it.polimi.ingsw.server.model.game.Turn;
import it.polimi.ingsw.server.model.player.Board;
import it.polimi.ingsw.server.model.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * message that handles the application of a production that owns the player
 */
public class ApplyProductionMessageController extends PlayingMessageController{
    private static final Logger logger = LogManager.getLogger(ApplyProductionMessageController.class);


    public ApplyProductionMessageController(ApplyProductionMessage clientMessage) {
        super(clientMessage);
    }

    /**
     * method that apply a specified production
     * @param controllerActions controller action of current game
     * @return ApplyProductionAnswer to notify the client with the changes of the model
     * @throws ControllerException
     */
    @Override
    protected Answer doActionNoChecks(ControllerActions<?> controllerActions) throws ControllerException {
        Player thisPlayer=getPlayerFromId(controllerActions);
        Board board = thisPlayer.getBoard();
        ApplyProductionMessage clientMessage=(ApplyProductionMessage) getClientMessage();

        Turn turn= controllerActions.getGame().getTurn();

        if(!turn.isProductionsActivated()){
            try{
                turn.setProductionsActivated(true);
            }catch (MainActionAlreadyOccurredException e){
                throw new ControllerException("you have already done your main action, wait the next turn");
            } catch (MarketTrayNotEmptyException e) {
                logger.error("market tray not flushed");
                throw new ControllerException("The market has resources that needs to be flushed");
            } catch (ProductionsResourcesNotFlushedException e) {
                logger.error("productions not flushed");
                throw new ControllerException("productions have resources that needs to be flushed");
            }
        }

        try {
            board.activateProduction(clientMessage.getWhichProd(),clientMessage.getResToGive() ,clientMessage.getResToGain() ,controllerActions.getGame() );
        } catch (InvalidResourcesByPlayerException e) {
            throw new ControllerException("you cannot produce or give this type of resources!");
        } catch (InvalidProductionSlotChosenException e) {
            throw new ControllerException("you have chosen an invalid production slot!");
        } catch (ProductionAlreadyActivatedException e) {
            throw new ControllerException("this production has already been activated!");
        } catch (NotEnoughResourcesException e){
            throw new ControllerException("you don't own enough resources to activate this production");
        } catch (ResourceNotDiscountableException e) {
            throw new ControllerException("wrong type of resource chosen");
        } catch (InvalidArgumentException e) {
            throw new ControllerException("wrong input");
        } catch (ModelException e) {
            // todo: check t
            logger.error("something unexpected happened in " + logger.getName());
            throw new UnexpectedControllerException(e.getMessage());
        }


        //generating the parameters to construct the answer
        Production production;
        try
        {
            production= board.getProduction(clientMessage.getWhichProd());
        } catch(IllegalArgumentException e){
            logger.error("something unexpected happened in " + logger.getName() + "invalid argument in getProduction that should be already caught");
            throw new UnexpectedControllerException(e.getMessage());
        }
        TreeMap<Resource,Integer> resToFlush =new TreeMap<Resource, Integer>(production.getGainedResources());

        LocalDepotLeader localDepot;
        ArrayList<LocalDepotLeader> leaderDepots=new ArrayList<>();
        for(DepotLeaderCard leaderDepot: board.getDepotLeaders()){
            localDepot=ConverterToLocalModel.convert(leaderDepot);
            leaderDepots.add(localDepot);
        }

        TreeMap<Resource, Integer> resInNormalDepots=board.getResInNormalDepots();


        return new ApplyProductionAnswer(clientMessage.getGameId(),clientMessage.getPlayerId(), resToFlush, resInNormalDepots, leaderDepots, clientMessage.getWhichProd());

    }
}
