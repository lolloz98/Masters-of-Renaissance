package it.polimi.ingsw.client.answerhandler.mainaction;

import it.polimi.ingsw.client.answerhandler.AnswerHandler;
import it.polimi.ingsw.client.localmodel.LocalBoard;
import it.polimi.ingsw.client.localmodel.LocalGame;
import it.polimi.ingsw.messages.answers.mainactionsanswer.UseMarketAnswer;

public class UseMarketAnswerHandler extends AnswerHandler {
    public UseMarketAnswerHandler(UseMarketAnswer answer) {
        super(answer);
    }

    @Override
    public void handleAnswer(LocalGame<?> localGame) {
        UseMarketAnswer serverAnswer=(UseMarketAnswer) getAnswer();

        //update turn
        localGame.getLocalTurn().setMarketActivated(true);
        localGame.getLocalTurn().setMainActionOccurred(true);
        localGame.getLocalTurn().notifyObserver();

        //update market
        localGame.getLocalMarket().setResCombinations(serverAnswer.getResCombinations());

    }
}
