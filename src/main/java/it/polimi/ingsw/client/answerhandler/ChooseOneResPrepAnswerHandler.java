package it.polimi.ingsw.client.answerhandler;

import it.polimi.ingsw.client.localmodel.LocalGame;
import it.polimi.ingsw.client.localmodel.LocalMulti;
import it.polimi.ingsw.messages.answers.preparationanswer.ChooseOneResPrepAnswer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ChooseOneResPrepAnswerHandler extends AnswerHandler {
    private static final Logger logger = LogManager.getLogger(ChooseOneResPrepAnswerHandler.class);

    public ChooseOneResPrepAnswerHandler(ChooseOneResPrepAnswer answer) {
        super(answer);
    }

    @Override
    public void handleAnswer(LocalGame<?> localGame) {
        ChooseOneResPrepAnswer chooseOneResPrepAnswer = (ChooseOneResPrepAnswer) getAnswer();
        if (localGame instanceof LocalMulti) {
            LocalMulti localMulti = (LocalMulti) localGame;
            if(chooseOneResPrepAnswer.getPlayerId() == localMulti.getMainPlayerId()) {
                localMulti.getMainPlayer().getLocalBoard().addResInNormalDepot(chooseOneResPrepAnswer.getRes());
                localMulti.notifyObserver();
            }
            if(localMulti.getState()!=chooseOneResPrepAnswer.getState()) {
                localMulti.setState(chooseOneResPrepAnswer.getState());
                localMulti.notifyObserver();
            }
        }
        else{
            logger.error("Prep message sent to single player game");
        }
    }
}
