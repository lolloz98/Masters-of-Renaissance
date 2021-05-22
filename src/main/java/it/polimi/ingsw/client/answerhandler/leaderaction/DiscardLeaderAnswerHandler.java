package it.polimi.ingsw.client.answerhandler.leaderaction;

import it.polimi.ingsw.client.answerhandler.AnswerHandler;
import it.polimi.ingsw.client.localmodel.LocalBoard;
import it.polimi.ingsw.client.localmodel.LocalGame;
import it.polimi.ingsw.client.localmodel.LocalSingle;
import it.polimi.ingsw.client.localmodel.localcards.LocalCard;
import it.polimi.ingsw.client.localmodel.localcards.LocalConcealedCard;
import it.polimi.ingsw.client.localmodel.localcards.LocalLeaderCard;
import it.polimi.ingsw.messages.answers.leaderanswer.DiscardLeaderAnswer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DiscardLeaderAnswerHandler extends AnswerHandler {
    private static final Logger logger = LogManager.getLogger(DiscardLeaderAnswerHandler.class);

    public DiscardLeaderAnswerHandler(DiscardLeaderAnswer answer) {
        super(answer);
    }

    @Override
    public void handleAnswer(LocalGame<?> localGame) {
        DiscardLeaderAnswer serverAnswer = (DiscardLeaderAnswer) getAnswer();

        LocalBoard localBoard = localGame.getPlayerById(serverAnswer.getPlayerId()).getLocalBoard();

        //update the leaderCard
        if (localGame.getMainPlayer().getId() == serverAnswer.getPlayerId()) {
            for (LocalCard card : localGame.getMainPlayer().getLocalBoard().getLeaderCards()) {
                if (card.getId() == serverAnswer.getLeader().getId())
                    ((LocalLeaderCard) card).setDiscarded(true);
            }
        } else {
            boolean isDiscarded = false;
            for (LocalCard card : localGame.getPlayerById(serverAnswer.getPlayerId()).getLocalBoard().getLeaderCards()) {
                if (card.getId() == 0 && !isDiscarded && !((LocalConcealedCard) card).isDiscarded()) {
                    ((LocalConcealedCard) card).setDiscarded(true);
                    // only one card
                    isDiscarded = true;
                }
            }
            if(!isDiscarded) logger.error("No card has been set to be discarded");
        }

        //update the tracks
        localGame.updatePlayerFaithTracks(serverAnswer.getLocalTracks());

        //update the lorenzo track
        if (localGame instanceof LocalSingle)
            ((LocalSingle) localGame).setLorenzoTrack(serverAnswer.getLorenzoTrack());

        localBoard.notifyObservers();
    }
}
