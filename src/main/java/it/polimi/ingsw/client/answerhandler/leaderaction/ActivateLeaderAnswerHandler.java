package it.polimi.ingsw.client.answerhandler.leaderaction;

import it.polimi.ingsw.client.answerhandler.AnswerHandler;
import it.polimi.ingsw.client.localmodel.LocalBoard;
import it.polimi.ingsw.client.localmodel.LocalGame;
import it.polimi.ingsw.client.localmodel.LocalMulti;
import it.polimi.ingsw.client.localmodel.localcards.LocalCard;
import it.polimi.ingsw.client.localmodel.localcards.LocalConcealedCard;
import it.polimi.ingsw.client.localmodel.localcards.LocalLeaderCard;
import it.polimi.ingsw.messages.answers.leaderanswer.LeaderAnswer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

/**
 * Answer Handler that handles the activation of a generic leader
 */
public abstract class ActivateLeaderAnswerHandler extends AnswerHandler {
    private static final Logger logger = LogManager.getLogger(ActivateLeaderAnswerHandler.class);

    public ActivateLeaderAnswerHandler(LeaderAnswer answer) {
        super(answer);
    }

    /**
     * method that updates the local game after the activation of a leader card
     *
     * @param localGame
     */
    @Override
    public void handleAnswer(LocalGame<?> localGame) {
        LeaderAnswer serverAnswer = (LeaderAnswer) getAnswer();

        //update the leader card
        LocalBoard localBoard = localGame.getPlayerById(serverAnswer.getPlayerId()).getLocalBoard();
        ArrayList<LocalCard> leaderCards = localBoard.getLeaderCards();
        ArrayList<LocalCard> copyOfLeaderCards = new ArrayList<>(leaderCards);

        if (localGame.getMainPlayer().getId() != serverAnswer.getPlayerId()) {
            //modifying the arraylist of leader cards in local board
            boolean isActivated = false;
            for (LocalCard copyOfLeaderCard : copyOfLeaderCards) {
                if (copyOfLeaderCard.getId() == 0 && !isActivated && !((LocalConcealedCard) copyOfLeaderCard).isDiscarded()) {
                    leaderCards.remove(copyOfLeaderCard);
                    leaderCards.add(serverAnswer.getLeader());
                    // only one card
                    isActivated = true;
                }
            }
        } else {
            for (int i = 0; i < leaderCards.size(); i++) {
                LocalCard card = leaderCards.get(i);
                if (card.getId() == serverAnswer.getLeader().getId()) {
                    leaderCards.set(i, serverAnswer.getLeader());
                }
            }
        }

        // update history
        if (localGame instanceof LocalMulti) {
            LocalMulti localMulti = (LocalMulti) localGame;
            String actionDescription;
            if (serverAnswer.getPlayerId() == localMulti.getMainPlayerId()) {
                actionDescription = "You activated a leader card";
            } else {
                actionDescription = localMulti.getPlayerById(serverAnswer.getPlayerId()).getName() + " activated a leader card";
            }
            localMulti.getLocalTurn().getHistoryObservable().getHistory().add(actionDescription);
        }


        handleLeaderAnswer(localGame);
        localBoard.notifyObservers();


    }

    protected abstract void handleLeaderAnswer(LocalGame<?> localGame);
}
