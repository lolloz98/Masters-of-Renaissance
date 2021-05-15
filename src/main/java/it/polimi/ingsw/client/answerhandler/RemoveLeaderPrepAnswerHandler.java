package it.polimi.ingsw.client.answerhandler;

import it.polimi.ingsw.client.localmodel.LocalGame;
import it.polimi.ingsw.messages.answers.preparationanswer.RemoveLeaderPrepAnswer;

public class RemoveLeaderPrepAnswerHandler extends AnswerHandler{
    public RemoveLeaderPrepAnswerHandler(RemoveLeaderPrepAnswer answer) {
        super(answer);
    }

    @Override
    public void handleAnswer(LocalGame<?> localGame) {

    }
}
