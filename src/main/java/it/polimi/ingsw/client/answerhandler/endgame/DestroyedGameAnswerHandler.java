package it.polimi.ingsw.client.answerhandler.endgame;

import it.polimi.ingsw.client.answerhandler.AnswerHandler;
import it.polimi.ingsw.client.localmodel.LocalGame;
import it.polimi.ingsw.client.localmodel.LocalGameState;
import it.polimi.ingsw.messages.answers.endgameanswer.DestroyedGameAnswer;

public class DestroyedGameAnswerHandler extends AnswerHandler {
    public DestroyedGameAnswerHandler(DestroyedGameAnswer answer) {
        super(answer);
    }

    @Override
    public void handleAnswer(LocalGame<?> localGame) {
        localGame.setState(LocalGameState.DESTROYED);
        localGame.notifyObservers();
    }
}
