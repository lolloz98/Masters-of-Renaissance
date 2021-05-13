package it.polimi.ingsw.server.controller.messagesctr.playing;

import it.polimi.ingsw.client.localmodel.LocalTrack;
import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.messages.answers.mainactionsanswer.FlushProductionResAnswer;
import it.polimi.ingsw.messages.requests.actions.FlushProductionResMessage;
import it.polimi.ingsw.server.controller.ControllerActions;
import it.polimi.ingsw.server.controller.exception.ControllerException;
import it.polimi.ingsw.server.controller.messagesctr.preparation.ChooseOneResPrepMessageController;
import it.polimi.ingsw.server.model.cards.Production;
import it.polimi.ingsw.server.model.cards.leader.ProductionLeaderCard;
import it.polimi.ingsw.server.model.exception.*;
import it.polimi.ingsw.server.model.game.Resource;
import it.polimi.ingsw.server.model.game.Turn;
import it.polimi.ingsw.server.model.player.Board;
import it.polimi.ingsw.server.model.player.DevelopCardSlot;
import it.polimi.ingsw.server.model.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.TreeMap;


public class  FlushProductionResMessageController extends PlayingMessageController{
    private static final Logger logger= LogManager.getLogger(ChooseOneResPrepMessageController.class);

    public FlushProductionResMessageController(FlushProductionResMessage clientMessage) {
        super(clientMessage);
    }

    @Override
    protected Answer doActionNoChecks(ControllerActions<?> controllerActions) throws ControllerException {
        Player thisPlayer = getPlayerFromId(controllerActions);
        Board board=thisPlayer.getBoard();
        Turn turn=controllerActions.getGame().getTurn();

        try {
            board.flushResFromProductions(controllerActions.getGame());
        } catch (ModelException e) {
            // todo: handle exceptions
            throw new ControllerException(e.getMessage());
        }

        TreeMap<Resource,Integer> totGainedResources= new TreeMap<>();

        //first flush gained resources from normal production if activated
        if(board.getNormalProduction().hasBeenActivated()) totGainedResources.putAll(board.getNormalProduction().getGainedResources());

        //second flush develop card gained resources if activated
        for(DevelopCardSlot slot: board.getDevelopCardSlots()){
            Production production=slot.getCards().get(slot.getCards().size()-1).getProduction();
            if(production.hasBeenActivated())
                totGainedResources.putAll(production.getGainedResources());
        }

        //and then flush production leader card gained resources if activated
        for(ProductionLeaderCard leader: board.getProductionLeaders()){
            Production production=leader.getProduction();
            if(production.hasBeenActivated())
                totGainedResources.putAll(production.getGainedResources());
        }

        try{
            turn.setProductionsActivated(false);
        }catch(MainActionAlreadyOccurredException e){//an idea is to disable the button with this option if there is not been applied any production
            throw  new ControllerException("error: you cannot flush the resources");
        } catch (MarketTrayNotEmptyException | ProductionsResourcesNotFlushedException e) {
            // todo: handle exceptions
        }

        ArrayList<LocalTrack> localTracks=controllerActions.getFaithTracks();
        return new FlushProductionResAnswer(getClientMessage().getGameId(),getClientMessage().getPlayerId(),totGainedResources, localTracks);
    }

}
