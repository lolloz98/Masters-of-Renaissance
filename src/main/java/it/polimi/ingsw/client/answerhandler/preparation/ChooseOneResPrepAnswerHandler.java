package it.polimi.ingsw.client.answerhandler.preparation;

import it.polimi.ingsw.client.answerhandler.AnswerHandler;
import it.polimi.ingsw.client.localmodel.LocalBoard;
import it.polimi.ingsw.client.localmodel.LocalGame;
import it.polimi.ingsw.client.localmodel.LocalMulti;
import it.polimi.ingsw.messages.answers.preparationanswer.ChooseOneResPrepAnswer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Answer Handler that handles the choose of initial resources modifying the local game.
 */
public class ChooseOneResPrepAnswerHandler extends AnswerHandler {
    private static final Logger logger = LogManager.getLogger(ChooseOneResPrepAnswerHandler.class);

    public ChooseOneResPrepAnswerHandler(ChooseOneResPrepAnswer answer) {
        super(answer);
    }

    /**
     * method that update the local game after a choose resources request.
     *
     * @param localGame
     */
    @Override
    public void handleAnswer(LocalGame<?> localGame) {
        ChooseOneResPrepAnswer chooseOneResPrepAnswer = (ChooseOneResPrepAnswer) getAnswer();

        if (localGame instanceof LocalMulti) {
            LocalMulti localMulti = (LocalMulti) localGame;
            if (chooseOneResPrepAnswer.getPlayerId() == localMulti.getMainPlayerId()) {
                localMulti.getMainPlayer().getLocalBoard().addResInNormalDepot(chooseOneResPrepAnswer.getRes());
                localMulti.getMainPlayer().getLocalBoard().setInitialRes(localMulti.getMainPlayer().getLocalBoard().getInitialRes() - 1);
            } else {
                LocalBoard localBoard = localMulti.getPlayerById(chooseOneResPrepAnswer.getPlayerId()).getLocalBoard();
                localBoard.addResInNormalDepot(chooseOneResPrepAnswer.getRes());
                localBoard.setInitialRes(localBoard.getInitialRes() - 1);
            }

            if (localMulti.getState() != chooseOneResPrepAnswer.getState()) {
                localMulti.setState(chooseOneResPrepAnswer.getState());
                localGame.notifyObservers();
            }
        } else {
            logger.error("Prep message sent to single player game");
        }

        localGame.getMainPlayer().getLocalBoard().notifyObservers(); // It could be useful, I leave it here
    }
}
