package it.polimi.ingsw.client.answerhandler.preparation;

import it.polimi.ingsw.client.answerhandler.AnswerHandler;
import it.polimi.ingsw.client.localmodel.LocalGame;
import it.polimi.ingsw.client.localmodel.LocalMulti;
import it.polimi.ingsw.client.localmodel.localcards.LocalCard;
import it.polimi.ingsw.messages.answers.preparationanswer.RemoveLeaderPrepAnswer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class RemoveLeaderPrepAnswerHandler extends AnswerHandler {
    private static final Logger logger = LogManager.getLogger(RemoveLeaderPrepAnswerHandler.class);

    public RemoveLeaderPrepAnswerHandler(RemoveLeaderPrepAnswer answer) {
        super(answer);
    }

    @Override
    public void handleAnswer(LocalGame<?> localGame) {
        RemoveLeaderPrepAnswer removeLeaderPrepAnswer = (RemoveLeaderPrepAnswer) getAnswer();
        if(localGame.getMainPlayer().getId() == removeLeaderPrepAnswer.getPlayerId()) {
            ArrayList<LocalCard> localCardsToRemove = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                if (removeLeaderPrepAnswer.getRemovedLeaderIds().contains(localGame.getMainPlayer().getLocalBoard().getLeaderCards().get(i).getId())) {
                    localCardsToRemove.add(localGame.getMainPlayer().getLocalBoard().getLeaderCards().get(i));
                }
            }
            localGame.getMainPlayer().getLocalBoard().getLeaderCards().removeAll(localCardsToRemove);
            localGame.notifyObserver();
        }
        else {
            // if the player id received is not the mainplayer, this must be a multiplayer, i remove two covered cards from the player
            if (localGame instanceof LocalMulti) {
                LocalMulti localMulti = (LocalMulti) localGame;
                localMulti.getPlayerById(removeLeaderPrepAnswer.getPlayerId()).getLocalBoard().getLeaderCards().remove(0);
                localMulti.getPlayerById(removeLeaderPrepAnswer.getPlayerId()).getLocalBoard().getLeaderCards().remove(0);
            } else {
                logger.error("Answer with wrong player id");
            }
        }
        if(localGame.getState()!=removeLeaderPrepAnswer.getState()) {
            localGame.setState(removeLeaderPrepAnswer.getState());
            localGame.notifyObserver();
        }
    }
}
