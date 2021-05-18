package it.polimi.ingsw.client.answerhandler.leaderaction;

import it.polimi.ingsw.client.answerhandler.AnswerHandler;
import it.polimi.ingsw.client.localmodel.LocalGame;
import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.messages.answers.leaderanswer.ActivateMarbleLeaderAnswer;

public class ActivateMarbleLeaderAnswerHandler extends ActivateLeaderAnswerHandler {
    public ActivateMarbleLeaderAnswerHandler(ActivateMarbleLeaderAnswer answer) {
        super(answer);
    }

    @Override
    protected void handleLeaderAnswer(LocalGame<?> localGame) {
        //this method does nothing
    }
}
