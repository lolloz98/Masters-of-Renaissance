package it.polimi.ingsw.client.answerhandler.leaderaction;

import it.polimi.ingsw.client.answerhandler.AnswerHandler;
import it.polimi.ingsw.client.localmodel.LocalBoard;
import it.polimi.ingsw.client.localmodel.LocalGame;
import it.polimi.ingsw.client.localmodel.localcards.LocalCard;
import it.polimi.ingsw.client.localmodel.localcards.LocalConcealedCard;
import it.polimi.ingsw.client.localmodel.localcards.LocalLeaderCard;
import it.polimi.ingsw.messages.answers.leaderanswer.LeaderAnswer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public abstract class ActivateLeaderAnswerHandler extends AnswerHandler {
    private static final Logger logger = LogManager.getLogger(ActivateLeaderAnswerHandler.class);

    public ActivateLeaderAnswerHandler(LeaderAnswer answer) {
        super(answer);
    }

    @Override
    public void handleAnswer(LocalGame<?> localGame) {
        LeaderAnswer serverAnswer = (LeaderAnswer) getAnswer();

        //update the leader card
        LocalBoard localBoard = localGame.getPlayerById(serverAnswer.getPlayerId()).getLocalBoard();
        ArrayList<LocalCard> leaderCards = localBoard.getLeaderCards();
        ArrayList<LocalCard> copyOfLeaderCards = new ArrayList<>(leaderCards);

        if (! (localGame.getMainPlayer().getId() == serverAnswer.getPlayerId())) {
            //modifying the arraylist of leader cards in local board
            boolean isActivated = false;
            for (LocalCard copyOfLeaderCard : copyOfLeaderCards) {
                if (copyOfLeaderCard.getId() == 0 && !isActivated && !((LocalConcealedCard) copyOfLeaderCard).isDiscarded()) {
                    leaderCards.remove(copyOfLeaderCard);
                    leaderCards.add(serverAnswer.getLeader());
                    // only one card
                    isActivated = true;
                }
                if(!isActivated) logger.error("No card has been set to be activated");
            }
        } else {
            for (LocalCard leader : leaderCards) {
                if (leader.getId() == serverAnswer.getLeader().getId())
                    ((LocalLeaderCard) leader).setActive(true);
            }
        }

        handleLeaderAnswer(localGame);

        localBoard.notifyObservers();
    }

    protected abstract void handleLeaderAnswer(LocalGame<?> localGame);
}
