package it.polimi.ingsw.client.answerhandler;

import it.polimi.ingsw.client.localmodel.LocalGame;
import it.polimi.ingsw.client.localmodel.LocalMulti;
import it.polimi.ingsw.client.localmodel.LocalSingle;
import it.polimi.ingsw.messages.answers.GameStatusAnswer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Answer Handler that sets all the useful information of the game in the local game when the game is actually created.
 */
public class GameStatusAnswerHandler extends AnswerHandler {
    private static final Logger logger = LogManager.getLogger(GameStatusAnswerHandler.class);

    public GameStatusAnswerHandler(GameStatusAnswer answer) {
        super(answer);
    }

    /**
     * method that updates the game after a game status request.
     *
     * @param localGame
     */
    @Override
    public void handleAnswer(LocalGame<?> localGame) {
        GameStatusAnswer gameStatusAnswer = (GameStatusAnswer) getAnswer();
        if (localGame instanceof LocalMulti) {
            LocalMulti localMulti = (LocalMulti) localGame;
            if (gameStatusAnswer.getGame() instanceof LocalMulti) {
                localMulti.setLocalTurn(((LocalMulti) gameStatusAnswer.getGame()).getLocalTurn());
                localMulti.setLocalPlayers(((LocalMulti) gameStatusAnswer.getGame()).getLocalPlayers());
                localMulti.setMainPlayerId(((LocalMulti) gameStatusAnswer.getGame()).getMainPlayerId());
                localMulti.setState(gameStatusAnswer.getGame().getState());
            } else {
                logger.error("Game type mismatch");
            }
        } else if (localGame instanceof LocalSingle) {
            LocalSingle localSingle = (LocalSingle) localGame;
            if (gameStatusAnswer.getGame() instanceof LocalSingle) {
                localSingle.setLocalTurn(((LocalSingle) gameStatusAnswer.getGame()).getLocalTurn());
                localSingle.setMainPlayer(((LocalSingle) gameStatusAnswer.getGame()).getMainPlayer());
                localSingle.setLorenzoTrack(((LocalSingle) gameStatusAnswer.getGame()).getLorenzoTrack());
            } else {
                logger.error("Game type mismatch");
            }
        }
        localGame.setState(gameStatusAnswer.getGame().getState());
        localGame.setGameId(gameStatusAnswer.getGame().getGameId());
        localGame.setLocalMarket(gameStatusAnswer.getGame().getLocalMarket());
        localGame.setLocalDevelopmentGrid(gameStatusAnswer.getGame().getLocalDevelopmentGrid());
        localGame.notifyObservers();
    }
}
