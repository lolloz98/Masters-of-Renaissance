package it.polimi.ingsw.client.answerhandler;

import it.polimi.ingsw.client.localmodel.LocalGame;
import it.polimi.ingsw.messages.answers.Answer;

/**
 * abstract class useful to call the correct handleAnswer method on the specific AnswerHandler.
 */
public abstract class AnswerHandler {
    private final Answer answer;

    public AnswerHandler(Answer answer) {
        this.answer = answer;
    }

    public Answer getAnswer() {
        return answer;
    }

    public abstract void handleAnswer(LocalGame<?> localGame);

    /**
     * we synchronize the local game updates.
     * @param localGame
     */
    public void handleAnswerSync(LocalGame<?> localGame){
        synchronized (localGame){
            handleAnswer(localGame);
        }
    }
}
