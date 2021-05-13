package it.polimi.ingsw.client.answerhandler;

import it.polimi.ingsw.client.localmodel.LocalGame;
import it.polimi.ingsw.messages.answers.Answer;

public abstract class AnswerHandler {
    private final Answer answer;

    public AnswerHandler(Answer answer) {
        this.answer = answer;
    }

    public Answer getAnswer() {
        return answer;
    }

    public abstract void handleAnswer(LocalGame<?> localGame);
}
