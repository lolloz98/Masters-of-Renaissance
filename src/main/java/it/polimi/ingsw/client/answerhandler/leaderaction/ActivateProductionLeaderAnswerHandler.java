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
    /**
     * the production is already passed with the new leader updated in the method of ActivateLeaderAnswerHandler(super-class), so we don't have to update other things
     */
    protected void handleLeaderAnswer(LocalGame<?> localGame) {
        //this method does nothing
    }
}
