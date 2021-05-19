package it.polimi.ingsw.server.controller.messagesctr.playing;

import it.polimi.ingsw.client.localmodel.LocalTrack;
import it.polimi.ingsw.client.localmodel.localcards.LocalLeaderCard;
import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.messages.answers.leaderanswer.DiscardLeaderAnswer;
import it.polimi.ingsw.messages.requests.leader.DiscardLeaderMessage;
import it.polimi.ingsw.messages.requests.leader.LeaderMessage;
import it.polimi.ingsw.server.controller.ControllerActions;
import it.polimi.ingsw.server.controller.exception.*;
import it.polimi.ingsw.server.model.ConverterToLocalModel;
import it.polimi.ingsw.server.model.cards.leader.LeaderCard;
import it.polimi.ingsw.server.model.exception.*;
import it.polimi.ingsw.server.model.game.SinglePlayer;
import it.polimi.ingsw.server.model.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class DiscardLeaderMessageController extends PlayingMessageController implements ConcealedLeaderMessageInterface {

    private static final Logger logger = LogManager.getLogger(DiscardLeaderMessageController.class);


    public DiscardLeaderMessageController(DiscardLeaderMessage clientMessage) {
        super(clientMessage);
    }

    /**
     * Set leader card to discarded. It also moves the player of one step on their faith track.
     *
     * @param controllerActions current controller action
     * @return DiscardLeaderCardAnswer
     */
    @Override
    protected Answer doActionNoChecks(ControllerActions<?> controllerActions) throws WrongPlayerIdControllerException, InvalidArgumentControllerException, AlreadyActiveLeaderControllerException, AlreadyDiscardedLeaderControllerException, UnexpectedControllerException {
        LeaderCard<?> card;
        Player thisPlayer = getPlayerFromId(controllerActions);
        try {
            card = thisPlayer.getBoard().getLeaderCard(((LeaderMessage) getClientMessage()).getLeaderId());
        } catch (InvalidArgumentException e) {
            throw new InvalidArgumentControllerException("The leaderCardId provided was not valid");
        }

        try {
            card.discard();
        } catch (AlreadyActiveLeaderException e) {
            throw new AlreadyActiveLeaderControllerException();
        } catch (ActivateDiscardedCardException e) {
            throw new AlreadyDiscardedLeaderControllerException();
        }

        try {
            thisPlayer.getBoard().getFaithtrack().move(1, controllerActions.getGame());
        } catch (EndAlreadyReachedException | InvalidStepsException e) {
            logger.warn("After discarding leader, moving of one step gave: " + e + " - Continuing normal execution");
        }

        // All faith tracks might be modified if vatican Figure is activated while moving
        ArrayList<LocalTrack> localTracks = ConverterToLocalModel.getLocalFaithTracks(controllerActions.getGame());

        //pass lorenzo track if single player
        LocalTrack lorenzoTrack = null;
        if(controllerActions.getGame() instanceof SinglePlayer)
            lorenzoTrack = ConverterToLocalModel.convert(((SinglePlayer) controllerActions.getGame()).getLorenzo().getFaithTrack());

        LocalLeaderCard localLeader=ConverterToLocalModel.convert(card);

        return new DiscardLeaderAnswer(getClientMessage().getGameId(), getClientMessage().getPlayerId(),localLeader, localTracks, lorenzoTrack);
    }
}
