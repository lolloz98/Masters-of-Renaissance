package it.polimi.ingsw.client.answerhandler.leaderaction;

import it.polimi.ingsw.client.answerhandler.AnswerHandler;
import it.polimi.ingsw.client.localmodel.LocalGame;
import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.messages.answers.leaderanswer.ActivateProductionLeaderAnswer;

public class ActivateProductionLeaderAnswerHandler extends ActivateLeaderAnswerHandler {

    public ActivateProductionLeaderAnswerHandler(ActivateProductionLeaderAnswer answer) {
        super(answer);
    }


    @Override
    protected void handleLeaderAnswer(LocalGame<?> localGame) {
        //this method does nothing
    }
}
