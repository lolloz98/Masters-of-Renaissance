package it.polimi.ingsw.client.answerhandler;

import it.polimi.ingsw.client.localmodel.LocalGame;
import it.polimi.ingsw.client.localmodel.LocalMulti;
import it.polimi.ingsw.client.localmodel.localcards.LocalCard;
import it.polimi.ingsw.messages.answers.preparationanswer.RemoveLeaderPrepAnswer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class RemoveLeaderPrepAnswerHandler extends AnswerHandler{
    private static final Logger logger = LogManager.getLogger(RemoveLeaderPrepAnswerHandler.class);

    public RemoveLeaderPrepAnswerHandler(RemoveLeaderPrepAnswer answer) {
        super(answer);
    }

    @Override
    public void handleAnswer(LocalGame<?> localGame) {
        RemoveLeaderPrepAnswer removeLeaderPrepAnswer = (RemoveLeaderPrepAnswer) getAnswer();
        if (localGame instanceof LocalMulti) {
            LocalMulti localMulti = (LocalMulti) localGame;
            if(localMulti.getMainPlayerId() == removeLeaderPrepAnswer.getPlayerId()) {
                ArrayList<LocalCard> localCardsToRemove = new ArrayList<>();
                for (int i = 0; i < 4; i++) {
                    if (removeLeaderPrepAnswer.getRemovedLeaderIds().contains(localMulti.getMainPlayer().getLocalBoard().getLeaderCards().get(i).getId())) {
                        localCardsToRemove.add(localMulti.getMainPlayer().getLocalBoard().getLeaderCards().get(i));
                    }
                }
                localMulti.getMainPlayer().getLocalBoard().getLeaderCards().remove(localCardsToRemove);
                localGame.notifyObserver();
            }
            else {
                // if the player is not the mainplayer, i remove two covered cards
                localMulti.getPlayerById(removeLeaderPrepAnswer.getPlayerId()).getLocalBoard().getLeaderCards().remove(0);
                localMulti.getPlayerById(removeLeaderPrepAnswer.getPlayerId()).getLocalBoard().getLeaderCards().remove(0);
            }
            if(localMulti.getState()!=removeLeaderPrepAnswer.getState()) {
                localMulti.setState(removeLeaderPrepAnswer.getState());
                localMulti.notifyObserver();
            }
        } else {
            logger.error("Prep answer received by singlePlayer game");
        }
    }
}
