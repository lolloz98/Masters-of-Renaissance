package it.polimi.ingsw.client.answerhandler;

import it.polimi.ingsw.client.localmodel.LocalGame;
import it.polimi.ingsw.messages.answers.Answer;

public class GameStatusAnswerHandler extends AnswerHandler{
    public GameStatusAnswerHandler(Answer answer) {
        super(answer);
    }

    @Override
    public void handleAnswer(LocalGame<?> localGame) {
        // todo
    }
}
